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
@DiskMirrorConfig(
        secureKey = 123456
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 对象 注意这里使用的是注解中的密钥 123456
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 为 1号 用户空间生成安全密钥 这里返回的是 1号 空间专用的密钥！ 假设是 1779931471 (可能会有变化)
        final int i = adapter.setSpaceSk("1");

        // 在下面继续从 adapter 获取到 1 号空间的文件列表 TODO 这里会出现问题 因为 1 号空间对应的密钥被修改为了 i 的值
        final DiskMirrorRequest urls = DiskMirrorRequest.getUrls(1, Type.Binary);
        urls.setSk(123456);
        final JSONObject urls1 = adapter.getUrls(urls);
        System.out.println(urls1);

        // 所以我们需要使用 i 做为sk
        urls.setSk(i);
        final JSONObject urls2 = adapter.getUrls(urls);
        System.out.println(urls2);

        // 然而 如果是 其它空间 则是继续使用 123456 做为密钥
        // 简单来说就是 通过 adapter.setSpaceSk 方法设置过密钥，就需要使用新密钥，反之则是使用 Config.secureKey 做为密钥
        urls.setUserId(2);
        urls.setSk(123456);
        final JSONObject urls3 = adapter.getUrls(urls);
        System.out.println(urls3);
    }
}