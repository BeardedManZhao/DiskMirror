import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
public class MAIN {
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        config.put(Config.ROOT_DIR, "/DiskMirror/data");
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(config);
        try (InputStream inputStream = adapter.downLoad(
                DiskMirrorRequest.uploadRemove(1, Type.Binary, "diskMirror-backEnd-spring-boot.yaml")
        )) {
            System.out.println(IOUtils.getStringByStream(inputStream));
        }

    }
}