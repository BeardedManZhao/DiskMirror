package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.utils.PathGeneration;


/**
 * 配置类
 *
 * @author zhao
 */
public final class Config extends JSONObject {
    /**
     * 用于文件存储的路径 配置项的名称
     */
    public final static String ROOT_DIR = "root.dir";

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
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public Config() {
        super();
        super.put(ROOT_DIR, "/DiskMirror");
        super.put(OK_VALUE, "ok!!!!");
        super.put(RES_KEY, "res");
        super.put(PROTOCOL_PREFIX, "http://localhost:8080");
        final String rootDir = (String) super.get(ROOT_DIR);
        // 默认的路径生成逻辑  由 <空间id，文件名称> 生成 文件路径
        super.put(GENERATION_RULES, (PathGeneration) jsonObject -> {
            final int userId = jsonObject.getIntValue("userId");
            final String type = jsonObject.get("type").toString();
            final String fileName = jsonObject.getString("fileName");
            if (fileName != null) {
                return rootDir + '/' + userId + '/' + type + '/' + fileName;
            } else {
                return rootDir + '/' + userId + '/' + type + '/';
            }
        });

    }

}
