package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 适配器对象的接口，通过此类可以实现操作对应的文件系统设备，同时也是通过此类的实例来管理文件系统设备
 * <p>
 * The interface of the adapter object, through which operations can be carried out on corresponding file system devices, and instances of this class can also be used to manage file system devices
 */
public interface Adapter {

    /**
     * 检查函数 此函数会处于所有服务函数的第一行
     *
     * @param config     需要使用的适配器的配置类对象
     * @param jsonObject 需要被检查的 json 对象
     */
    static void checkJsonObj(Config config, JSONObject jsonObject) {
        if (jsonObject == null) {
            throw new UnsupportedOperationException("您提供的 json 对象为空，diskMirror 拒绝了您的访问\nThe json object you provided is empty, and diskMirror has denied your access\nerror json = null");
        }
        checkSK(config, (int) jsonObject.getOrDefault(Config.SECURE_KEY, 0));
    }

    /**
     * 检查函数 此函数会处于所有服务函数的第一行
     *
     * @param config 需要使用的适配器的配置类对象
     * @param sk     需要被检查的 sk 参数
     */
    static void checkSK(Config config, int sk) {
        if (config.getSecureKey() != sk) {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(sk));
            stringBuilder.replace(1, stringBuilder.length() - 1, "******");
            throw new UnsupportedOperationException("您提供的密钥错误，diskMirror 拒绝了您的访问\nThe key you provided is incorrect, and diskMirror has denied your access\nerror key = " + stringBuilder);
        }
    }

    /**
     * 获取到适配器配置类对象
     *
     * @return 配置类对象
     */
    Config getConfig();

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     */
    void setSpaceMaxSize(String spaceId, long maxSize);

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     * @param sk      安全密钥
     */
    void setSpaceMaxSize(String spaceId, long maxSize, int sk);

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path 需要被删除的文件目录
     * @return 被删除的所有文件所展示用的空间字节数
     * @throws IOException 删除操作出现异常
     */
    long rDelete(String path) throws IOException;


    /**
     * 将一个字符串写到文件中，并将文件保存
     *
     * @param data      文件数据流
     * @param fileName  文件名称
     * @param userId    空间id
     * @param type      文件类型,
     * @param secureKey 需要使用的加密密钥
     * @return {
     * res:上传结果,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    JSONObject writer(String data, String fileName, int userId, String type, int secureKey) throws IOException;


    /**
     * 将一个字符串写到文件中，并将文件保存
     *
     * @param bytes     需要被写入的二进制数据
     * @param fileName  文件名称
     * @param userId    空间id
     * @param type      文件类型,
     * @param secureKey 需要使用的加密密钥
     * @return {
     * res:上传结果,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    JSONObject writer(byte[] bytes, String fileName, int userId, String type, int secureKey) throws IOException;

    /**
     * 将一个文件上传
     *
     * @param inputStream 文件数据流
     * @param jsonObject  {
     *                    fileName  文件名称
     *                    userId      空间id
     *                    type        文件类型,
     *                    secure.key  加密密钥
     *                    }
     * @return {
     * res:上传结果,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    JSONObject upload(InputStream inputStream, JSONObject jsonObject) throws IOException;

    /**
     * 将一个文件删除
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return {res: 删除结果}
     * @throws IOException 操作异常
     */
    JSONObject remove(JSONObject jsonObject) throws IOException;

    /**
     * 将一个文件进行重命名操作
     *
     * @param jsonObject {
     *                   fileName  文件名称,
     *                   newName  文件重命名之后的名称,
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return {res: 删除结果}
     * @throws IOException 操作异常
     */
    JSONObject reName(JSONObject jsonObject) throws IOException;

    /**
     * 将一个文件下载，使用数据流的方式来进行文件获取。
     * @param jsonObject  {
     *                    fileName  文件名称
     *                    userId      空间id
     *                    type        文件类型,
     *                    secure.key  加密密钥
     *                    }
     * @return 对应的目标文件的数据流对象
     * @throws IOException 操作异常
     */
    InputStream downLoad(JSONObject jsonObject) throws IOException;

    /**
     * 将一个用户所有的 url 获取到
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return {
     * userId:文件所属用户id,
     * urls:[
     * {
     * url:文件的url
     * type:文件类型
     * }
     * ]
     * }
     * @throws IOException 操作异常
     */
    JSONObject getUrls(JSONObject jsonObject) throws IOException;

    /**
     * 通过盘镜在指定的用户文件空间中创建一个文件夹
     *
     * @param jsonObject {
     *                   dirName     文件目录名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return {res: 操作结果}
     * @throws IOException 创建过程出现错误则返回此异常对象
     */
    JSONObject mkdirs(JSONObject jsonObject) throws IOException;

    /**
     * 获取用户使用空间大小
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return 空间已使用的大小
     * @throws IOException 操作异常
     */
    long getUseSize(JSONObject jsonObject) throws IOException;

    /**
     * 获取用户使用空间大小
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @param path       可以直接用于路径统计的路径
     * @return 空间已使用的大小
     * @throws IOException 操作异常
     */
    long getUseSize(JSONObject jsonObject, String path) throws IOException;

    /**
     * 获取指定空间 id 的最大占用量，此函数的返回值是空间最大容量的字节数值。
     * <p>
     * Get the maximum usage of the specified space ID, and the return value of this function is the byte value of the maximum capacity of the space.
     *
     * @param id 需要被检索的空间的 id
     *           <p>
     *           The ID of the space that needs to be retrieved
     * @return 用户空间的存储最大的大小 字节为单位，请注意这里的返回值是最大大小，而不是已使用的大小，如果您需要获取已使用的字节数 请调用 getUseSize 方法
     * <p>
     * The maximum storage size of user space is in bytes. Please note that the return value here is the maximum size, not the used size. If you need to obtain the number of used bytes, please call the getUseSize method
     */
    long getSpaceMaxSize(String id);

    /**
     * 关闭适配器
     */
    void close();

    /**
     * @return 当前适配器对应的版本号
     */
    String version();
}
