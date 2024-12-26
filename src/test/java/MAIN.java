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
@DiskMirrorConfig(isNotOverWrite = false)
public class MAIN {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        JSONObject urls = adapter.getUrls(DiskMirrorRequest.getUrls(1, Type.Binary));
        System.out.println(urls);

        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\14.png")) {
            adapter.upload(fileInputStream, DiskMirrorRequest.uploadRemove(1, Type.Binary, "/message/welcome2.png"));
        }

        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\15.png")) {
            adapter.upload(fileInputStream, DiskMirrorRequest.uploadRemove(1, Type.Binary, "/message/welcome2.png"));
        }
    }
}