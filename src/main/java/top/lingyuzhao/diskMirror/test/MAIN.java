package top.lingyuzhao.diskMirror.test;

import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.TcpAdapter;

import java.io.IOException;

public final class MAIN {
    public static void main(String[] args) throws IOException {
        System.out.println(DiskMirror.VERSION);
        // 使用适配器对象 用来接收远程数据并进行处理
        final TcpAdapter adapterPacking0 = (TcpAdapter) DiskMirror.TCP_Adapter.getAdapterPacking(
                // 指定此适配器内部要使用的适配器的类型
                DiskMirror.LocalFSAdapter,
                // 指定此适配器的配置
                ConfigTCPAdapter1.class,
                // 指定此适配器的子适配的配置
                ConfigAdapter.class
        );

        // 开始监听！ 这里代表的是监听三次 实际的项目中 您也可以使用循环逻辑实现监听
        for (int i = 0; i < 3; i++) {
            // run 函数会阻塞线程进行监听！
            adapterPacking0.run();
        }
    }

    // TCP 适配器1的配置
    @DiskMirrorConfig(
            // 设置 TCP 适配器服务打开的端口 分别是 元数据端口 文件传输端口
            fsDefaultFS = "10001,10002"
    )
    public static final class ConfigTCPAdapter1 {
    }

    // TCP 适配器中所包含的子适配器的配置，TCP适配器实现了包装器 因此可以将任意一种适配器接入到TCP的数据传输中
    // 这里就是子适配器的配置
    @DiskMirrorConfig()
    public static final class ConfigAdapter {
    }
}