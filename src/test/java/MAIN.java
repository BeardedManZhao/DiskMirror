import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 设置默认的空间大小，这个空间设置会作用在所有的空间中
        userDiskMirrorSpaceQuota = 1024
)
public final class MAIN {
    public static void main(String[] args) {
        try (final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class)) {
            // 直接获取就是默认的配置
            long spaceMaxSize = adapter.getSpaceMaxSize("1");
            System.out.println(spaceMaxSize);

            // 我们可以为某个空间单独设置这个最大使用量
            adapter.setSpaceMaxSize("1", 1024 * 1024 * 1024);
            // 再获取就是1024 * 1024 * 1024
            spaceMaxSize = adapter.getSpaceMaxSize("1");
            System.out.println(spaceMaxSize);

            System.out.println(adapter.getSpaceMaxSize("2"));
        }
    }
}