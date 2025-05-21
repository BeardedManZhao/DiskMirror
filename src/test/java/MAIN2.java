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

)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN2.class);
        JSONObject urls = adapter.getUrlsNoRecursion(DiskMirrorRequest.getUrlsNoRecursion(1024, Type.Binary, "/test"));
        System.out.println(urls);
    }
}