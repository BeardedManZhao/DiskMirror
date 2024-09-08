import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorHttpAdapter;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        fsDefaultFS = "http://localhost:8080"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        DiskMirrorHttpAdapter adapter = (DiskMirrorHttpAdapter) DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        DiskMirrorRequest urls = DiskMirrorRequest.getUrls(1, Type.Binary);
        JSONObject urls1 = adapter.getUrls(urls);
        System.out.println(urls1);
    }
}