package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 实例化配置类
        final Config config = new Config();
        // TODO 设置 路径的协议前缀 在这里指向的是 HDFS 文件系统 盘镜目录 访问地址
        config.put(Config.PROTOCOL_PREFIX, "http://192.168.0.141:9870/webhdfs/v1/DiskMirror");
        // TODO 设置 HDFS 协议前缀 在这里指向的是 HDFS 文件系统访问地址 是 hdfs 开头
        config.put(Config.FS_DEFAULT_FS, "hdfs://192.168.0.141:8020");
        // TODO 设置请求参数 HDFS 要求需要设置 OP 参数
        final JSONObject params = config.putObject(Config.PARAMS);
        params.put("op", "OPEN");
        // TODO 装载到适配器 在这里使用HDFS文件系统
        final Adapter adapter = DiskMirror.HDFSAdapter.getAdapter(config);
        // 开始查询 1024 空间目录
        System.out.println(read(adapter, 1024));
        // 对目录中的文件 《defimage2.svg.png》 重命名为 test.png
        System.out.println(reName(adapter, 1024, "defimage2.svg.png", "test.png"));
        // 再一次查询 1024 空间目录
        System.out.println(read(adapter, 1024));
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

    private static JSONObject reName(Adapter adapter, int userId, String oldFileName, String newFileName) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置文件的旧名字 和 新名字
        jsonObject.put("fileName", oldFileName);
        jsonObject.put("newName", newFileName);
        return adapter.reName(jsonObject);
    }
}