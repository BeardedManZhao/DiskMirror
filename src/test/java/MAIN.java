import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.TcpClientAdapter;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 使用适配器对象 用来接收远程数据并进行处理
        final TcpClientAdapter adapterPacking0 = (TcpClientAdapter) DiskMirror.TCP_CLIENT_Adapter.getAdapter(
                ConfigAdapter.class
        );

        try (final FileInputStream fileInputStream = new FileInputStream("F:\\JianYingOutput\\10月21日.mp4")) {
            final JSONObject upload = adapterPacking0.upload(fileInputStream, DiskMirrorRequest.uploadRemove(1, Type.Binary, "10_21.mp4"));
            System.out.println(upload);
        }
    }

    // TCP 适配器中所包含的子适配器的配置，TCP适配器实现了包装器 因此可以将任意一种适配器接入到TCP的数据传输中
    // 这里就是子适配器的配置
    // TCP 客户端适配器配置 在这里指定的就是 TCP 适配器所在的主机 和 元数据端口 文件端口
    @DiskMirrorConfig(
            fsDefaultFS = "192.168.1.200:10001,10002"
    )
    public static final class ConfigAdapter {
    }
}