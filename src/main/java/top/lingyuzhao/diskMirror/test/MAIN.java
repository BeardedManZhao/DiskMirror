package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // TODO 设置 DiskMirror 远程 后端 服务器地址 这个是需要搭建的哦!!!
        // 具体的后端服务器搭建 可以阅读：https://github.com/BeardedManZhao/DiskMirrorBackEnd.git
        fsDefaultFS = "https://diskMirror.lingyuzhao.top/DiskMirrorBackEnd",
        secureKey = 2123691651
)
public final class MAIN {
    public static void main(String[] args) {
        // 获取到 远程 diskMirror 适配器
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        // 获取到 远程服务器版本
        System.out.println(adapter.version());
        // 查看 1 25 的maxSize
        System.out.println(adapter.getSpaceMaxSize("1"));
        System.out.println(adapter.getSpaceMaxSize("25"));
    }
}