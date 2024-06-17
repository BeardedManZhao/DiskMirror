import top.lingyuzhao.diskMirror.core.DiskMirrorRequest;
import top.lingyuzhao.diskMirror.core.Type;

/**
 * @author zhao
 */
public final class MAIN {
    public static void main(String[] args) {
        // 我们发现创建请求的时候 有时候会很繁琐，因此我们提供了快捷的创建请求的方法 就是下面这种，它的效果等同于手动操作 FastJson2
        final DiskMirrorRequest diskMirrorRequest = DiskMirrorRequest.reName(1, Type.TEXT, "test.txt", "test2.txt");
        System.out.println(diskMirrorRequest);
    }
}