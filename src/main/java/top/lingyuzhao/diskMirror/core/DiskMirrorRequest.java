package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;

import java.util.Map;

/**
 * 在 盘镜 中的请求类，使用此类来进行请求数据的构造将可以有效减少请求参数构造的繁琐代码！
 * <p>
 * In the request class of the “disk mirror”, using this class to construct request data can effectively reduce the tedious code of request parameter construction!
 *
 * @author zhao
 */
public class DiskMirrorRequest extends JSONObject {


    /**
     * 关于此类的构造函数
     *
     * @param map 我们允许传入一个map，然后自动将map中的键值对构造成jsonObject
     */
    protected DiskMirrorRequest(Map map) {
        super(map);
    }

    protected DiskMirrorRequest(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 关于此类的构造函数
     *
     * @param jsonObject 我们允许传入一个jsonObject，然后自动将jsonObject中的键值对构造成jsonObject
     */
    public static DiskMirrorRequest createFrom(JSONObject jsonObject) {
        return new DiskMirrorRequest(jsonObject);
    }

    /**
     * 创建 getUrls 请求
     *
     * @param userId 需要被获取到的空间的所属 id
     * @param type   需要被获取到的空间的类型
     * @return 操作结果！
     */
    public static DiskMirrorRequest getUrls(int userId, Type type) {
        return new DiskMirrorRequest(6).setUserId(userId).setType(type);
    }

    /**
     * 创建 Upload 和 remove 请求
     *
     * @param userId 需要被获取到的空间的所属 id
     * @param type   需要被获取到的空间的类型
     * @return 操作结果！
     */
    public static DiskMirrorRequest uploadRemove(int userId, Type type, String fileName) {
        return getUrls(userId, type).setFileName(fileName);
    }


    /**
     * 创建 reName 请求
     *
     * @param userId 需要被获取到的空间的所属 id
     * @param type   需要被获取到的空间的类型
     * @return 操作结果！
     */
    public static DiskMirrorRequest reName(int userId, Type type, String fileName, String newName) {
        return uploadRemove(userId, type, fileName).setNewName(newName);
    }

    /**
     * 设置当前的请求对象中 userId
     *
     * @param userId 代表的是需要操作的空间的 id
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setUserId(int userId) {
        this.put("userId", userId);
        return this;
    }

    /**
     * 设置当前的请求对象中 type
     *
     * @param type 代表的是需要操作的空间的类型 不同类型将会分开存储和计算 若不需要则直接使用二进制即可
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setType(Type type) {
        this.put("type", type);
        return this;
    }

    /**
     * 设置当前的请求对象中 fileName
     *
     * @param fileName 代表的是需要操作的文件的名称
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setFileName(String fileName) {
        this.put("fileName", fileName);
        return this;
    }

    /**
     * 设置当前的请求对象中 newName
     *
     * @param newName 代表的是需要操作的文件的新名称
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setNewName(String newName) {
        this.put("newName", newName);
        return this;
    }

    /**
     * 设置当前的请求对象中 sk
     *
     * @param sk 代表的是需要操作的文件的 secureKey
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setSk(int sk) {
        this.put(Config.SECURE_KEY, sk);
        return this;
    }
}
