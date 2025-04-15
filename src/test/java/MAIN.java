import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        fsDefaultFS = "127.0.0.1:10001,10002"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.TCP_CLIENT_Adapter.getAdapter(MAIN.class);
        JSONObject urlsNoRecursion = adapter.getUrlsNoRecursion(
                DiskMirrorRequest.getUrlsNoRecursion(1, Type.Binary, "/")
        );
        System.out.println(urlsNoRecursion);
    }
}