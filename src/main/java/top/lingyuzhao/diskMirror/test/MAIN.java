package top.lingyuzhao.diskMirror.test;
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.utils.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        rootDir = "/diskMirror"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到 diskMirror 适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备我们需要的文件的描述信息
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 设置要获取的文件的文件名字
        jsonObject.put("fileName", "test/arc122.png");
        // 获取到数据流对象
        try(
                final InputStream inputStream = adapter.downLoad(jsonObject);
                final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\zhao\\Desktop\\fsdownload\\res.png")
        ){
            // 在这里可以使用数据流 数据流中就是我们需要的文件！
            IOUtils.copy(inputStream, fileOutputStream, true);
        }
    }
}