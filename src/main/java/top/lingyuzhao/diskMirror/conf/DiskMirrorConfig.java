package top.lingyuzhao.diskMirror.conf;

import java.lang.annotation.*;

/**
 * 盘镜 配置注解类，针对一个类使用该注解，可以将一个类的类对象做为注解类进行传递，您只需要在注解类中配置相关的属性即可，大大的降低了使用配置类的繁琐程度。
 * <p>
 * Disk Mirror Configuration Annotation Class. When using this annotation for a class, the class object of a class can be passed as an annotation class. You only need to configure the relevant properties in the annotation class, greatly reducing the complexity of using the configuration class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DiskMirrorConfig {

    /**
     * @return 用于文件存储的路径 配置项
     * <p>
     * Path configuration item for file storage
     */
    String rootDir() default "/DiskMirror";

    /**
     * @return HDFS  主节点链接路径配置
     * <p>
     * HDFS Master Node Link Path Configuration
     */
    String fsDefaultFS() default "hdfs://127.0.0.1:8020";

    /**
     * @return 操作正确之后要返回的值
     * <p>
     * Value to be returned after correct operation
     */
    String okValue() default "ok!!!!";

    /**
     * @return 盘镜服务中的安全密钥配置，此密钥对应的如果是字符串，则会转换为 hash 值 如果是数值 则会被直接做为密钥
     * 设置了密钥之后，则在访问盘镜服务时，需要在请求的数据包中添加（secure.key, xxx）
     * <p>
     * The security key configuration in the disk mirror service, if this key corresponds to a string, it will be converted to a hash value. If it is a numerical value, it will be directly used as the key
     * After setting the key, when accessing the disk mirror service, it is necessary to add (secure. key, xxx) to the requested data packet
     */
    int secureKey() default 0;

    /**
     * @return 操作结果对应的 key
     * <p>
     * The key corresponding to the operation result
     */
    String resKey() default "res";

    /**
     * @return 协议前缀 protocolPrefix
     */
    String protocolPrefix() default "http://localhost:8080";

    /**
     * @return 请求中的 url 中的参数的名字，当读取请求时，如果您有一些参数的需求，可以在这里进行添加，这应该是一个 json 字符串
     * <p>
     * The name of the parameter in the URL of the request. When reading the request, if you have some parameter requirements, you can add them here. This should be a JSON string
     */
    String params() default "{}";

    /**
     * @return 用户 盘镜 空间配额，每个用户只能使用固定容量的盘镜配额空间！ 这里是以字节为单位的数值 默认值为 128MB
     * <p>
     * User disk mirror space quota, each user can only use a fixed capacity disk mirror quota space! Here is a numerical value in bytes, with a default value of 128MB
     */
    long userDiskMirrorSpaceQuota() default 128 << 10 << 10;

    /**
     * @return 盘镜 服务中 所有与字符编码相关的操作，要使用的字符编码集。
     * <p>
     * The character encoding set to be used for all operations related to character encoding in the disk mirror service.
     */
    String charSet() default "UTF-8";
}
