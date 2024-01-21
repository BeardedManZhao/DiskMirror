package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.utils.PathGeneration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 文件系统适配器接口
 *
 * File system adapter interface
 *
 * @author zhao
 */
public abstract class FSAdapter implements Adapter {

    /**
     * 配置类
     */
    protected final Config config;
    /**
     * 内存使用占用映射 key是空间id  value 就是空间中已使用的内存数量
     */
    private final HashMap<String, Long> HASH_MAP = new HashMap<>();

    /**
     * 构建一个适配器
     *
     * @param config 适配器中的配置文件对象
     */
    public FSAdapter(Config config) {
        this.config = config;
    }

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     */
    @Override
    public void setSpaceMaxSize(String spaceId, long maxSize) {
        this.config.setSpaceMaxSize(spaceId, maxSize);
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象，这里不强制在返回的地方设置 useSize，会自动获取数据量，当然 如果您希望从自己的算法中获取 useSize 您可以进行设置
     *
     * @param path        路径对象
     * @param path_res    能够直接与协议前缀拼接的路径
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
    protected abstract JSONObject pathProcessorUpload(String path, String path_res, JSONObject inJson, InputStream inputStream) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出结果对象  需要注意的是 您需要在这里设置返回的 useSize
     *
     * @param path     路径对象
     * @param path_res 能够直接与协议前缀拼接的路径
     * @param inJson   文件输入的 json 对象
     * @return 获取到的结果，在这里有一个示例 ```{
     * "userId": 1024,
     * "type": "Binary",
     * "useSize": 787141,
     * "useAgreement": true,
     * "maxSize": 134217728,
     * "urls": [
     * {
     * "fileName": "fsdownload",
     * "url": "http://localhost:8080/1024/Binary//fsdownload",
     * "lastModified": 1705762229601,
     * "size": 0,
     * "type": "Binary",
     * "isDir": true,
     * "urls": [
     * {
     * "fileName": "myFile.png",
     * "url": "http://localhost:8080/1024/Binary//fsdownload/myFile.png",
     * "lastModified": 1705762229664,
     * "size": 293172,
     * "type": "Binary",
     * "isDir": false
     * }
     * ]
     * },
     * {
     * "fileName": "test.png",
     * "url": "http://localhost:8080/1024/Binary//test.png",
     * "lastModified": 1702903450767,
     * "size": 493969,
     * "type": "Binary",
     * "isDir": false
     * }
     * ],
     * "res": "ok!!!!"
     * }```
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出结果对象  需要注意的是 您需要在这里设置返回的 useSize
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorRemove(String path, JSONObject inJson) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出结果对象，这里不强制在返回的地方设置 useSize，会自动获取数据量，当然 如果您希望从自己的算法中获取 useSize 您可以进行设置
     *
     * @param path   重命名操作的作用目录
     * @param inJson 文件输入的 json 对象
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * fileName:旧的文件名字
     * newName:新的文件名字
     * }
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorReName(String path, JSONObject inJson) throws IOException;

