package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;

import java.util.ArrayList;

/**
 * 校验模块 此校验模块的所有实现类都会被自动的添加与注册。当每次有请求过来时，会自动调用所有注册的校验模块。
 * <p>
 * All implementation classes of this verification module will be automatically added and registered. Every time a request comes, it will automatically call all registered verification modules.
 */
public abstract class VerificationModule {

    private final static ArrayList<VerificationModule> VERIFICATION_MODULES = new ArrayList<>();

    public VerificationModule() {
        VERIFICATION_MODULES.add(this);
    }

    public static void check(Config config, JSONObject jsonObject) throws CheckException {
        for (VerificationModule verificationModule : VERIFICATION_MODULES) {
            verificationModule.checkFun(config, jsonObject);
        }
    }

    public abstract void checkFun(Config config, JSONObject jsonObject) throws CheckException;
}
