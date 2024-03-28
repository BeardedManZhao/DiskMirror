package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.utils.IOUtils;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject jsonObject, InputStream inputStream) throws IOException {
        final File file = new File(path);
        final File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            final boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                jsonObject.put("res", "文件目录：" + parentFile.getPath() + " 已尝试创建，但是创建操作失败了!!!");
            }
        }
        if (file.exists()) {
            throw new IOException("文件《" + jsonObject.getString("fileName") + "》已经存在!");
        }
        final Config config = this.getConfig();
        try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            // 然后获取到数据输出流 输出数据
            IOUtils.copy(inputStream, bufferedOutputStream, true);
        }
        // 返回结果
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
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
        final Config config = this.getConfig();
        final JSONArray urls = jsonObject.putArray("urls");
        final String res_key = config.getString(Config.RES_KEY);
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
            // 查看当前的是否是一个目录 如果是目录就继续获取到字目录
            if (file.isDirectory()) {
                jsonObject1.put("isDir", true);
                jsonObject1.putAll(this.pathProcessorGetUrls(file, filePath, jsonObject1.clone(), type));
                jsonObject1.remove("useSize");
                jsonObject1.remove(res_key);
            } else {
                jsonObject1.put("isDir", false);
            }
        }
        jsonObject.put("useSize", this.getUseSize(jsonObject, path.getPath()));
        jsonObject.put(res_key, config.getString(Config.OK_VALUE));
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) throws IOException {
        return pathProcessorGetUrls(new File(path), path_res, jsonObject, jsonObject.getObject("type", Type.class));
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {"res": 创建结果}
     */
    @Override
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) {
        inJson.put(config.getString(Config.RES_KEY), new File(path).mkdirs() ? config.getString(Config.OK_VALUE) : "文件目录创建失败，可能文件目录已经存在了!!!");
        return inJson;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException {
        return new BufferedInputStream(Files.newInputStream(Paths.get(path)));
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject jsonObject) throws IOException {
        final Config config = this.getConfig();
        // 开始进行删除操作
        final File file = new File(path);
        if (!file.exists()) {
            jsonObject.put(config.getString(Config.RES_KEY), "删除失败!!!文件不存在!");
            return jsonObject;
        }
        if (file.isDirectory()) {
            // 如果是文件夹就使用文件夹的删除方法
            jsonObject.put("useSize", this.diffUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), rDelete(path)));
            jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
            return jsonObject;
        }
        final long length = file.length();
        if (file.delete()) {
            // 删除成功
            jsonObject.put("useSize", this.diffUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), length));
            jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        } else {
            jsonObject.put(config.getString(Config.RES_KEY), "删除失败!!!");
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
        System.out.println(inJson);
        final String fileName = inJson.getString("fileName");
        final File file = new File(path + fileName);
        if (!file.exists()) {
            inJson.put(config.getString(Config.RES_KEY), "重命名失败，文件《" + fileName + "》不存在!");
        } else {
            if (file.renameTo(new File(path + inJson.getString("newName")))) {
                inJson.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
            } else {
                inJson.put(config.getString(Config.RES_KEY), "重命名失败，请稍后再试!!!（可能是您重命名之后的文件路径的父目录不存在）");
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
     * @param path 需要被删除的文件目录
     * @return 删除的字节数值
     * @throws IOException 删除操作出现异常
     */
    public long rDelete(String path) throws IOException {
        // 判断路径是否存在
        Path dir = Paths.get(path);
        if (!Files.exists(dir)) {
            throw new IOException("Directory does not exist.");
        }
        // 判断路径是否为目录
        if (!Files.isDirectory(dir)) {
            throw new IOException("Path is not a directory.");
        }
        // 递归删除目录及其内容
        long bytesDeleted = 0;
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
            for (Path file : directoryStream) {
                if (Files.isDirectory(file)) {
                    bytesDeleted += rDelete(file.toString());
                } else {
                    bytesDeleted += Files.size(file);
                    Files.delete(file);
                }
            }
        } finally {
            // 最后删除目录本身
            bytesDeleted += Files.size(dir);
            Files.delete(dir);
        }
        return bytesDeleted;
    }
}
