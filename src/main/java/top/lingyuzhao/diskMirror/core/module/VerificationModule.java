package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;

/**
 * 校验模块 此校验模块的所有实现类都会被自动的添加与注册。当每次有请求过来时，会自动调用所有注册的校验模块。
 * <p>
 * All implementation classes of this verification module will be automatically added and registered. Every time a request comes, it will automatically call all registered verification modules.
 */
public interface VerificationModule {

    /**
     *
     * @return 当前模块的作用以及其描述！
     */
    String message();

    void checkFun(Config config, JSONObject jsonObject) throws CheckException;
}
