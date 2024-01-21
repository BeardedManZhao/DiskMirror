package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.utils.PathGeneration;


/**
 * 盘镜配置类，用于存储配置信息，此配置类是通过JSON对象实现的，因此可以使用JSON对象的API进行操作。
 *
 *The disk mirror configuration class is used to store configuration information. This configuration class is implemented through JSON objects, so it can be operated using the API of JSON objects.
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
     * 请求的url中的参数
     */
    public final static String PARAMS = "params";

    /**
     * 用户 盘镜 空间配额，每个用户只能使用固定容量的盘镜配额空间！ 这里是以字节为单位的数值 默认值为 128MB
     */
    public final static String USER_DISK_MIRROR_SPACE_QUOTA = "user.disk.mirror.space.quota";

    /**
     * 用户 盘镜 空间配合映射表，通过此处的映射操作可以获取到指定用户的空间的使用量最大值。
     */
    private final static JSONObject SPACE_SIZE = new JSONObject();

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
        // 默认的路径生成逻辑  由 <空间id，文件名称> 生成 文件路径
        super.put(GENERATION_RULES, (PathGeneration) jsonObject -> {
            final int userId = jsonObject.getIntValue("userId");
            final String type = jsonObject.get("type").toString();
            final String fileName = jsonObject.getString("fileName");
            Object isRead = jsonObject.get("useAgreement");
            if (isRead == null) {
                isRead = false;
            }
            // 如果是读取 同时 具有前部协议 则 在这里去掉 路径前缀 使用 协议前缀替代 反之加上路径前缀
            final String rootDir = (((boolean) isRead) && super.getString(PROTOCOL_PREFIX).length() != 0) ? "" : (String) super.get(ROOT_DIR);
            // 生成参数
            final StringBuilder stringBuilder = new StringBuilder();
            super.getJSONObject(PARAMS).forEach((k, v) -> stringBuilder.append(k).append("=").append(v).append("&"));
            if (fileName != null) {
                return rootDir + '/' + userId + '/' + type + '/' + fileName + (stringBuilder.length() == 0 ? "" : "?" + stringBuilder);
            } else {
                return rootDir + '/' + userId + '/' + type + '/' + (stringBuilder.length() == 0 ? "" : "?" + stringBuilder);
            }
        });
    }

    /**
     * 获取到指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @return 指定空间的最大使用量 字节数
     */
    public long getSpaceMaxSize(String spaceId) {
        return SPACE_SIZE.getLongValue(spaceId, this.getLongValue(USER_DISK_MIRROR_SPACE_QUOTA));
    }

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     */
    public void setSpaceMaxSize(String spaceId, long maxSize) {
        SPACE_SIZE.put(spaceId, maxSize);
    }
}
