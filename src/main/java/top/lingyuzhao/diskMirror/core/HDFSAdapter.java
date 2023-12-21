package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import top.lingyuzhao.diskMirror.conf.Config;
import zhao.utils.IOUtils;
import zhao.utils.StrUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * HDFS 文件系统适配器
 *
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


    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject jsonObject, InputStream inputStream) throws IOException {
        // 首先获取到 HDFS 中的数据流
        final Path path1 = new Path(config.get(Config.FS_DEFAULT_FS) + StrUtils.splitBy(path, '?', 2)[0]);
        if (fileSystem.exists(path1)) {
            throw new IOException("文件《" + jsonObject.getString("fileName") + "》已经存在!");
        }
        try (final FSDataOutputStream fsDataOutputStream = fileSystem.create(path1)) {
            // 输出数据
            IOUtils.copy(inputStream, fsDataOutputStream, true);
        }
        // 返回结果
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        jsonObject.put("url", config.get(Config.PROTOCOL_PREFIX) + path_res);
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) throws IOException {
        final String[] strings = StrUtils.splitBy(path, '?', 2);
        path = strings[0];
        path_res = StrUtils.splitBy(path_res, '?', 2)[0];
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
                    jsonObject1.put("url", string + path_res + '/' + name + "?" + strings[1]);
                    jsonObject1.put("lastModified", subPath.getModificationTime());
                    jsonObject1.put("size", subPath.getLen());
                }
            }
        }
        jsonObject.put("useSize", this.getUseSize(jsonObject));
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) {
        try {
            path = StrUtils.splitBy(path, '?', 2)[0];
            final Path path1 = new Path(path);
            if (!fileSystem.exists(path1)) {
                // 如果不存在就代表不需要删除
                inJson.put(config.getString(Config.RES_KEY), "删除失败!!!文件不存在!");
            }
            final long len = fileSystem.getFileStatus(path1).getLen();
            fileSystem.delete(path1, true);
            inJson.put("useSize", this.diffUseSize(inJson.getIntValue("userId"), inJson.getString("type"), len));
            inJson.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        } catch (IOException e) {
            inJson.put(config.getString(Config.RES_KEY), "删除失败:" + e);
            throw new RuntimeException(e);
        }
        return inJson;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象，这里不强制在返回的地方设置 useSize，会自动获取数据量，当然 如果您希望从自己的算法中获取 useSize 您可以进行设置
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * fileName:旧的文件名字
     * newName:新的文件名字
     * }
     * @throws IOException 操作异常
     */
    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) throws IOException {
        Path oldPath = new Path(path + inJson.getString("fileName"));  //旧的路径
        Path newPath = new Path(path + inJson.getString("newName"));  //新的路径
        if (fileSystem.rename(oldPath, newPath)) {
            inJson.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        } else {
            inJson.put(config.getString(Config.RES_KEY), "重命名失败，请稍后再试!!!");
        }
        return inJson;
    }

    /**
     * 路径处理器 接收一个路径 输出路径中的资源占用量
     *
     * @param path   路径对象 不包含文件名称
     * @param inJson 文件输入的 json 对象 包含空间id 以及 文件类型
     * @return 用户空间的存储占用大小 字节为单位
     */
    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        final String[] strings = StrUtils.splitBy(path, '?', 2);
        path = strings[0];
        final Path path1 = new Path(path);
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.exists(path1) ? fileSystem.listFiles(path1, true) : null;
        long useSize = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                LocatedFileStatus subPath = iterator.next();
                if (subPath.isFile()) {
                    useSize += subPath.getLen();
                }
            }
        }
        return useSize;
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
