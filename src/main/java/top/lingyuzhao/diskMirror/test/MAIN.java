package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        // 配置根目录 也是能够被盘镜 管理的目录，所有的管理操作只会在这个目录中生效，默认是/DiskMirror!
        rootDir = "/DiskMirror",
        // 配置所有的 url 中的协议前缀，这会影响 getUrls 的结果， 如果您只是在本地文件系统中获取这些数据 就是文件系统的协议前缀，也就是什么都不加
        // 如果您要在 hdfs 文件系统中获取这些数据 这就是 hdfs 的协议前缀
        // 如果您要在 web JS 或者通过 url 中获取这些数据 这就是 web 的 http 协议前缀
        // 在这里我们给定空字符串就是代表使用本地文件系统
        protocolPrefix = ""
)
public final class MAIN {
    public static void main(String[] args) {
        // 开始构建盘镜 由于我们在这里使用的是本地文件系统 所以我们使用 DiskMirror.LocalFSAdapter.getAdapter(被注解的类对象) 来实例化 本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
    }
}