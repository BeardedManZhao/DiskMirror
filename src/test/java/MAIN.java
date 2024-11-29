import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(fsDefaultFS = "https://diskMirror.lingyuzhao.top/DiskMirrorBackEnd")
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final String version = DiskMirror.DiskMirrorHttpAdapter.getVersion();
        System.out.println(version);
        final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        System.out.println(adapter.version());
    }
}