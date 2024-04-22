package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.utils.IOUtils;
import top.lingyuzhao.utils.StrUtils;

import java.io.*;
import java.net.Socket;

/**
 * @author zhao
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
                metaO.writeLong(inputStream.available());
                try (final Socket socket1 = new Socket((String) string[0], (Integer) string[2]);
                     final BufferedOutputStream fileO = new BufferedOutputStream(socket1.getOutputStream())) {
                    IOUtils.copy(inputStream, fileO, true);
                    fileO.flush();
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

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) throws IOException {
        try (
                final Socket socket = new Socket((String) string[0], (Integer) string[1]);
                final DataOutputStream metaO = new DataOutputStream(socket.getOutputStream());
                final DataInputStream metaI = new DataInputStream(socket.getInputStream());

        ) {
            metaO.writeUTF("getUrls");
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
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) throws IOException {
        return null;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        return null;
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) throws IOException {
        return null;
    }

    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) throws IOException {
        return null;
    }

    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        return 0;
    }
}
