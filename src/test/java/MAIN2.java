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
        fsDefaultFS = "https://diskmirror.lingyuzhao.top/DiskMirrorBackEnd"
)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(MAIN2.class);
        try (
                // 下载 文件 并且设定 下载的区间 单位 Byte
                InputStream inputStream = adapter.downLoad(DiskMirrorRequest.download(-999999999, Type.Binary, "/share/6af46c0b626935d9af41f90c5e5825aefb0ddc980e11013f308ac71201ea8dac.mp4"), "0", "4000");
                FileOutputStream fileOutputStream = new FileOutputStream("D:/6af46c0b626935d9af41f90c5e5825aefb0ddc980e11013f308ac71201ea8dac.mp4")
        ) {
            IOUtils.copy(inputStream, fileOutputStream, false);
        }
    }
}