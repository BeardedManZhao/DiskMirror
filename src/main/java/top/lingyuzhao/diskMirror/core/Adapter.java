package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.core.module.VerificationModule;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

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
        VerificationModule.check(config, jsonObject);
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
     * 获取某个空间的所有进度条，这些进度条代表的往往都是正在处于上传状态的文件的操作进度对象
     * Retrieves all progress bars for a given space, typically representing ongoing upload operations for files within that space.
     *
     * @param id 指定的空间的 id
     *           A JSON collection of progress bar objects, where the keys correspond to progress scales.
     * @return 进度条对象的 json 集合 其中 key 是进度id value 是进度对象
     * The unique identifier of the specified space.
     * 返回示例：{"diskMirror-1.2.1-javadoc.jar":{"count":98304,"maxCount":214246.0,"progressId":"diskMirror-1.2.1-javadoc.jar","spaceId":"1"}}
     */
    JSONObject getAllProgressBar(String id);

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
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
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
     * 将当前适配器对应的文件系统，指定空间的所有文件的路径获取到！
     * <p>
     * Retrieve the paths of all files in the specified space from the file system corresponding to the current adapter!
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @param result     每一个文件路径的处理逻辑
     * @return 文件系统的 json 结构对象，当处理结束之后，会将文件系统的 json 结构也一并返回
     * <p>
     * The JSON structure object of the file system will be returned together with the JSON structure of the file system after processing is completed
     * @throws IOException 操作异常
     */
    JSONObject getFilesPath(JSONObject jsonObject, Consumer<String> result) throws IOException;

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
     * 跨盘镜空间文件转移，将一个非盘镜空间的文件通过 url 上传到盘镜空间
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥,
     *                   url 需要被下载的文件的路径
     *                   }
     * @return 操作结果
     * @throws IOException 异常信息
     */
    JSONObject transferDeposit(JSONObject jsonObject) throws IOException;

    /**
     * 将一个 url 地址指向的数据进行转存操作，将 url 指向的文件转存到空间中的指定位置！
     * <p>
     * Transfer the data pointed to by a URL address to a specified location in the space!
     *
     * @param jsonObject {
     *                   fileName     文件目录名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @param url        需要被转存的文件资源对应的地址
     *                   <p>
     *                   The address corresponding to the file resource that needs to be transferred
     * @return 转存后的结果
     * <p>
     * Result after transfer
     * @throws IOException 操作异常信息
     */
    JSONObject transferDeposit(JSONObject jsonObject, URL url) throws IOException;

    /**
     * 查询当前用户转存状态
     *
     * @param jsonObject {
     *                   fileName     文件目录名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return 当前空间中 包含着处于转存状态文件的的Map集合 其中 k 代表的就是当前转存的文件名字，value 代表的就是当前转存的 url
     * <p>
     * The current space contains a collection of Maps for files in the transfer state, where k represents the name of the current transfer file and value represents the URL of the current transfer
     */
    JSONObject transferDepositStatus(JSONObject jsonObject);

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
     * 设置指定空间 id 的 sk 访问密钥 若无设置则使用默认密钥
     *
     * @param id 需要被设置的 空间 id
     * @return 操作成功之后，在这里会返回密钥对应的结果，我们需要将此参数返回给客户端，由客户端去使用！
     * @throws IOException 操作异常
     */
    int setSpaceSk(String id) throws IOException;

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
