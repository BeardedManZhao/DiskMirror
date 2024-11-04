package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.ioStream.AutoCloseableInputStream;
import top.lingyuzhao.diskMirror.utils.ProgressBar;
import top.lingyuzhao.utils.IOUtils;
import top.lingyuzhao.utils.StrUtils;

import java.io.*;
import java.net.Socket;

/**
 * 专门用来对接 tcp 适配器 服务端的客户端适配器！
 *
 * @author 赵凌宇
 */
public class TcpClientAdapter extends FSAdapter {

    /**
     * 服务信息 【服务I，服务元数据端口，服务文件端口】
     */
    final Object[] string = new Object[3];


    /**
     * 构建一个适配器
     *
     * @param config 适配器中的配置文件对象
     */
    public TcpClientAdapter(Config config) {
        super(config);
        final String[] strings = StrUtils.splitBy(config.getString(Config.FS_DEFAULT_FS), ':');
        string[0] = strings[0];
        final String[] strings1 = StrUtils.splitBy(strings[1], ',');
        string[1] = Integer.parseInt(strings1[0]);
        string[2] = Integer.parseInt(strings1[1]);
    }


    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject inJson, InputStream inputStream) throws IOException {
        try (
                final Socket socket = new Socket((String) string[0], (Integer) string[1]);
                final DataOutputStream metaO = new DataOutputStream(socket.getOutputStream());
                final DataInputStream metaI = new DataInputStream(socket.getInputStream());

        ) {
            metaO.writeUTF("upload");
            metaO.writeUTF(inJson.toString());
            // 等待回复
            final String s = metaI.readUTF();
            if (s.equals("ok")) {
                final Long streamSize = inJson.getLong("streamSize");
                metaO.writeLong(streamSize);
                final ProgressBar progressBar = new ProgressBar(inJson.getString("userId"), inJson.getString("fileName"));
                progressBar.setMaxSize(streamSize);
                try (final Socket socket1 = new Socket((String) string[0], (Integer) string[2]);
                     final BufferedOutputStream fileO = new BufferedOutputStream(socket1.getOutputStream())) {
                    IOUtils.copy(streamSize, inputStream, fileO, progressBar);
                    fileO.flush();
                } finally {
                    progressBar.function3(0);
                    IOUtils.close(inputStream);
                }
            }
            final String s1 = metaI.readUTF();
            try {
                return JSONObject.parse(s1);
            } catch (Exception e) {
                throw new RuntimeException(s1);
            }
        }
    }

    private JSONObject getResJsonObject(String mkdirs, JSONObject inJson) throws IOException {
        try (
                final Socket socket = new Socket((String) string[0], (Integer) string[1]);
                final DataOutputStream metaO = new DataOutputStream(socket.getOutputStream());
                final DataInputStream metaI = new DataInputStream(socket.getInputStream());

        ) {
            metaO.writeUTF(mkdirs);
            metaO.writeUTF(inJson.toString());
            final String s = metaI.readUTF();
            try {
                return JSONObject.parse(s);
            } catch (Exception e) {
                throw new RuntimeException(s);
            }
        }
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) throws IOException {
        return getResJsonObject("getUrls", inJson);
    }

    @Override
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) throws IOException {
        return getResJsonObject("mkdirs", inJson);
    }


    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        final Socket socket = new Socket((String) string[0], (Integer) string[1]);
        final DataOutputStream metaO = new DataOutputStream(socket.getOutputStream());
        final AutoCloseableInputStream autoCloseableInputStream = new AutoCloseableInputStream(socket);
        metaO.writeUTF("download");
        metaO.writeUTF(inJson.toString());
        return autoCloseableInputStream;
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) throws IOException {
        return getResJsonObject("remove", inJson);
    }

    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) throws IOException {
        return getResJsonObject("reName", inJson);
    }

    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        try (
                final Socket socket = new Socket((String) string[0], (Integer) string[1]);
                final DataOutputStream metaO = new DataOutputStream(socket.getOutputStream());
                final DataInputStream metaI = new DataInputStream(socket.getInputStream());

        ) {
            metaO.writeUTF("useSize");
            metaO.writeUTF(inJson.toString());
            return metaI.readLong();
        }
    }

    @Override
    public JSONObject upload(InputStream inputStream, JSONObject jsonObject, long streamSize) throws IOException {
        // 首先获取到使用的空间占用
        jsonObject.put("streamSize", streamSize < 0 ? inputStream.available() : streamSize);
        return this.pathProcessorUpload("", "", jsonObject, inputStream);
    }

    @Override
    public InputStream downLoad(JSONObject jsonObject) throws IOException {
        return this.pathProcessorDownLoad("", jsonObject);
    }

    @Override
    public JSONObject remove(JSONObject jsonObject) throws IOException {
        return this.pathProcessorRemove("", jsonObject);
    }

    @Override
    public JSONObject reName(JSONObject jsonObject) throws IOException {
        return this.pathProcessorReName("", jsonObject);
    }

    @Override
    public JSONObject getUrls(JSONObject jsonObject) throws IOException {
        return this.pathProcessorGetUrls("", "", jsonObject);
    }

    @Override
    public JSONObject mkdirs(JSONObject jsonObject) throws IOException {
        return this.pathProcessorMkdirs("", jsonObject);
    }

    @Override
    public long getUseSize(JSONObject jsonObject, String path) throws IOException {
        return this.pathProcessorUseSize(path, jsonObject);
    }

    @Override
    public long getUseSize(JSONObject jsonObject) throws IOException {
        return this.pathProcessorUseSize("", jsonObject);
    }

    @Override
    public long diffUseSize(int id, String type, long size) throws IOException {
        throw new UnsupportedOperationException("The client does not support the operation: diffUseSize");
    }

    @Override
    public long addUseSize(int id, String type, long size) throws IOException {
        throw new UnsupportedOperationException("The client does not support the operation: addUseSize");
    }

    @Override
    public void removeUseSize(int id, String type) {
        throw new UnsupportedOperationException("The client does not support the operation: removeUseSize");
    }

    @Override
    public void setSpaceMaxSize(String spaceId, long maxSize) {
        throw new UnsupportedOperationException("The client does not support the operation: setSpaceMaxSize");
    }

    @Override
    public long getSpaceMaxSize(String id) {
        throw new UnsupportedOperationException("The client does not support the operation: getSpaceMaxSize");
    }


}
