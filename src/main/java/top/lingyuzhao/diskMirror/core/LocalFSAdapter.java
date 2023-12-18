package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import zhao.utils.IOUtils;

import java.io.*;


/**
 * 本地文件系统适配器
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
        jsonObject.put("url", config.get(Config.PROTOCOL_PREFIX) + path_res);
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) throws IOException {
        // 开始进行文件获取
        final File[] files = new File(path).listFiles();
        if (files == null) {
            jsonObject.put("res", "空间 [" + path + "] 不可读!!!");
            return jsonObject;
        }
        // 获取到协议前缀
        final Config config = this.getConfig();
        final String string = config.getString(Config.PROTOCOL_PREFIX);
        final JSONArray urls = jsonObject.putArray("urls");
        for (File file : files) {
            final JSONObject jsonObject1 = urls.addObject();
            final String name = file.getName();
            jsonObject1.put("fileName", name);
            jsonObject1.put("url", string + path_res + '/' + name);
            jsonObject1.put("lastModified", file.lastModified());
            jsonObject1.put("size", file.length());
        }
        jsonObject.put("useSize", this.getUseSize(jsonObject));
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        return jsonObject;
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
     * 路径处理器 接收一个路径 输出路径中的资源占用量
     *
     * @param path   路径对象 不包含文件名称
     * @param inJson 文件输入的 json 对象 包含空间id 以及 文件类型
     * @return 用户空间的存储占用大小 字节为单位
     */
    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) {
        long res = 0;
        final File[] files = new File(path).listFiles();
        if (files == null) {
            return res;
        }
        for (File file : files) {
            res += file.length();
        }
        return res;
    }
}
