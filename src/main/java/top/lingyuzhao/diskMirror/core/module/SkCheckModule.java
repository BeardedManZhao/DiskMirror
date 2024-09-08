package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;

import java.util.HashMap;
import java.util.Random;

/**
 * 安全密钥检查组件！
 * <p>
 * Security key check component!
 *
 * @author zhao
 */
public class SkCheckModule implements VerificationModule {

    /**
     * id 与 安全密钥 存储容器
     */
    private static final HashMap<String, Integer> USER_ID_SK = new HashMap<>();

    private static final Random RANDOM = new Random();

    /**
     * 设置id 与 安全密钥
     *
     * @param userId 被设置的空间 id
     * @return 设置成功之后的结果！
     */
    public static int setUserSk(String userId) {
        if (userId == null) {
            throw new NullPointerException("userId cannot be null");
        }
        final int i = RANDOM.nextInt(Integer.MAX_VALUE);
        USER_ID_SK.put(userId, i);
        return i;
    }

    /**
     * 获取用户id 与 安全密钥
     *
     * @param userId 被设置的空间 id
     * @param config 默认配置对象 当 userId 没有在 USER_ID_SK 中设置过的时候，需要从其中取出 默认的 sk 参数
     * @return 操作结果
     */
    public static int getUserSk(String userId, Config config) {
        final Integer i = USER_ID_SK.get(userId);
        if (i == null) {
            return config.getSecureKey();
        } else {
            return i;
        }
    }

    /**
     * 检查函数 此函数会处于所有服务函数的第一行
     *
     * @param config  需要使用的适配器的配置类对象
     * @param spaceId 需要被检查的 spaceId
     * @param sk      需要被检查的 sk 参数
     */
    public static void checkSK(Config config, String spaceId, int sk) {
        if (getUserSk(spaceId, config) != sk) {
            StringBuilder showData = new StringBuilder(String.valueOf(sk));
            final int length = showData.length();
            for (int i = 0; i < length; i++) {
                showData.setCharAt(i, i - (i >> 1 << 1) == 0 ? showData.charAt(i) : '*');
            }
            throw new UnsupportedOperationException(
                    "您提供的密钥错误，diskMirror 拒绝了您的访问\nThe key you provided is incorrect, and diskMirror has denied your access\nerror key = " +
                            showData
            );
        }
    }

    @Override
    public String message() {
        return "负责请求信息中 安全密钥 的校验工作！";
    }

    /**
     * 检查函数 此函数会处于所有服务函数的第一行
     *
     * @param config     需要使用的适配器的配置类对象
     * @param jsonObject 需要被检查的 jsonObject 一般会包含 userId 和 sk 参数
     * @throws CheckException 检查不通过则抛出异常
     */
    @Override
    public void checkFun(Config config, JSONObject jsonObject) throws CheckException {
        final Object orDefault = jsonObject.getOrDefault(Config.SECURE_KEY, 0);
        checkSK(config, jsonObject.getString("userId"), orDefault instanceof Integer ? (int) orDefault : 0);
    }
}
