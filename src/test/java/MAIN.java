import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(fsDefaultFS = "casaos703.lingyuzhao.top:10001,10002")
public class MAIN {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.TCP_CLIENT_Adapter.getAdapter(MAIN.class);
        JSONObject urls = adapter.getUrls(DiskMirrorRequest.getUrls(10000002, Type.Binary));
        System.out.println(urls);

        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Pictures\\Screenshots\\屏幕截图 2024-12-15 183521.png")) {
            adapter.upload(fileInputStream, DiskMirrorRequest.uploadRemove(10000002, Type.Binary, "/message/welcome2.png"));
        }
    }
}