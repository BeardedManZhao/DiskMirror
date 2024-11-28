package top.lingyuzhao.diskMirror.core.module;

/**
 * 验证模块抽象类
 *
 * @author 赵凌宇
 */
public abstract class Verification implements VerificationModule {

    protected final String message;
    private final String moduleName;

    public Verification(String moduleName, String message) {
        this.moduleName = moduleName;
        this.message = message;
    }

    @Override
    public String name() {
        return this.moduleName;
    }

    @Override
    public String message() {
        return this.message;
    }
}
