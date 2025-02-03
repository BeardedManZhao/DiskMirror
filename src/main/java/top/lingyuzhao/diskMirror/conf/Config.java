package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.utils.PathGeneration;

import java.util.HashMap;


/**
 * 盘镜配置类，用于存储配置信息，此配置类是通过JSON对象实现的，因此可以使用JSON对象的API进行操作。
 * <p>
 * The disk mirror configuration class is used to store configuration information. This configuration class is implemented through JSON objects, so it can be operated using the API of JSON objects.
 *
 * @author zhao
 */
public class Config extends JSONObject {

    /**
     * 用于文件存储的路径 配置项的名称
     */
    public final static String ROOT_DIR = "root.dir";
    /**
     * HDFS  主节点链接路径配置名称
     */
    public static final String FS_DEFAULT_FS = "fs.defaultFS";
    /**
     * 用于文件路径生成 的配置项的名称
     */
    public final static String GENERATION_RULES = "generation.rules";
    /**
     * 操作正确之后要返回的值
     */
    public final static String OK_VALUE = "ok.value";
    /**
     * 操作结果对应的 key
     */
    public final static String RES_KEY = "resK";
    /**
     * 协议前缀
     */
    public final static String PROTOCOL_PREFIX = "protocol.prefix";
    /**
     * 请求的url中的参数, 当读取请求时，如果您有一些参数的需求，可以通过此配置设置一个 json 对象，如：{"op":"OPEN"}
     */
    public final static String PARAMS = "params";
    /**
     * 用户 盘镜 空间配额，每个用户只能使用固定容量的盘镜配额空间！ 这里是以字节为单位的数值 默认值为 128MB
     */
    public final static String USER_DISK_MIRROR_SPACE_QUOTA = "user.disk.mirror.space.quota";
    /**
     * 盘镜服务中的安全密钥配置，此密钥对应的如果是字符串，则会转换为 hash 值 如果是数值 则会被直接做为密钥
     * 设置了密钥之后，则在访问盘镜服务时，需要在请求的数据包中添加（secure.key, xxx）
     */
    public final static String SECURE_KEY = "secure.key";
    /**
     * 盘镜 服务中 所有与字符编码相关的操作，要使用的字符编码集。
     * <p>
     * The character encoding set to be used for all operations related to character encoding in the disk mirror service.
     */
    public final static String CHAR_SET = "diskMirror.charset";
    /**
     * 是否不允许覆盖写入
     */
    public final static String IS_NOT_OVER_WRITE = "is.not.over.write";

    private final SpaceConfig spaceConfig = new SpaceConfig(this);

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public Config() {
        super();
        super.put(ROOT_DIR, "/DiskMirror");
        super.put(FS_DEFAULT_FS, "hdfs://127.0.0.1:8020");
        super.put(OK_VALUE, "ok!!!!");
        super.put(RES_KEY, "res");
        super.put(PROTOCOL_PREFIX, "http://localhost:8080");
        super.put(USER_DISK_MIRROR_SPACE_QUOTA, 128 << 10 << 10);
        super.putObject(PARAMS);
        super.put(SECURE_KEY, 0);
        super.put(CHAR_SET, "UTF-8");
        super.put(IS_NOT_OVER_WRITE, true);
        // 默认的路径生成逻辑  由 <空间id，文件名称> 生成 文件路径
        super.put(GENERATION_RULES, getPathGeneration());
    }

    /**
     * 通过配置类的注解构造一个配置对象
     *
     * @param config 配置注解对象
     */
    public Config(DiskMirrorConfig config) {
        super();
        super.put(ROOT_DIR, config.rootDir());
        super.put(FS_DEFAULT_FS, config.fsDefaultFS());
        super.put(OK_VALUE, config.okValue());
        super.put(RES_KEY, config.resKey());
        super.put(PROTOCOL_PREFIX, config.protocolPrefix());
        super.put(USER_DISK_MIRROR_SPACE_QUOTA, config.userDiskMirrorSpaceQuota());
        super.put(PARAMS, JSONObject.parse(config.params()));
        super.put(SECURE_KEY, config.secureKey());
        super.put(CHAR_SET, config.charSet());
        super.put(IS_NOT_OVER_WRITE, config.isNotOverWrite());
        // 默认的路径生成逻辑  由 <空间id，文件名称> 生成 文件路径
        super.put(GENERATION_RULES, getPathGeneration());
    }

