package top.lingyuzhao.diskMirror.conf;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DiskMirrorConfig {
    /**
     * @return 用于文件存储的路径 配置项
     */
    String rootDir() default "/DiskMirror";

    /**
     * @return HDFS  主节点链接路径配置
     */
    String fsDefaultFS() default "hdfs://127.0.0.1:8020";

    /**
     * 操作正确之后要返回的值
     */
    String okValue() default "ok!!!!";

    /**
     * @return 盘镜服务中的安全密钥配置，此密钥对应的如果是字符串，则会转换为 hash 值 如果是数值 则会被直接做为密钥
     * 设置了密钥之后，则在访问盘镜服务时，需要在请求的数据包中添加（secure.key, xxx）
     */
    int secureKey() default 0;

    /**
     * @return 操作结果对应的 key
     */
    String resKey() default "res";

    /**
     * @return 协议前缀
     */
    String protocolPrefix() default "http://localhost:8080";

    /**
     * @return 请求中的 url 中的参数的名字，当读取请求时，如果您有一些参数的需求，可以在这里进行添加，这应该是一个 json 字符串
     */
    String params() default "{}";

    /**
     * @return 用户 盘镜 空间配额，每个用户只能使用固定容量的盘镜配额空间！ 这里是以字节为单位的数值 默认值为 128MB
     */
    long userDiskMirrorSpaceQuota() default 128 << 10 << 10;
}
