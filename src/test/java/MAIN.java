import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.module.DDosCheckModule;
import top.lingyuzhao.diskMirror.core.module.ModuleManager;

import java.io.IOException;

@DiskMirrorConfig
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 注册一个 ddos 验证器 防御写操作的ddos 这里设置的是 1000 毫秒内不能超过5次请求
        // 由于注册到了读操作 因此是 1000 毫秒内不能超过5次读取请求
        ModuleManager.registerModuleRead(new DDosCheckModule("ddos", "一个用于防ddos的模块", 1000, 5));
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        System.out.println(adapter.version());
        for (int i = 0; i < 20; i++) {
            System.out.println(adapter.getUrls(DiskMirrorRequest.getUrls(47, Type.Binary)));
        }
    }
}