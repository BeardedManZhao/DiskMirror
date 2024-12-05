import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备一个请求对象
        final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.getUrls(1, Type.Binary)
                // 可以手动关闭协议前缀，这样性能更好
                .setProtocolPrefix(false);
        // 发送获取文件目录的请求
        final JSONObject urls = adapter.getUrls(diskMirrorRequest);
        // 打印结果
        System.out.println(urls);
    }
}