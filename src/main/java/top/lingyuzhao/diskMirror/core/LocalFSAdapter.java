package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.filter.FileMatchManager;
import top.lingyuzhao.diskMirror.utils.ProgressBar;
import top.lingyuzhao.utils.IOUtils;
import top.lingyuzhao.utils.IOUtils2;
import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * 本地文件系统适配器，支持对于本地文件系统的管理控制
 *
 * @author zhao
 */
public final class LocalFSAdapter extends FSAdapter {


    /**
     * 构建一个适配器
     *
     * @param config 适配器中的配置文件对象
     */
    public LocalFSAdapter(Config config) {
        super(config);
    }

    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject jsonObject, InputStream inputStream, final ProgressBar progressBar) throws IOException {
        final File file = new File(path);
        final File parentFile = file.getParentFile();
        Files.createDirectories(parentFile.toPath());
        if (file.exists()) {
            if (this.isNotOverWrite) {
                throw new IOException("文件《" + jsonObject.getString("fileName") + "》已经存在!");
            }
        }
        try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            // 然后获取到数据输出流 输出数据
            IOUtils2.copy(inputStream, bufferedOutputStream, progressBar);
        } finally {
            progressBar.function3(0);
            IOUtils.close(inputStream);
        }
        // 返回结果
        jsonObject.put(this.resK, this.resOkValue);
        jsonObject.put("url", path_res);
        return jsonObject;
    }

    private JSONObject pathProcessorGetUrls(File path, String path_res, JSONObject jsonObject, Type type) throws IOException {
        // 开始进行文件获取
        final File[] files = path.listFiles();
        if (files == null) {
            jsonObject.put("res", "空间 [" + path + "] 不可读!!!");
            return jsonObject;
        }
        // 获取到协议前缀
        final JSONArray urls = jsonObject.putArray("urls");
        // 获取到文件所在空间类型
        for (File file : files) {
            final JSONObject jsonObject1 = urls.addObject();
            final String name = file.getName();
            final String filePath = path_res + '/' + name;
            jsonObject1.put("fileName", name);
            jsonObject1.put("url", filePath);
            jsonObject1.put("lastModified", file.lastModified());
            jsonObject1.put("size", file.length());
            jsonObject1.put("type", type);
            // 查看当前的是否是一个目录 如果是目录就继续获取到子目录
            if (file.isDirectory()) {
                jsonObject1.put("isDir", true);
                jsonObject1.putAll(this.pathProcessorGetUrls(file, filePath, jsonObject1.clone(), type));
            } else {
                jsonObject1.put("isDir", false);
            }
        }
        return jsonObject;
    }

    /**
     * 不递归的获取文件列表 只可以获取到当前目录下的文件列表
     *
     * @param path       当前要迭代的目录路径
     * @param path_res   可以用于拼接 url 的路径
     * @param jsonObject 返回结果对象
     * @param type       空间类型
     * @return 结果
     */
    private JSONObject pathProcessorGetUrlsNoRecursion(File path, String path_res, JSONObject jsonObject, Type type) {
        // 开始进行文件获取
        final File[] files = path.listFiles();
        if (files == null) {
            jsonObject.put("res", "空间 [" + path + "] 不可读!!!");
            return jsonObject;
        }
        // 获取到协议前缀
        final JSONArray urls = jsonObject.putArray("urls");
        // 获取到文件所在空间类型
        for (File file : files) {
            final JSONObject jsonObject1 = urls.addObject();
            final String name = file.getName();
            jsonObject1.put("fileName", name);
            jsonObject1.put("url", path_res + '/' + name);
            jsonObject1.put("lastModified", file.lastModified());
            jsonObject1.put("size", file.length());
            jsonObject1.put("type", type);
            jsonObject1.put("isDir", file.isDirectory());
        }
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) throws IOException {
        JSONObject jsonObject1 = pathProcessorGetUrls(new File(path), path_res, jsonObject, jsonObject.getObject("type", Type.class));
        jsonObject1.put(this.resK, this.resOkValue);
        return jsonObject1;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, String nowPath, JSONObject inJson) {
        JSONObject jsonObject = pathProcessorGetUrlsNoRecursion(new File(nowPath), path_res, inJson, inJson.getObject("type", Type.class));
        jsonObject.put(this.resK, this.resOkValue);
        return jsonObject;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {"res": 创建结果}
     */
    @Override
    protected JSONObject pathProcessorMkDirs(String path, JSONObject inJson) {
        inJson.put(this.resK, new File(path).mkdirs() ? this.resOkValue : "文件目录创建失败，可能文件目录已经存在了!!!");
        return inJson;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        return new BufferedInputStream(Files.newInputStream(Paths.get(path)));
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject jsonObject, FileMatchManager fileMatchManager, Transformation<KeyValue<Long, String>, Boolean> filter, boolean allowDirNoDelete) throws IOException {
        // 开始进行删除操作
        final File file = new File(path);
        if (!file.exists()) {
            jsonObject.put(this.resK, "删除失败!!!文件不存在! ");
            return jsonObject;
        }
        if (file.isDirectory()) {
            // 如果是文件夹就使用文件夹的删除方法
            jsonObject.put("useSize", this.diffUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), this.rDelete(file, filter, allowDirNoDelete)));
            jsonObject.put(this.resK, this.resOkValue);
            return jsonObject;
        }
        final long length = file.length();

        if (file.delete()) {
            // 删除成功
            jsonObject.put("useSize", this.diffUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), length));
            jsonObject.put(this.resK, this.resOkValue);
        } else {
            jsonObject.put(this.resK, "删除失败!!!");
        }
        return jsonObject;
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
     */
    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) {
        final String fileName = inJson.getString("fileName");
        final File file = new File(path + fileName);
        if (!file.exists()) {
            inJson.put(this.resK, "重命名失败，文件《" + fileName + "》不存在!");
        } else {
            if (file.renameTo(new File(path + inJson.getString("newName")))) {
                inJson.put(this.resK, this.resOkValue);
            } else {
                inJson.put(this.resK, "重命名失败，请稍后再试!!!（可能是您重命名之后的文件路径的父目录不存在）");
            }
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
    protected long pathProcessorUseSize(String path, JSONObject inJson) {
        return pathProcessorUseSize(new File(path));
    }

    /**
     * 计算出路径中的资源占用量
     *
     * @param path 文件路径
     * @return 指定路径中所有文件的占用大小
     */
    private long pathProcessorUseSize(File path) {
        long res = 0;
        final File[] files = path.listFiles();
        if (files == null) {
            return res;
        }
        for (File file : files) {
            // 判断是否是一个文件夹
            if (file.isDirectory()) {
                // 是文件夹就计算文件夹内部的值
                res += pathProcessorUseSize(file);
                continue;
            }
            res += file.length();
        }
        return res;
    }

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param file1            需要被删除的文件目录
     * @param filter           过滤器 如果返回 true 才会被删除
     * @param allowDirNoDelete 是否允许目录不被完全删除
     * @return 删除的字节数值
     * @throws IOException 删除操作出现异常
     */
    public long rDelete(final File file1, final Transformation<KeyValue<Long, String>, Boolean> filter, final boolean allowDirNoDelete) throws IOException {
        // 判断路径是否存在
        if (!file1.exists()) {
            return 0;
        }
        // 判断路径是否为目录
        if (!file1.isDirectory()) {
            final long length = file1.length();
            if (file1.delete()) {
                return length;
            } else {
                throw new IOException("Failed to delete file: " + file1.getName());
            }
        }
        // 递归删除目录及其内容
        long bytesDeleted = 0;
        File[] files = file1.listFiles();
        if (files == null) {
            return 0;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                bytesDeleted += rDelete(file, filter, allowDirNoDelete);
            } else {
                // 判断是否需要删除
                final KeyValue<Long, String> longStringKeyValue = new KeyValue<>(file.lastModified(), file.getName());
                // 判断是否需要删除
                if (filter.function(longStringKeyValue)) {
                    // 删除文件
                    final long length = file.length();
                    if (file.delete()) {
                        bytesDeleted += length;
                    } else {
                        throw new IOException("Failed to delete file: " + file.getName());
                    }
                }
            }
        }
        // 然后删除自己
        if (file1.delete() || allowDirNoDelete) {
            return bytesDeleted + file1.length();
        } else {
            throw new IOException("Failed to delete directory: " + file1.getName());
        }
    }
}
