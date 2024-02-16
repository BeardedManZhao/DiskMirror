package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        final JSONObject text = adapter.writer(
                "这是一个即将会被写进文件中的字符串！！！！",
                // 设置在 diskMirror 空间中用于存储字符串的文件名字
                "test.txt",
                // 设置用于存储数据的 diskMirror 空间的id
                1,
                // 设置文件类型
                "text",
                // 设置需要使用的安全密钥 由于我们没有设置密钥 所以写为 空
                0
        );
        System.out.println(text);
    }
}