package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;
import top.lingyuzhao.utils.CacheUtils;

/**
 * ddos 验证
 *
 * @author 赵凌宇
 */
public class DDosCheckModule extends Verification {

    private final CacheUtils cacheUtils;
    private static final CheckException userIdIsNull = new CheckException("userId is null");
    private static final CheckException ddos = new CheckException("您对本空间的操作过于频繁，请稍后再试!");

    private final int maxCount;

    /**
     * 构造函数
     *
     * @param moduleName    模块名称
     * @param message       模块的描述
     * @param windowsTimeMs 窗口时间 也就是被检测的时间
     * @param maxCount      检测的次数，在指定的时间内，如果超过这个次数，就会抛出异常
     */
    public DDosCheckModule(String moduleName, String message, long windowsTimeMs, int maxCount) {
        super(moduleName, message);
        this.cacheUtils = new CacheUtils("ddos", windowsTimeMs);
        this.maxCount = maxCount;
    }

    @Override
    public void checkFun(Config config, JSONObject jsonObject) throws CheckException {
        final String string = jsonObject.getString("userId");
        if (string == null) {
            throw userIdIsNull;
        }
        final Object o = cacheUtils.get(string);
        if (o == null) {
            cacheUtils.put(string, 1);
        } else {
            final int i = (int) o;
            if (i > maxCount) {
                throw ddos;
            }
            cacheUtils.put(string, i + 1);
        }
    }
}
