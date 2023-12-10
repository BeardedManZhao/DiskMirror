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

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path        路径对象
     * @param inputStream 文件数据流
     * @return {
     * res:上传结果/错误,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     */
    @Override
    protected JSONObject pathProcessorUpload(String path, JSONObject jsonObject, InputStream inputStream) throws IOException {
        final File file = new File(path);
        final File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            final boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                jsonObject.put("res", "文件目录：" + parentFile.getPath() + " 已尝试创建，但是创建操作失败了!!!");
            }
        }
        final Config config = this.getConfig();
        try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            // 然后获取到数据输出流 输出数据
            IOUtils.copy(inputStream, bufferedOutputStream, true);
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
    protected JSONObject pathProcessorGetUrls(String path, JSONObject jsonObject) {
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
            jsonObject1.put("url", string + path + '/' + name);
            jsonObject1.put("size", file.length());
        }
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        return jsonObject;
    }
}
