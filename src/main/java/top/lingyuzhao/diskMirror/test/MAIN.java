package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
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

        // 获取到当前适配器所属的版本
        System.out.println("当前适配器版本：" + DiskMirror.HDFSAdapter.getVersion());
        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\arc.png")) {
            // 将文件保存到 1024 号空间
            // 打印结果: {"fileName":"arc.png","userId":1024,"type":"Binary","useAgreement":true,"res":"ok!!!!","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary/arc.png?op=OPEN&"}
            save(adapter, fileInputStream, 1024);
        }


        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果: {"userId":1024,"type":"Binary","useAgreement":true,"urls":[{"fileName":"arc.png","url":"http://192.168.0.141:9870/webhdfs/v1/DiskMirror/1024/Binary//arc.png?op=OPEN&","size":4237376}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果: {"userId":2048,"type":"Binary","useAgreement":true,"urls":[],"res":"ok!!!!"}
        read(adapter, 2048);

        // 删除 1024 空间中的文件 arc.png
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject remove = adapter.remove(jsonObject);
        // 打印结果：{"fileName":"arc.png","userId":1024,"type":"Binary","res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","useAgreement":true,"urls":[],"res":"ok!!!!"}
        read(adapter, 1024);
    }

    private static void read(Adapter adapter, int userId) throws IOException {
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        final JSONObject urls = adapter.getUrls(jsonObject);
        // 打印结果
        System.out.println(urls);
    }

    /**
     * @param adapter         需要使用的适配器对象
     * @param fileInputStream 需要使用的文件数据流
     * @throws IOException 操作异常
     */
    private static void save(Adapter adapter, FileInputStream fileInputStream, int userId) throws IOException {
        // 开始写数据 准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件名字
        jsonObject.put("fileName", "arc.png");
        // 设置文件所属空间id
        jsonObject.put("userId", userId);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 存储文件
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        // 打印保存结果
        System.out.println(upload.toString());
    }
}