    public Config(HashMap<String, Object> hashMap) {
        super(hashMap);
    }

    /**
     * 获取路径生成逻辑实现函数
     *
     * @return 路径生成逻辑实现函数 函数处理之后的返回结果应为：[空间路径（无协议）, 空间路径（有协议）, 文件路径（无协议）, 文件路径（有协议）]
     */
    protected PathGeneration getPathGeneration() {
        final String protocol = this.getString(PROTOCOL_PREFIX);
        final String rootDir = (String) this.get(ROOT_DIR);
        return jsonObject -> {
            final int userId = jsonObject.getIntValue("userId");
            final String type = jsonObject.get("type").toString();
            final String fileName = jsonObject.getString("fileName");
            // 确保文件名不为空
            final String fn = fileName != null ? fileName : "未命名_" + System.currentTimeMillis();
            boolean isRead = (boolean) jsonObject.getOrDefault("useAgreement", true);
            // 生产参数
            final StringBuilder stringBuilder = new StringBuilder();
            if (isRead) {
                this.getJSONObject(PARAMS).forEach((k, v) -> stringBuilder.append(k).append("=").append(v).append("&"));
            }
            // 开始构建[空间路径（无协议）, 空间路径（有协议）, 文件路径（无协议）, 文件路径（有协议）]
            final String s3 = '/' + String.valueOf(userId) + '/' + type + '/';
            final String
                    s1 = protocol + s3,
                    s2 = rootDir + s3;
            // 生成参数字符串
            if (stringBuilder.length() > 0) {
                final String s = "?" + stringBuilder;
                return new String[]{
                        s2 + s,
                        s1 + s,
                        s2 + fn,
                        s1 + fn + s,
                };
            } else {
                // s 是空 不需要!
                return new String[]{
                        s2,
                        s1,
                        s2 + fn,
                        s1 + fn,
                };
            }
        };
    }

    @Override
    public Object put(String key, Object value) {
        Object put = super.put(key, value);
        switch (key) {
            case ROOT_DIR:
            case PROTOCOL_PREFIX:
                // 这个情况需要刷新 path 生成器
                super.put(GENERATION_RULES, getPathGeneration());
            default:
                // 继续返回结果即可
                return put;
        }
    }

    /**
     * 获取到空间配置对象，此空间配置对象 是可以针对某一个空间来进行配置的，它配置的优先级更高！
     *
     * @return 获取到空间配置对象
     */
    public SpaceConfig getSpaceConfig() {
        return spaceConfig;
    }

    /**
     * 获取到指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @return 指定空间的最大使用量 字节数
     */
    public long getSpaceMaxSize(String spaceId) {
        return (long) this.spaceConfig.getSpaceConfigByKey(spaceId, Config.USER_DISK_MIRROR_SPACE_QUOTA);
    }

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     */
    public void setSpaceMaxSize(String spaceId, long maxSize) {
        this.spaceConfig.setSpaceConfigByKey(spaceId, Config.USER_DISK_MIRROR_SPACE_QUOTA, maxSize);
    }

    /**
     * @return 获取到安全密钥 您可以在这里查询到密钥对应的数值，因为 setSecureKey 函数中的形参如果是字符串，则会转换为 hash 值 如果是数值 则会被直接做为密钥，所以需要这个函数获取到最终的密钥结果，这个密钥结果需要被提供给函数。
     * <p>
     * To obtain the security key, you can search for the corresponding numerical value here. If the formal parameter in the setSecureKey function is a string, it will be converted to a hash value. If it is a numerical value, it will be directly used as the key. Therefore, this function needs to obtain the final key result, which needs to be provided to the function
     */
    public int getSecureKey() {
        return (int) this.get(SECURE_KEY);
    }

    /**
     * 设置安全密钥
     *
     * @param k 密钥 可以是字符传 也可以是数值
     */
    public void setSecureKey(Object k) {
        if (k instanceof String) {
            k = k.hashCode();
        }
        if (k instanceof Integer) {
            this.put(SECURE_KEY, k);
        } else {
            throw new UnsupportedOperationException("您在 setSecureKey 设置密钥的时候，参数只能是字符串或数值。\nWhen setting the key in setSecureKey, the parameters can only be strings or numerical values.\nerror type = " + k.getClass().getName());
        }
    }
}
