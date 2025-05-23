package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.filter.FileMatchManager;
import top.lingyuzhao.diskMirror.utils.ProgressBar;
import top.lingyuzhao.utils.IOUtils;
import top.lingyuzhao.utils.IOUtils2;
import top.lingyuzhao.utils.StrUtils;
import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

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
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject jsonObject, InputStream inputStream, ProgressBar progressBar) throws IOException {
        // 首先获取到 HDFS 中的数据流
        final Path path1 = new Path(config.get(Config.FS_DEFAULT_FS) + path);
        if (fileSystem.exists(path1)) {
            if (this.isNotOverWrite) {
                throw new IOException("文件《" + jsonObject.getString("fileName") + "》已经存在!");
            }
        }
        try (final FSDataOutputStream fsDataOutputStream = fileSystem.create(path1)) {
            // 输出数据
            IOUtils2.copy(inputStream, fsDataOutputStream, progressBar);
        } finally {
            progressBar.function3(0);
            IOUtils.close(inputStream);
        }
        // 返回结果
        jsonObject.put(this.resK, this.resOkValue);
        jsonObject.put("url", path_res);
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) throws IOException {
        final String[] strings = StrUtils.splitBy(path, '?', 2);
        path = strings.length > 0 ? strings[0] : path;
        path_res = StrUtils.splitBy(path_res, '?', 2)[0];
        return pathProcessorGetUrls(path, path_res, jsonObject, strings.length > 1 ? strings[1] : "");
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, String nowPath, JSONObject inJson) throws IOException {
        final String[] strings = StrUtils.splitBy(path, '?', 2);
        path = strings.length > 0 ? strings[0] : path;
        path_res = StrUtils.splitBy(path_res, '?', 2)[0];
        nowPath = StrUtils.splitBy(nowPath, '?', 2)[0];
        return pathProcessorGetUrlsNoRecursion(nowPath, path, path_res, inJson, strings.length > 1 ? strings[1] : "");
    }


    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject, String paramStr) throws IOException {
        final Path path1 = new Path(path);
        RemoteIterator<FileStatus> iterator = fileSystem.exists(path1) ? fileSystem.listStatusIterator(path1) : null;
        final JSONArray urls = jsonObject.putArray("urls");
        // 将所有的子文件添加到数组中
        if (iterator != null) {
            while (iterator.hasNext()) {
                FileStatus subPath = iterator.next();
                final JSONObject jsonObject1 = urls.addObject();
                final Path path2 = subPath.getPath();
                final String name = path2.getName();
                final String filePath_HDFS = path + '/' + name;
                final String fnNoParam = path_res + name;
                final String filePath = fnNoParam + "?" + paramStr;
                jsonObject1.put("fileName", name);
                jsonObject1.put("url", filePath);
                jsonObject1.put("lastModified", subPath.getModificationTime());
                jsonObject1.put("size", subPath.getLen());
                jsonObject1.put("type", jsonObject.get("type"));
                if (subPath.isFile()) {
                    jsonObject1.put("isDir", false);
                } else {
                    // 如果是目录就继续获取到子目录
                    jsonObject1.put("isDir", true);
                    jsonObject1.putAll(this.pathProcessorGetUrls(filePath_HDFS, fnNoParam + '/', jsonObject1.clone(), paramStr));
                    jsonObject1.remove("useSize");
                    jsonObject1.remove(this.resK);
                }
            }
        }
        return jsonObject;
    }

    private static String extractedFileMetaToJsonGetUrlPrefix(String path_res, JSONObject jsonObject, String paramStr, FileStatus subPath, JSONObject jsonObject1) {
        final Path path2 = subPath.getPath();
        final String name = path2.getName();
        final String fnNoParam = path_res + name, filePath = fnNoParam + "?" + paramStr;
        jsonObject1.put("fileName", name);
        jsonObject1.put("url", filePath);
        jsonObject1.put("lastModified", subPath.getModificationTime());
        jsonObject1.put("size", subPath.getLen());
        jsonObject1.put("type", jsonObject.get("type"));
        jsonObject1.put("isDir", subPath.isDirectory());
        return fnNoParam;
    }

    protected JSONObject pathProcessorGetUrlsNoRecursion(String nowPath, String spacePath, String path_res, JSONObject jsonObject, String paramStr) throws IOException {
        final Path path1 = new Path(nowPath);
        final boolean exists = fileSystem.exists(path1);
        if (!exists) {
            jsonObject.put("res", "空间 [" + nowPath + "] 不存在!!!");
            return jsonObject;
        }
        final RemoteIterator<FileStatus> iterator = fileSystem.listStatusIterator(path1);
        // 提取出被操作的文件/目录的元数据
        final String s = extractedFileMetaToJsonGetUrlPrefix(path_res, jsonObject, paramStr, fileSystem.getFileStatus(path1), jsonObject) + '/';
        final JSONArray urls = jsonObject.putArray("urls");
        // 然后再将所有的子文件添加到数组中
        if (iterator != null) {
            while (iterator.hasNext()) {
                final FileStatus subPath = iterator.next();
                final JSONObject jsonObject1 = urls.addObject();
                extractedFileMetaToJsonGetUrlPrefix(s, jsonObject, paramStr, subPath, jsonObject1);
            }
        }
        jsonObject.put("useSize", this.getUseSize(jsonObject, spacePath));
        jsonObject.put(this.resK, this.resOkValue);
        return jsonObject;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {"res": 创建结果}
     * @throws IOException 操作异常
     */
    @Override
    protected JSONObject pathProcessorMkDirs(String path, JSONObject inJson) throws IOException {
        inJson.put(this.resK, fileSystem.mkdirs(new Path(path)) ? this.resOkValue : "创建失败，可能文件目录已经存在，或者无法连接到 HDFS 服务器");
        return inJson;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        return fileSystem.open(new Path(path));
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson, FileMatchManager fileMatchManager, Transformation<KeyValue<Long, String>, Boolean> filter, boolean allowDirNoDelete) {
        try {
            final Path path1 = new Path(path);
            if (!fileSystem.exists(path1)) {
                // 如果不存在就代表不需要删除
                inJson.put(this.resK, "删除失败!!!文件不存在!");
            }
            final long useSize;
            {
                if (fileMatchManager == FileMatchManager.ALLOW_ALL) {
                    useSize = this.rDelete(path1);
                } else {
                    useSize = this.rDelete(path1, filter);
                }
            }
            inJson.put("useSize", this.diffUseSize(inJson.getIntValue("userId"), inJson.getString("type"), useSize));
            inJson.put(this.resK, this.resOkValue);
        } catch (IOException e) {
            inJson.put(this.resK, "删除失败:" + e);
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
            inJson.put(this.resK, this.resOkValue);
        } else {
            inJson.put(config.getString(Config.RES_KEY), "重命名失败，请稍后再试!!!（可能是您重命名之后的文件路径的父目录不存在）");
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
        final Path path1 = new Path(path);
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.exists(path1) ? fileSystem.listFiles(path1, true) : null;
        long useSize = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                LocatedFileStatus subPath = iterator.next();
                if (subPath.isFile()) {
                    useSize += subPath.getLen();
                    continue;
                }
                // 如果是个目录就递归
                useSize += this.pathProcessorUseSize(path + '/' + subPath.getPath().getName(), inJson);
            }
        }
        return useSize;
    }

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path 需要被删除的文件目录
     * @return 删除的字节数值
     * @throws IOException 删除操作出现异常
     */
    public long rDelete(String path) throws IOException {
        final Path path1 = new Path(path);
        return rDelete(path1);
    }

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path 需要被删除的文件目录
     * @throws IOException 删除操作出现异常
     */
    private long rDelete(final Path path) throws IOException {
        final ContentSummary contentSummary = fileSystem.getContentSummary(path);
        final long used = contentSummary.getLength();
        if (fileSystem.delete(path, true)) {
            return used;
        }
        return 0;
    }

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path   需要被删除的文件目录
     * @param filter 删除过滤器 如果返回 true 则删除，否则不删除
     * @throws IOException 删除操作出现异常
     */
    private long rDelete(final Path path, final Transformation<KeyValue<Long, String>, Boolean> filter) throws IOException {
        long used = 0;
        if (!fileSystem.exists(path)) {
            return 0;
        }

        // 如果是一个文件或者满足过滤条件的文件/目录，则直接删除
        final FileStatus fileStatus = fileSystem.getFileStatus(path);
        if (!fileStatus.isDirectory()) {
            return rDelete(path);
        }

        // 递归删除目录及其内容
        long okCount = 0, allCount = 0;
        // 如果是目录，需要递归处理其内容
        final RemoteIterator<FileStatus> statusIterator = fileSystem.listStatusIterator(path);
        while (statusIterator.hasNext()) {
            ++allCount;
            final FileStatus status = statusIterator.next();
            final Path path1 = status.getPath();
            final KeyValue<Long, String> longStringKeyValue = new KeyValue<>(status.getModificationTime(), path1.getName());
            if (filter.function(longStringKeyValue)) {
                ++okCount;
                used += rDelete(path1, filter);
            }
        }
        if (allCount != okCount) {
            return used;
        }
        // 然后删除自己
        fileSystem.delete(path, false); // false 表示不递归删除
        return used;
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
