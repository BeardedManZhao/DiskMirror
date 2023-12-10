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
        // 设置 路径的协议前缀 默认是 http://localhost:8080
        config.put(Config.PROTOCOL_PREFIX, "http://xxx.xxx");
        // 装载到适配器 在这里使用本地文件系统
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);


        // 准备一个文件数据流
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\arc.png")) {
            // 将文件保存到 1024 号空间
            // 打印结果: {"fileName":"arc.png","userId":1024,"type":"Binary","res":"ok!!!!","url":"http://xxx.xxx/DiskMirror/1024/Binary/arc.png"}
            save(adapter, fileInputStream, 1024);
        }


        // 读取 1024 号空间中的所有 url 由于刚刚保存文件的操作出现在这里 所以 1024 号空间应该是有刚才的文件的
        // 打印结果: {"userId":1024,"type":"Binary","urls":[{"fileName":"arc.png","url":"http://xxx.xxx/DiskMirror/1024/Binary//arc.png","size":4237376}],"res":"ok!!!!"}
        read(adapter, 1024);


        // 读取 2048 号空间中的所有 url 这里并没有进行过保存 所以在这里的空间是没有数据的
        // 打印结果: {"userId":2048,"type":"Binary","res":"空间 [/DiskMirror/2048/Binary/] 不可读!!!"}
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
        // 打印结果：{"res":"ok!!!!"}
        System.out.println(remove);

        // 删除之后再次查看 1024 空间中的目录
        // 打印结果：{"userId":1024,"type":"Binary","urls":[],"res":"ok!!!!"}
        read(adapter, 1024);
    }

    private static void read(Adapter adapter, int userId) {
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
