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
        // 设置默认的空间大小，这个空间设置会作用在所有的空间中
        userDiskMirrorSpaceQuota = 1024,
        // 设置存储空间配置使用redis存储（如 空间的大小，空间的sk）
        useSpaceConfigMode = "JedisMapper",
        // 设置redis的配置
        redisHostPortDB = "redis.lingyuzhao.top:46379:0",
        redisPassword = "38243824"
)
public final class MAIN {
    public static void main(String[] args) {
        try (final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class)) {
            JSONObject urls = adapter.getUrls(DiskMirrorRequest.getUrls(1, Type.Binary));
            System.out.println(urls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}