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
@DiskMirrorConfig(fsDefaultFS = "https://diskMirror.lingyuzhao.top/DiskMirrorBackEnd")
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        final int i = adapter.setSpaceSk("1", 1001101010);
        // 这里的 i 就是更新之后的密钥了
        System.out.println(i);
        // 可以使用新密钥操作 TODO 假设服务器设置了 "SkCheckModule$read"
        final JSONObject urls = adapter.getUrls(
                DiskMirrorRequest.getUrls(1, Type.Binary).setSk(i)
        );
        System.out.println(urls);
    }
}