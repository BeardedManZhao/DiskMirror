import top.lingyuzhao.diskMirror.core.module.ModuleManager;
import top.lingyuzhao.diskMirror.core.module.SkCheckModule;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        final SkCheckModule skCheckModule = new SkCheckModule("test", "一个用来测试的模块");
        // 这个时候都是 false 因为其没有被装载到模块管理器中
        System.out.println(skCheckModule.isRead());
        System.out.println(skCheckModule.isWriter());

        // 将模块注册到读模块管理器中
        ModuleManager.registerModuleRead(skCheckModule);

        // 这个时候Read是 true 因为其被装载到读操作模块管理器中
        System.out.println(skCheckModule.isRead());
        System.out.println(skCheckModule.isWriter());
    }
}