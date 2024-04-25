package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;
import top.lingyuzhao.diskMirror.utils.ProgressBar;

import java.io.IOException;

@DiskMirrorConfig(
        rootDir = "/DiskMirror/"
)
public final class MAIN2 {
    public static void main(String[] args) throws IOException {
        // 准备参数 把 url 放到参数中
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "https://s01.oss.sonatype.org/content/repositories/releases/io/github/BeardedManZhao/diskMirror/1.2.1/diskMirror-1.2.1-javadoc.jar");
        jsonObject.put("fileName", "diskMirror-1.2.1-javadoc.jar");
        jsonObject.put("userId", 1);
        jsonObject.put("type", Type.Binary);
        // 准备适配器对象
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN2.class);

        // 使用一个线程进行转存记录的查看 因为我们要测试查看线程转存状态的小玩意，因此在这里就需要保持转存的同时调用 transferDepositStatus
        new Thread(() -> {
            try {
                // 使用这个 确保转存操作已开始
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            show(adapter, jsonObject);
        }).start();

        // 开始进行转存
        System.out.println("开始转存！");
        final JSONObject jsonObject1 = adapter.transferDeposit(jsonObject);
        // 打印正在上传的文件的数据
        System.out.println("转存完毕！" + jsonObject1);
        // 转存完毕再打印一下看看
        show(adapter, jsonObject);
    }

    public static void show(Adapter adapter, JSONObject jsonObject) {
        adapter.getAllProgressBar(jsonObject.getString("userId")).forEach((k, v) -> {
            final ProgressBar v1 = (ProgressBar) v;
            System.out.println("正在保存的文件：" + k + "\t文件目前保存的字节数：" + v1.getCount() + "\t文件总大小：" + v1.getMaxCount() + "\t文件保存进度：" + v1.getCount() / v1.getMaxCount() * 100 + "%");
        });
    }
}