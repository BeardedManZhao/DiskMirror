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
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject jsonObject) {
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
        jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        return jsonObject;
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject jsonObject) {
        final Config config = this.getConfig();
        // 开始进行删除操作
        final File file = new File(path);
        if (file.delete()) {
            // 删除成功
            jsonObject.put(config.getString(Config.RES_KEY), config.getString(Config.OK_VALUE));
        } else {
            jsonObject.put(config.getString(Config.RES_KEY), "删除失败!!!");
        }
        return jsonObject;
    }
}