    /**
     * 路径处理器 接收一个路径 输出路径中的资源占用量
     *
     * @param path   路径对象 不包含文件名称
     * @param inJson 文件输入的 json 对象 包含空间id 以及 文件类型
     * @return 用户空间的存储占用大小 字节为单位
     * @throws IOException 操作异常
     */
    protected abstract long pathProcessorUseSize(String path, JSONObject inJson) throws IOException;

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
     * maxSize: 当前空间的最大使用量,
     * type:文件类型
     * }
     */
    @Override
    public JSONObject upload(InputStream inputStream, JSONObject jsonObject) throws IOException {
        // 首先获取到 文件的路径
        final Config config = this.getConfig();
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String path = pathGeneration.function(
                jsonObject
        );
        // 首先获取到使用的空间占用
        final long inputSize = inputStream.available();
        // 获取到增加之后的空间占用
        final Integer userId = jsonObject.getInteger("userId");
        final String type = jsonObject.getString("type");
        final long l = this.addUseSize(userId, type, inputSize);
        final long maxSize = config.getSpaceMaxSize(userId.toString());
        jsonObject.put("useAgreement", config.getString(Config.PROTOCOL_PREFIX).length() > 0);
        if (l > maxSize) {
            jsonObject.put("useSize", this.diffUseSize(userId, type, inputSize));
            throw new IOException("id为 " + userId + " 的 " + type + " 空间不足，因为上传《" + jsonObject.getString("fileName") + "》之后的字节数【" + l + "】 > 最大字节数【" + maxSize + "】");
        }
        try {
            final JSONObject jsonObject1 = pathProcessorUpload(path, pathGeneration.function(jsonObject), jsonObject, inputStream);
            jsonObject1.put("useSize", l);
            jsonObject1.put("maxSize", maxSize);
            return jsonObject1;
        } catch (IOException | RuntimeException e) {
            jsonObject.put("useSize", this.diffUseSize(userId, type, inputSize));
            throw e;
        }
    }

    /**
     * 将一个文件删除
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型
     *                   }
     * @return {res: 删除结果,maxSize: 当前空间的最大使用量,}
     */
    @Override
    public JSONObject remove(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return this.pathProcessorRemove(path, jsonObject);
    }

    /**
     * 将一个文件进行重命名操作
     *
     * @param jsonObject {
     *                   fileName  文件名称,
     *                   newName  文件重命名之后的名称,
     *                   userId      空间id
     *                   type        文件类型
     *                   }
     * @return {
     * res : 结果
     * userId:文件所属用户id,
     * type:文件类型,
     * fileName:旧的文件名字,
     * maxSize: 当前空间的最大使用量,
     * newName:新的文件名字
     * }
     * @throws IOException 操作异常
     */
    @Override
    public JSONObject reName(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        // 移除文件名字 用来生成父目录
        final Object fileName = jsonObject.remove("fileName");
        // 这里就是父目录
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        // 重新添加文件名字
        jsonObject.put("fileName", fileName);
        jsonObject.put("useSize", getUseSize(jsonObject.clone()));
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return this.pathProcessorReName(path, jsonObject);
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
     * maxSize: 当前空间的最大使用量,
     * urls:[{url:文件的url, size:文件的大小, name:文件的名字}]
     * }
     */
    @Override
    public JSONObject getUrls(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String path = pathGeneration.function(
                jsonObject
        );
        jsonObject.put("useSize", getUseSize(jsonObject));
        jsonObject.put("useAgreement", config.getString(Config.PROTOCOL_PREFIX).length() > 0);
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return pathProcessorGetUrls(path, pathGeneration.function(jsonObject), jsonObject);
    }

    /**
     * 获取用户使用空间大小
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   }
     * @return 空间已使用的大小
     */
    @Override
    public long getUseSize(JSONObject jsonObject) throws IOException {
        final Integer userId = jsonObject.getInteger("userId");
        final String key = userId + jsonObject.getString("type");
        if (HASH_MAP.containsKey(key)) {
            // 代表有值了 直接返回 Map 中的值
            return HASH_MAP.get(key);
        }
        // 如果需要计算就先在这里判断是否包含文件名字 如果包含就去除文件名字
        jsonObject.remove("fileName");
        // 计算路径
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String path = pathGeneration.function(
                jsonObject
        );
        // 计算占用空间
        final long l = this.pathProcessorUseSize(path, jsonObject);
        HASH_MAP.put(key, l);
        return l;
    }

    /**
     * 手动指定用户空间的占用量
     *
     * @param id   用户id
     * @param type 文件类型
     * @param size 要调整的空间占用大小
     * @return 用户空间的存储占用大小 字节为单位
     * @throws IOException 操作异常
     */
    public long addUseSize(int id, String type, long size) throws IOException {
        final String key = id + type;
        initUseSize(key, id, type);
        final long l = HASH_MAP.get(key) + size;
        HASH_MAP.put(key, l);
        return l;
    }

    /**
     * 手动指定用户空间的占用量
     *
     * @param id   用户id
     * @param type 文件类型
     * @param size 要调整的空间占用大小
     * @return 用户空间的存储占用大小 字节为单位
     * @throws IOException 操作异常
     */
    public long diffUseSize(int id, String type, long size) throws IOException {
        final String key = id + type;
        initUseSize(key, id, type);
        final long l = HASH_MAP.get(key) - size;
        HASH_MAP.put(key, l);
        return l;
    }

    /**
     * 初始化用户空间占用量
     *
     * @param key  用户的 key
     * @param id   用户空间id
     * @param type 文件类型
     * @throws IOException 操作异常
     */
    private void initUseSize(String key, int id, String type) throws IOException {
        if (!HASH_MAP.containsKey(key)) {
            // 如果这个路径没有初始化过 就进行一次计算
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", id);
            jsonObject.put("type", type);
            HASH_MAP.put(key, getUseSize(jsonObject));
        }
    }

    /**
     * 关闭适配器
     */
    @Override
    public void close() {

    }
}
