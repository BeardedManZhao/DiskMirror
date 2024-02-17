package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 将 1024 号空间的最大容量修改为 256MB
        adapter.setSpaceMaxSize("1024", 256 << 10 << 10);
        // 查看 1 号空间 和 1024 号空间的最大容量
        System.out.println(adapter.getSpaceMaxSize("1"));
        System.out.println(adapter.getSpaceMaxSize("1024"));
    }
}