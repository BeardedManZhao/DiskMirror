import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.TcpAdapter;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        fsDefaultFS = "10001,10002"
)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        TcpAdapter adapter = (TcpAdapter) DiskMirror.TCP_Adapter.getAdapterPacking(DiskMirror.LocalFSAdapter, MAIN2.class, new Config());
        System.out.println(adapter.version());
        adapter.run();
        adapter.run();
        adapter.run();
        adapter.run();
        adapter.run();
        adapter.close();
    }
}