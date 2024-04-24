package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// TCP 客户端适配器配置 在这里指定的就是 TCP 适配器所在的主机 和 元数据端口 文件端口
@DiskMirrorConfig(
        fsDefaultFS = "127.0.0.1:10001,10002"
)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        System.out.println("开始发送数据！");
        // 实例化出 Tcp 客户端适配器
        final Adapter adapter = DiskMirror.TCP_CLIENT_Adapter.getAdapter(MAIN2.class);
        // 直接将 TCP 客户端适配器中的 upload 方法进行调用
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        jsonObject.put("secure.key", 0);
        jsonObject.put("fileName", "test1.jpg");

        // 删除名为 test1.jpg 的文件
        final JSONObject remove = adapter.remove(jsonObject);
        System.out.println(remove);

        // 再将 test1.jpg 上传
        final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\arc.png");
        final JSONObject upload = adapter.upload(fileInputStream, jsonObject);
        System.out.println(upload);

        // 下载文件
        try (
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream outputStream = new FileOutputStream("./out.jpg")
        ) {
            IOUtils.copy(inputStream, outputStream, true);
        }

        // 把 test1.jpg 重命名为 test2.jpg
        jsonObject.put("newName", "test2.jpg");
        final JSONObject jsonObject1 = adapter.reName(jsonObject);
        System.out.println(jsonObject1);
        jsonObject.remove("newName");

        // 查看文件结构
        jsonObject.remove("fileName");
        final JSONObject urls = adapter.getUrls(jsonObject);
        System.out.println(urls);

        // 查看版本
        System.out.println(adapter.version());
    }
}