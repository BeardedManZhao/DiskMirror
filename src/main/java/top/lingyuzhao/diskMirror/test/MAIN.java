package top.lingyuzhao.diskMirror.test;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.DiskMirrorConfig;
import top.lingyuzhao.diskMirror.core.Adapter;
import top.lingyuzhao.diskMirror.core.DiskMirror;
import top.lingyuzhao.diskMirror.core.Type;

import java.io.IOException;

/**
 * @author zhao
 */
@DiskMirrorConfig(
        //  TODO 设置 路径的协议前缀 在这里指向的是 HDFS 文件系统 盘镜目录 访问地址
        protocolPrefix = "http://192.168.0.141:9870/webhdfs/v1/DiskMirror",
        // TODO 设置 HDFS 服务器地址 在这里指向的是 HDFS 文件系统访问地址 是 hdfs 开头
        fsDefaultFS = "hdfs://192.168.0.141:8020",
        // TODO 设置请求参数 HDFS 要求需要设置 OP 参数 在读取返回 url 的时候会使用这个参数进行拼接
        params = "{\"op\": \"OPEN\"}"
)
public final class MAIN {
    public static void main(String[] args) throws IOException {
        // 获取到本地文件系统适配器
        final Adapter adapter = DiskMirror.HDFSAdapter.getAdapter(MAIN.class);
        // 准备参数
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1024);
        jsonObject.put("type", Type.Binary);
        // 设置要创建的目录
        jsonObject.put("fileName", "MyDir");
        // 直接创建
        // {"userId":1024,"type":"Binary","fileName":"MyDir","useSize":0,"res":"ok!!!!"}
        System.out.println(adapter.mkdirs(jsonObject));
        // 查看文件系统结构 这里由于只需要 userId type 而恰巧在上面我们设置好了 所以直接将上面的json 输入
        // {"userId":1024,"type":"Binary","fileName":"MyDir","useSize":0,"res":"ok!!!!","useAgreement":true,"maxSize":134217728,"urls":[{"fileName":"MyDir","url":"http://localhost:8080/1024/Binary//MyDir","lastModified":1706779978911,"size":0,"type":"Binary","isDir":true,"urls":[]}]}
        System.out.println(adapter.getUrls(jsonObject));
    }
}