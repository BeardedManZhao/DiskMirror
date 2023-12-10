package top.lingyuzhao.cloudPool.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.cloudPool.conf.Config;

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
     */
    JSONObject upload(InputStream inputStream, JSONObject jsonObject) throws IOException;

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
     */
    JSONObject getUrls(JSONObject jsonObject);

}
