package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;

import java.io.IOException;
import java.io.InputStream;

/**
 * 适配器对象
 */
public interface Adapter {

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
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path 需要被删除的文件目录
     * @return 被删除的所有文件所展示用的空间字节数
     * @throws IOException 删除操作出现异常
     */
    long rDelete(String path) throws IOException;

    /**
     * 将一个文件上传
     *
     * @param inputStream 文件数据流
     * @param jsonObject  {
     *                    fileName  文件名称
     *                    userId      空间id
     *                    type        文件类型
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
     *                   type        文件类型
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
     *                   type        文件类型
     *                   }
     * @return {res: 删除结果}
     * @throws IOException 操作异常
     */
    JSONObject reName(JSONObject jsonObject) throws IOException;

    /**
     * 将一个用户所有的 url 获取到
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
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
     * 获取用户使用空间大小
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   }
     * @return 空间已使用的大小
     * @throws IOException 操作异常
     */
    long getUseSize(JSONObject jsonObject) throws IOException;

    /**
     * 关闭适配器
     */
    void close();
}
