import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.filter.FileMatchManager;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        fsDefaultFS = "https://diskmirror.lingyuzhao.top/DiskMirrorBackEnd"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        JSONObject remove = adapter.remove(
                DiskMirrorRequest.uploadRemove(4, Type.Binary, "/chatFiles/root_1")
                        .setFilter(FileMatchManager.FILE_TIME_MATCH, "1739549276103")
                        .setSk(561089172)
        );
        JSONObject remove1 = adapter.remove(remove);
        System.out.println(remove1);
    }
}