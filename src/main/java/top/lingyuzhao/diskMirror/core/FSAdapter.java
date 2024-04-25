package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.diskMirror.utils.JsonUtils;
import top.lingyuzhao.diskMirror.utils.PathGeneration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * 文件系统适配器接口
 * <p>
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
     * 使用占用映射 key是空间id  value 就是空间中已使用的数量
     */
    protected final HashMap<String, Long> REMEMBER_MAP = new HashMap<>();
    /**
     * 转存操作记录类
     */
    protected final HashMap<String, JSONObject> transferDepositMap = new HashMap<>();

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
        this.setSpaceMaxSize(spaceId, maxSize, 0);
    }

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     * @param sk      安全密钥
     */
    @Override
    public void setSpaceMaxSize(String spaceId, long maxSize, int sk) {
        Adapter.checkSK(this.config, sk);
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
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {"res": 创建结果}
     * @throws IOException 操作异常
     */
    protected abstract JSONObject pathProcessorMkdirs(String path, JSONObject inJson) throws IOException;

    /**
     * 路径处理器 接收一个路径 返回此数据的数据流对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return 指定目标文件的数据流对象！
     * @throws IOException 操作异常 | 文件没有找到会异常，这里的异常必须捕获！！！ 否则会导致程序终止运行
     */
    protected abstract InputStream pathProcessorDownLoad(String path, JSONObject inJson) throws IOException;

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
    @Override
    public JSONObject writer(String data, String fileName, int userId, String type, int secureKey) throws IOException {
        return this.writer(data.getBytes(config.getOrDefault(Config.CHAR_SET, "UTF-8").toString()), fileName, userId, type, secureKey);
    }

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
    @Override
    public JSONObject writer(byte[] bytes, String fileName, int userId, String type, int secureKey) throws IOException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileName", fileName);
        jsonObject.put("userId", userId);
        jsonObject.put("type", type);
        jsonObject.put("secure.key", secureKey);
        return this.upload(new ByteArrayInputStream(bytes), jsonObject);

    }

    /**
     * 将一个文件上传
     *
     * @param inputStream 文件数据流
     * @param jsonObject  {
     *                    fileName  文件名称
     *                    userId      空间id
     *                    type        文件类型
     *                    secure.key  加密密钥
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
        return this.upload(inputStream, jsonObject, -1);
    }

    /**
     * 将一个文件上传
     *
     * @param inputStream 文件数据流
     * @param jsonObject  {
     *                    fileName  文件名称
     *                    userId      空间id
     *                    type        文件类型
     *                    secure.key  加密密钥
     *                    }
     * @param streamSize  数据流中所包含的数据量 如果此数值为负数，则会尝试从 inputStream.available() 获取数据量
     * @return {
     * res:上传结果/错误,
     * url:上传之后的 url,
     * userId:文件所属用户id,
     * maxSize: 当前空间的最大使用量,
     * type:文件类型
     * }
     * @throws IOException 操作异常
     */
    public JSONObject upload(InputStream inputStream, JSONObject jsonObject, long streamSize) throws IOException {
        // 首先获取到 文件的路径
        final Config config = this.getConfig();
        Adapter.checkJsonObj(config, jsonObject);
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String[] path = pathGeneration.function(
                jsonObject
        );
        // 获取到增加之后的空间占用
        final Integer userId = jsonObject.getInteger("userId");
        final String type = jsonObject.getString("type");

        // 首先获取到使用的空间占用
        final long inputSize = streamSize > 0 ? streamSize : inputStream.available();
        if (inputSize < 0) {
            // 代表数据流不合法，在这里清理缓存 为了重新计算数据占用
            this.removeUseSize(userId, type);
        }

        final long l = this.addUseSize(userId, type, inputSize);
        final long maxSize = config.getSpaceMaxSize(userId.toString());
        jsonObject.put("useAgreement", !config.getString(Config.PROTOCOL_PREFIX).isEmpty());
        if (l > maxSize) {
            jsonObject.put("useSize", this.diffUseSize(userId, type, inputSize));
            throw new IOException("id为 " + userId + " 的 " + type + " 空间不足，因为上传《" + jsonObject.getString("fileName") + "》之后的字节数【" + l + "】 > 最大字节数【" + maxSize + "】");
        }
        jsonObject.put("streamSize", inputSize);
        try {
            final JSONObject jsonObject1 = pathProcessorUpload(path[2], path[3], jsonObject, inputStream);
            jsonObject1.put("useSize", l);
            jsonObject1.put("maxSize", maxSize);
            return jsonObject1;
        } catch (IOException | RuntimeException e) {
            jsonObject.put("useSize", this.diffUseSize(userId, type, inputSize));
            throw e;
        }
    }

    @Override
    public InputStream downLoad(JSONObject jsonObject) throws IOException {
        // 首先获取到 文件的路径
        final Config config = this.getConfig();
        Adapter.checkJsonObj(config, jsonObject);
        // 这里就是文件的路径了
        final String path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        )[2];
        // 直接调用抽象逻辑
        return this.pathProcessorDownLoad(path, jsonObject);
    }

    /**
     * 将一个文件删除
     *
     * @param jsonObject {
     *                   fileName  文件名称
     *                   userId      空间id
     *                   type        文件类型
     *                   secure.key  加密密钥
     *                   }
     * @return {res: 删除结果,maxSize: 当前空间的最大使用量,}
     * @throws IOException 操作异常
     */
    @Override
    public JSONObject remove(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        Adapter.checkJsonObj(config, jsonObject);
        final String[] path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return this.pathProcessorRemove(path[2], jsonObject);
    }

    /**
     * 将一个文件进行重命名操作
     *
     * @param jsonObject {
     *                   fileName  文件名称,
     *                   newName  文件重命名之后的名称,
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
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
        Adapter.checkJsonObj(config, jsonObject);
        // 这里就是父目录和子目录
        final String[] path = ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        );
        jsonObject.put("useSize", getUseSize(jsonObject, path[0]));
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return this.pathProcessorReName(path[0], jsonObject);
    }

    /**
     * 将一个用户所有的 url 获取到
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   secure.key  加密密钥
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
        Adapter.checkJsonObj(config, jsonObject);
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String[] path = pathGeneration.function(
                jsonObject
        );
        jsonObject.put("useSize", getUseSize(jsonObject, path[0]));
        jsonObject.put("useAgreement", !config.getString(Config.PROTOCOL_PREFIX).isEmpty());
        jsonObject.put("maxSize", config.getSpaceMaxSize(jsonObject.getString("userId")));
        return pathProcessorGetUrls(path[0], path[1], jsonObject);
    }

    @Override
    public JSONObject getFilesPath(JSONObject jsonObject, Consumer<String> r) throws IOException {
        final JSONObject urls = this.getUrls(jsonObject);
        JsonUtils.jsonToList(urls, "urls", "fileName", r, "", '/');
        return urls;
    }

    /**
     * 通过盘镜在指定的用户文件空间中创建一个文件夹
     *
     * @param jsonObject {
     *                   fileName     文件目录名称
     *                   userId      空间id
     *                   type        文件类型,
     *                   secure.key  加密密钥
     *                   }
     * @return {res: 操作结果}
     * @throws IOException 创建过程出现错误则返回此异常对象
     */
    @Override
    public JSONObject mkdirs(JSONObject jsonObject) throws IOException {
        // 获取到路径
        final Config config = this.getConfig();
        Adapter.checkJsonObj(config, jsonObject);
        final PathGeneration pathGeneration = (PathGeneration) config.get(Config.GENERATION_RULES);
        final String[] path = pathGeneration.function(
                jsonObject
        );
        // 直接开始创建
        jsonObject.put("useSize", this.getUseSize(jsonObject, path[0]));
        return pathProcessorMkdirs(path[2], jsonObject);
    }

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
    public JSONObject transferDeposit(JSONObject jsonObject) throws IOException {
        final Object url = jsonObject.remove("url");
        final URL url1;
        if (url instanceof String) {
            url1 = new URL((String) url);
        } else {
            url1 = (URL) url;
        }
        return this.transferDeposit(jsonObject, url1);
    }

    @Override
    public JSONObject transferDeposit(JSONObject jsonObject, URL url) throws IOException {
        final String s = jsonObject.get("userId").toString() + '_' + jsonObject.get("type");
        final String string = jsonObject.getString("fileName");
        JSONObject jsonObject1 = this.transferDepositMap.get(s);
        final boolean b = jsonObject1 != null;
        if (b && jsonObject1.containsKey(string)) {
            // 代表正在转存 不需要进行多次转存
            jsonObject.put(config.getString(Config.RES_KEY), string + " 正在转存中，请不要重复转存哦!!!");
            return jsonObject;
        }
        final URLConnection urlConnection = url.openConnection();
        try (final InputStream inputStream = urlConnection.getInputStream()) {
            // 开始标记
            if (b) {
                jsonObject1.put(string, url.toString());
            } else {
                jsonObject1 = new JSONObject();
                jsonObject1.put(string, url.toString());
                this.transferDepositMap.put(s, jsonObject1);
            }
            // 开始转存
            final JSONObject upload = this.upload(inputStream, jsonObject, urlConnection.getContentLengthLong());
            // 移除标记
            jsonObject1.remove(string);
            return upload;
        }
    }

    @Override
    public JSONObject transferDepositStatus(JSONObject jsonObject) {
        final JSONObject jsonObject1 = this.transferDepositMap.get(jsonObject.get("userId").toString() + '_' + jsonObject.get("type"));
        return jsonObject1 != null ? jsonObject1 : new JSONObject();
    }

    /**
     * 获取用户使用空间大小
     *
     * @param jsonObject {
     *                   userId      空间id,
     *                   type        文件类型,
     *                   }
     * @param path       需要被统计的空间的路径，需要注意这里不能包含任何的子文件
     * @return 空间已使用的大小
     */
    @Override
    public long getUseSize(JSONObject jsonObject, String path) throws IOException {
        final Integer userId = jsonObject.getInteger("userId");
        final String key = userId + jsonObject.getString("type");
        if (REMEMBER_MAP.containsKey(key)) {
            // 代表有值了 直接返回 Map 中的值
            return REMEMBER_MAP.get(key);
        }
        // 计算占用空间
        final long l = this.pathProcessorUseSize(path, jsonObject);
        REMEMBER_MAP.put(key, l);
        return l;
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
        // 如果需要计算就先在这里判断是否包含文件名字 如果包含就去除文件名字
        return getUseSize(jsonObject, ((PathGeneration) config.get(Config.GENERATION_RULES)).function(
                jsonObject
        )[0]);
    }

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
    @Override
    public long getSpaceMaxSize(String id) {
        return config.getSpaceMaxSize(id);
    }

    /**
     * 清理掉当前用户的使用空间大小，这个操作常用来进行遇到未知数据流时候的缓存清理
     *
     * @param id   空间id
     * @param type 空间类型
     */
    public void removeUseSize(int id, String type) {
        final String key = id + type;
        REMEMBER_MAP.remove(key);
    }


    public long addUseSize(int id, String type, long size) throws IOException {
        return addUseSize(id, type, size, false);
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
        return diffUseSize(id, type, size, false);
    }

    /**
     * 手动指定用户空间的占用量
     *
     * @param id            用户id
     * @param type          文件类型
     * @param size          要调整的空间占用大小
     * @param notMustUpdate 若不是必须要修改的情况下，此数值为 true，这样的话，当我们再进行了初始化操作之后，这里不会继续修改，若初始化已经进行过，则进行修改
     * @return 用户空间的存储占用大小 字节为单位
     * @throws IOException 操作异常
     */
    public long addUseSize(int id, String type, long size, boolean notMustUpdate) throws IOException {
        final String key = id + type;
        if (initUseSize(key, id, type) && notMustUpdate) {
            // 代表第一次初始化 同时设置了不必须修改的情况下
            return REMEMBER_MAP.get(key);
        }
        final Long l1 = REMEMBER_MAP.get(key);
        final long l = l1 + size;
        REMEMBER_MAP.put(key, l);
        return l;
    }

    /**
     * 手动指定用户空间的占用量
     *
     * @param id            用户id
     * @param type          文件类型
     * @param size          要调整的空间占用大小
     * @param notMustUpdate 若不是必须要修改的情况下，此数值为 true，这样的话，当我们再进行了初始化操作之后，这里不会继续修改，若初始化已经进行过，则进行修改，简单来说就是，再第一次初始化之后，这里能否不修改，因为第一次初始化不一定是非要修改的
     * @return 用户空间的存储占用大小 字节为单位
     * @throws IOException 操作异常
     */
    public long diffUseSize(int id, String type, long size, boolean notMustUpdate) throws IOException {
        final String key = id + type;
        if (initUseSize(key, id, type) && notMustUpdate) {
            // 代表第一次初始化 同时设置了不必须修改的情况下
            return REMEMBER_MAP.get(key);
        }
        final Long l1 = REMEMBER_MAP.get(key);
        final long l = l1 - size;
        REMEMBER_MAP.put(key, l);
        return l;
    }

    /**
     * 初始化用户空间占用量
     *
     * @param key  用户的 key
     * @param id   用户空间id
     * @param type 文件类型
     * @return 如果此空间是第一次初始化，这里就直接返回 true
     * @throws IOException 操作异常
     */
    private boolean initUseSize(String key, int id, String type) throws IOException {
        if (!REMEMBER_MAP.containsKey(key)) {
            // 如果这个路径没有初始化过 就进行一次计算
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", id);
            jsonObject.put("type", type);
            REMEMBER_MAP.put(key, getUseSize(jsonObject));
            return true;
        }
        return false;
    }

    /**
     * 关闭适配器
     */
    @Override
    public void close() {

    }

    /**
     * @return 当前适配器对应的 toString 以及 版本号
     * <p>
     * The toString and version number corresponding to the current adapter
     */
    @Override
    public String version() {
        return this + ":V" + DiskMirror.VERSION;
    }
}
