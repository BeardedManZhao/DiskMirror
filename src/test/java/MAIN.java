import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

@DiskMirrorConfig()
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.LocalFSAdapter.getAdapter(MAIN.class);
        // 准备参数
        DiskMirrorRequest myDir = DiskMirrorRequest.mkdirs(1024, Type.Binary, "MyDir");
        // 直接创建
        // 结果 {"userId":1024,"type":"Binary","dirName":"MyDir","fileName":"MyDir","useSize":20061,"res":"ok!!!!"}
        System.out.println(adapter.mkdirs(myDir));
        // 查看文件系统结构 这里由于只需要 userId type 而恰巧在上面我们设置好了 所以直接将上面的json 输入
        // 是的哦 可以复用请求！！！只要必要的参数存在就可以 如果您不确定请求是否可用 也可以使用 DiskMirrorRequest.getUrls(1024, Type.Binary)
        // 结果 {"userId":1024,"type":"Binary","dirName":"MyDir","fileName":"MyDir","useSize":20061,"res":"ok!!!!","useAgreement":true,"maxSize":134217728,"urls":[]}
        System.out.println(adapter.getUrlsNoRecursion(myDir));
    }
}