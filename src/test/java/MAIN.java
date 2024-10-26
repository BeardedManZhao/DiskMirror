import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.core.module.HandleModule;
import top.lingyuzhao.utils.PalettePng;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 修改压缩模式，这里我们使用的是 png RGB 8位 压缩 如果期望使用其他压缩模式可以自行修改
        HandleModule.ImageCompressModule.setImageCompress(PalettePng.RGB_8);
        // 将压缩处理器装到适配器中
        adapter.addHandleModule(HandleModule.ImageCompressModule);
        // 上传一个 png 图片~
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\图片2.png")) {
            final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.uploadRemove(
                    // 在这里的 fileName 如果是以 png 等格式结尾的话，则会经由 ImageCompressModule 进行压缩处理
                    1, Type.Binary, "图片21.png"
            );
            System.out.println(
                    adapter.upload(fileInputStream, diskMirrorRequest)
            );
        }

        // 移除模块之后 将不会再压缩
        adapter.deleteHandleModule(HandleModule.ImageCompressModule);
        // 上传一个 png 图片~
        try (final FileInputStream fileInputStream = new FileInputStream("C:\\Users\\zhao\\Downloads\\图片2.png")) {
            final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.uploadRemove(
                    // 这里的 fileName 即使是 png 也不会被压缩 因为压缩模块被移除了
                    1, Type.Binary, "图片211.png"
            );
            System.out.println(
                    adapter.upload(fileInputStream, diskMirrorRequest)
            );
        }
    }
}