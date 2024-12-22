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
        this.setSk(0);
    }

    protected DiskMirrorRequest(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 关于此类的构造函数
     *
     * @param jsonObject 我们允许传入一个jsonObject，然后自动将jsonObject中的键值对构造成jsonObject
     * @return 根据传入的jsonObject构造的 请求对象
     */
    public static DiskMirrorRequest createFrom(JSONObject jsonObject) {
        return new DiskMirrorRequest(jsonObject);
    }

    /**
     * 对于当前空间进行一个访问令牌的生成
     *
     * @param userId 需要被创建访问令牌的空间id
     * @return 请求对象！
     */
    public static DiskMirrorRequest setSpaceSk(int userId) {
        return new DiskMirrorRequest(6).setUserId(userId);
    }


    /**
     * 创建 getUrls 请求
     *
     * @param userId 需要被获取到的空间的所属 id
     * @param type   需要被获取到的空间的类型
     * @return 请求对象！
     */
    public static DiskMirrorRequest getUrls(int userId, Type type) {
        return setSpaceSk(userId).setType(type);
    }

    /**
     * 创建 Upload 和 remove 请求
     *
     * @param userId   需要被获取到的空间的所属 id
     * @param type     需要被获取到的空间的类型
     * @param fileName 需要被操作的文件的名称
     * @return 请求对象！
     */
    public static DiskMirrorRequest uploadRemove(int userId, Type type, String fileName) {
        return getUrls(userId, type).setFileName(fileName);
    }

    /**
     * 创建 download 请求
     *
     * @param userId   需要被获取文件 所属的 id
     * @param type     需要被获取到的空间的类型
     * @param fileName 需要被获取到的文件的名称
     * @return 请求对象！
     */
    public static DiskMirrorRequest download(int userId, Type type, String fileName) {
        return uploadRemove(userId, type, fileName);
    }

    /**
     * 创建 Upload 和 remove 请求
     *
     * @param userId  需要被获取到的空间的所属 id
     * @param type    需要被获取到的空间的类型
     * @param dirName 需要被创建的文件夹名称
     * @return 请求对象！
     */
    public static DiskMirrorRequest mkdirs(int userId, Type type, String dirName) {
        return getUrls(userId, type).setDirName(dirName);
    }

    /**
     * 创建 transferDeposit 请求
     *
     * @param userId 需要被操作到的空间的所属 id
     * @param type   需要被操作到的空间的类型
     * @return 请求对象！
     */
    public static DiskMirrorRequest transferDepositStatus(int userId, Type type) {
        return getUrls(userId, type);
    }

    /**
     * 创建 transferDeposit 请求
     *
     * @param userId   需要被操作到的空间的所属 id
     * @param type     需要被操作到的空间的类型
     * @param fileName 需要被操作的文件的名称，文件被转存成功后，要存储在哪个位置
     * @param url      需要被操作的文件的 url
     * @return 请求对象！
     */
    public static DiskMirrorRequest transferDeposit(int userId, Type type, String fileName, String url) {
        return uploadRemove(userId, type, fileName).setUrl(url);
    }

    /**
     * 创建 reName 请求
     *
     * @param userId   需要被获取到的空间的所属 id
     * @param type     需要被获取到的空间的类型
     * @param fileName 需要被操作的文件的名称
     * @param newName  代表的是需要操作的文件的新名称
     * @return 请求对象！
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
     * 设置当前的请求对象中 fileName
     *
     * @param fileName 代表的是需要操作的文件的名称
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setDirName(String fileName) {
        this.put("dirName", fileName);
        return this;
    }

    /**
     * 设置当前的请求对象中 fileName
     *
     * @param url 请求对象中的 url 参数
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setUrl(String url) {
        this.put("url", url);
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

    /**
     * 设置当前的请求期望的返回值是否需要带协议前缀，协议前缀一般会搭配外界服务器 如 ftp web 等，这能够让外界的服务器直接访问盘镜，但并不建议，因为那会绕过盘镜的下载模块！
     *
     * @param isUse 是否使用协议前缀，默认为 true
     * @return 当前的请求对象
     */
    public DiskMirrorRequest setProtocolPrefix(boolean isUse) {
        this.put(Config.PROTOCOL_PREFIX, isUse);
        return this;
    }
}
