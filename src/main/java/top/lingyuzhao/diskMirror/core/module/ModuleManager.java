package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;

import java.util.ArrayList;

/**
 * 模块管理者对象！
 */
public final class ModuleManager {
    private final static ArrayList<VerificationModule> VERIFICATION_MODULES = new ArrayList<>();

    /**
     * 将当前项目中的已知模块进行注册
     */
    public static void init() {
        VERIFICATION_MODULES.add(new SkCheckModule());
    }

    /**
     * 将一个模块注册到校验器
     *
     * @param module 需要被注册的模块对象
     */
    public static void registerModule(VerificationModule module) {
        VERIFICATION_MODULES.add(module);
    }

    /**
     * 调用所有的校验模块进行校验
     *
     * @param config     适配器中的配置对象
     * @param jsonObject json 对象
     * @throws CheckException 检查操作时的异常信息
     */
    public static void check(Config config, JSONObject jsonObject) throws CheckException {
        for (VerificationModule verificationModule : VERIFICATION_MODULES) {
            verificationModule.checkFun(config, jsonObject);
        }
    }
}
