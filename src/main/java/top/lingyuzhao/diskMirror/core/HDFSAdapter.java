package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import top.lingyuzhao.diskMirror.conf.Config;
import zhao.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
public class HDFSAdapter extends FSAdapter {

    protected final FileSystem fileSystem;

    /**
     * 构建一个适配器
     *
     * @param config 适配器中的配置文件对象
     */
    public HDFSAdapter(Config config) {
        super(config);
        final Configuration entries = new Configuration();
        config.forEach((k, v) -> entries.set(k, v.toString()));
        try {
            fileSystem = FileSystem.get(entries);
        } catch (IOException e) {
            throw new UnsupportedOperationException("HDFS 适配器初始化失败!", e);
        }
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path        路径对象
     * @param jsonObject  输入参数 json 对象
     * @param inputStream 文件数据流
     * @return {
     * res:上传结果/错误,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    @Override
    protected JSONObject pathProcessorUpload(String path, JSONObject jsonObject, InputStream inputStream) throws IOException {
        // 首先获取到 HDFS 中的数据流
        try (final FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(config.get(Config.FS_DEFAULT_FS) + path))) {
            // 输出数据
            IOUtils.copy(inputStream, fsDataOutputStream, true);
        }
        // 返回结果
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        jsonObject.put("url", config.get(Config.PROTOCOL_PREFIX) + path);
        return jsonObject;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path       路径对象
     * @param jsonObject 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * urls:[{url:文件的url, size:文件的大小, name:文件的名字}]
     * }
     */
    @Override
    protected JSONObject pathProcessorGetUrls(String path, JSONObject jsonObject) throws IOException {
        final Path path1 = new Path(path);
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.exists(path1) ? fileSystem.listFiles(path1, true) : null;
        final JSONArray urls = jsonObject.putArray("urls");
        // 将所有的子文件添加到数组中
        final String string = config.getString(Config.PROTOCOL_PREFIX);
        if (iterator != null) {
            while (iterator.hasNext()) {
                LocatedFileStatus subPath = iterator.next();
                if (subPath.isFile()) {
                    final JSONObject jsonObject1 = urls.addObject();
                    final Path path2 = subPath.getPath();
                    final String name = path2.getName();
                    jsonObject1.put("fileName", name);
                    jsonObject1.put("url", string + path + '/' + name);
                    jsonObject1.put("size", subPath.getLen());
                }
            }
        }
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        return jsonObject;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * }
     */
    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) {
        try {
            fileSystem.delete(new Path(path), true);
            inJson.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        } catch (IOException e) {
            inJson.put(config.getString(Config.RES_KEY), "删除失败:" + e);
            throw new RuntimeException(e);
        }
        return inJson;
    }

    /**
     * 关闭适配器
     */
    @Override
    public void close() {
        super.close();
        IOUtils.close(fileSystem);
    }
}
