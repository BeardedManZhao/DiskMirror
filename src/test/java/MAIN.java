import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        fsDefaultFS = "https://diskmirror.lingyuzhao.top/DiskMirrorBackEnd",
        // 设置下载时 要使用的字符集 用于进行 url 编码等操作！
        charSet = "UTF-8"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN.class);
        try (
                final InputStream inputStream = adapter.downLoad(
                        DiskMirrorRequest.uploadRemove(-999999999, Type.Binary, "/111.31.150.102/share/2025_01_05/12月7日.mp4")
                );
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\e.mp4")
        ) {
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
}