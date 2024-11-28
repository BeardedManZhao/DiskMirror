package top.lingyuzhao.diskMirror.core.module;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.exception.CheckException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * 模块管理者对象！
 */
public final class ModuleManager {

    private final static HashMap<String, Integer> VERIFICATION_READ_MODULES = new HashMap<>();

    private final static HashMap<String, Integer> VERIFICATION_WRITER_MODULES = new HashMap<>();

    private final static ArrayList<VerificationModule> readModules = new ArrayList<>();
    private final static ArrayList<VerificationModule> writerModules = new ArrayList<>();

    /**
     * 将一个模块注册到校验器
     *
     * @param module 需要被注册的模块对象
     */
    public static void registerModule(VerificationModule module) {
        registerModuleRead(module);
        registerModuleWriter(module);
    }

    /**
     * 将一个模块注册到 读操作
     *
     * @param module 需要被注册的模块对象
     */
    public static void registerModuleRead(VerificationModule module) {
        if (!module.isRead()) {
            VERIFICATION_READ_MODULES.put(module.name(), readModules.size());
            readModules.add(module);
        }
    }

    /**
     * 将一个模块注册到 写操作
     *
     * @param module 需要被注册的模块对象
     */
    public static void registerModuleWriter(VerificationModule module) {
        if (!module.isWriter()) {
            VERIFICATION_WRITER_MODULES.put(module.name(), writerModules.size());
            writerModules.add(module);
        }
    }

    /**
     * 判断是否已经注册
     *
     * @param name 模块名称
     * @return 是否被读操作注册 如果被注册为读取或写入则代表被注册了
     */
    public static boolean checkIsRegisterRead(String name) {
        return VERIFICATION_READ_MODULES.containsKey(name);
    }

    /**
     * 判断是否已经注册
     *
     * @param name 模块名称
     * @return 是否被写操作注册 如果被注册为读取或写入则代表被注册了
     */
    public static boolean checkIsRegisterWriter(String name) {
        return VERIFICATION_WRITER_MODULES.containsKey(name);
    }

    /**
     * 删除并取出一个模块
     *
     * @param name 模块名称
     * @return 被删除并取出的模块对象
     */
    public static VerificationModule removeVerificationModuleRead(String name) {
        final Integer remove = VERIFICATION_READ_MODULES.remove(name);
        if (remove == null) {
            return null;
        }
        return readModules.set(remove, null);
    }

    /**
     * 删除并取出一个模块
     *
     * @param name 模块名称
     * @return 被删除并取出的模块对象
     */
    public static VerificationModule removeVerificationModuleWriter(String name) {
        final Integer remove = VERIFICATION_WRITER_MODULES.remove(name);
        if (remove == null) {
            return null;
        }
        return writerModules.set(remove, null);
    }

    /**
     * 调用所有的读取校验模块进行校验
     *
     * @param config     适配器中的配置对象
     * @param jsonObject json 对象
     * @throws CheckException 检查操作时的异常信息
     */
    public static void checkRead(Config config, JSONObject jsonObject) throws CheckException {
        for (VerificationModule verificationModule : readModules) {
            if (verificationModule == null) {
                continue;
            }
            verificationModule.checkFun(config, jsonObject);
        }
    }

    /**
     * 调用所有的写入校验模块进行校验
     *
     * @param config     适配器中的配置对象
     * @param jsonObject json 对象
     * @throws CheckException 检查操作时的异常信息
     */
    public static void checkWriter(Config config, JSONObject jsonObject) throws CheckException {
        for (VerificationModule verificationModule : writerModules) {
            if (verificationModule == null) {
                continue;
            }
            verificationModule.checkFun(config, jsonObject);
        }
    }

    /**
     * 整理读操作校验模块中的碎片 让其性能更好！频繁调用了 removeVerificationModuleRead 方法的话 可以使用此操作！
     */
    public static void arrangementRead() {
        // 清理 null 碎片
        readModules.removeIf(Objects::isNull);
        // 重设索引
        VERIFICATION_READ_MODULES.clear();
        int index = 0;
        for (VerificationModule readModule : readModules) {
            VERIFICATION_READ_MODULES.put(readModule.name(), index++);
        }
    }

    /**
     * 整理写操作校验模块中的碎片 让其性能更好！频繁调用了 removeVerificationModuleWriter 方法的话 可以使用此操作！
     */
    public static void arrangementWriter() {
        // 清理 null 碎片
        writerModules.removeIf(Objects::isNull);
        // 重设索引
        VERIFICATION_WRITER_MODULES.clear();
        int index = 0;
        for (VerificationModule readModule : writerModules) {
            VERIFICATION_WRITER_MODULES.put(readModule.name(), index++);
        }
    }
}
