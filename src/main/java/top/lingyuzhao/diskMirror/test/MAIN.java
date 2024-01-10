package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(new Config());
        // 获取到 1024 空间中的所有文件的url 首先准备参数
        final JSONObject jsonObject = new JSONObject();
        // 设置文件所属空间id
        jsonObject.put("userId", 1024);
        // 设置文件类型 根据自己的文件类型选择不同的类型
        jsonObject.put("type", Type.Binary);
        // 打印此空间中的所有文件 其中会包含一个 maxSize 参数 由于我们没有设置，所以这里是默认的
        System.out.println(adapter.getUrls(jsonObject.clone()));
        // 设置 1024 空间的最大空间大小
        adapter.setSpaceMaxSize("1024", 999999999);
        // 再一次打印 TODO 其中的 maxSize 已经被修改为 999999999 在校验的时候也会按照这个参数进行校验
        System.out.println(adapter.getUrls(jsonObject.clone()));
    }
}