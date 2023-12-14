package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.utils.PathGeneration;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件系统适配器
 *
 * @author zhao
 */
public abstract class FSAdapter implements Adapter {

    /**
     * 配置类
     */
    protected final Config config;

    /**
     * 构建一个适配器
     *
     * @param config 适配器中的配置文件对象
     */
    public FSAdapter(Config config) {
        this.config = config;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path        路径对象
     * @param inJson      输入参数 json 对象
     * @param inputStream 文件数据流
     * @return {
     * res:上传结果/错误,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorUpload(String path, JSONObject inJson, InputStream inputStream) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * urls:[{url:文件的url, size:文件的大小, name:文件的名字}]
     * }
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorGetUrls(String path, JSONObject inJson) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * urls:[{url:文件的url, size:文件的大小, name:文件的名字}]
     * }
     */
    protected abstract JSONObject pathProcessorRemove(String path, JSONObject inJson);

    /**
     * 获取到适配器配置类对象
     *
     * @return 配置类对象
     */
    @Override
    public Config getConfig() {
        return this.config;
    }

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
     * res:上传结果/错误,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * type:文件类型
     * }
     */
    @Override
    public JSONObject upload(InputStream inputStream, JSONObject jsonObject) throws IOException {
        // 首先获取到 文件的路径
        final Config config = this.getConfig();
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        return pathProcessorUpload(path, jsonObject, inputStream);
    }

    /**
     * 将一个文件删除
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型
     *                   }
     * @return {res: 删除结果}
     */
    @Override
    public JSONObject remove(JSONObject jsonObject) {
        // 获取到路径
        final Config config = this.getConfig();
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        return this.pathProcessorRemove(path, jsonObject);
    }

    /**
     * 将一个用户所有的 url 获取到
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   }
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * urls:[{url:文件的url, size:文件的大小, name:文件的名字}]
     * }
     */
    @Override
    public JSONObject getUrls(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        return pathProcessorGetUrls(path, jsonObject);
    }

    /**
     * 关闭适配器
     */
    @Override
    public void close() {

    }
}
