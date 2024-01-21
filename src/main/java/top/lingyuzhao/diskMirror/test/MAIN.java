package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Config config = new Config();
        config.put(Config.ROOT_DIR, "/DiskMirror");
        config.put(Config.PROTOCOL_PREFIX, "");
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        // 使用适配器获取到文件url
        final JSONObject read = read(adapter, 1024);
        System.out.println(read);
    }

    private static JSONObject read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        return adapter.getUrls(jsonObject);
    }
}