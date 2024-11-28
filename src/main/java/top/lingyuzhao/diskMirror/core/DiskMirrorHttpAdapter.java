package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * diskMirror 后端服务器的适配器，您可以直接通过此适配器操作远程的 diskMirror 后端服务器!
 * <p>
 * The adapter for the diskMirror backend server allows you to directly operate remote diskMirror backend servers through this adapter!
 *
 * @author zhao
 */
public class DiskMirrorHttpAdapter extends FSAdapter {

    /**
     * 有一些操作是后端服务器版本不适用的 遇到这类函数就会在这里直接抛出错误
     * <p>
     * There are some operations that are not applicable to the backend server version. When encountering such functions, an error will be directly thrown here
     */
    private final static UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException("DiskMirrorHttpAdapter 不支持此函数操作!!! 这是因为 DiskMirror 中暂时没有对应的服务 或 此服务还未启用!!");

    private final Charset charset;
    private final CloseableHttpClient httpClient;
    private final HttpPost httpPost;
    private final URI upload, remove, getUrls, mkdirs, reName, version, useSize, setSpaceSk, transferDeposit, transferDepositStatus;
    private final String downLoad;
    private final String getSpaceMaxSizeURL;

    /**
     * 构建一个适配器
     *
     * @param config     适配器中的配置文件对象
     *                   <p>
     *                   Configuration file objects in the adapter
     * @param controller 远程服务器的 控制器名称，一般来说 这里就是 `FsCrud`
     *                   <p>
     *                   The controller name of the remote server, usually referred to as' FsCrud '`
     * @param httpClient 操作时要使用的 http 客户端对象
     */
    public DiskMirrorHttpAdapter(Config config, String controller, Object httpClient) {
        super(config);
        // 根据远程服务器的 url 构造一个连接
        if (httpClient != null) {
            this.httpClient = (CloseableHttpClient) httpClient;
        } else {
            this.httpClient = HttpClients.createDefault();
        }
        final String url = config.getString(Config.FS_DEFAULT_FS) + '/' + controller + '/';
        this.httpPost = new HttpPost(url);
        try {
            upload = new URI(url + "add");
            version = new URI(url + "getVersion");
            remove = new URI(url + "remove");
            getUrls = new URI(url + "getUrls");
            mkdirs = new URI(url + "mkdirs");
            reName = new URI(url + "reName");
            useSize = new URI(url + "getUseSize");
            transferDeposit = new URI(url + "transferDeposit");
            transferDepositStatus = new URI(url + "transferDepositStatus");
            downLoad = url + "downLoad/";
            getSpaceMaxSizeURL = url + "getSpaceSize?";
            setSpaceSk = new URI(url + "setSpaceSk");
            charset = Charset.forName(config.getString(Config.CHAR_SET));
        } catch (URISyntaxException e) {
            throw new UnsupportedOperationException("error url => " + url, e);
        }
    }

    /**
     * 根据情况将结果构建出来
     *
     * @param string       返回的数据
     * @param url          请求的url
     * @param statusLine   状态行
     * @param checkOkValue 如果需要检查返回结果中的 okValue 则可以设置为 true 反之不需要
     * @return 处理的结果
     */
    private JSONObject getJsonObject(String string, URI url, StatusLine statusLine, boolean checkOkValue) {
        try {
            final JSONObject jsonObject = JSONObject.parseObject(string);
            final String string1 = jsonObject.getString(config.getString(Config.RES_KEY));
            if (checkOkValue) {
                if (string1.equals(config.getString(Config.OK_VALUE))) {
                    jsonObject.put("statusLine", statusLine);
                    return jsonObject;
                } else {
                    throw new JSONException(string1);
                }
            } else {
                jsonObject.put("statusLine", statusLine);
                return jsonObject;
            }
        } catch (JSONException e) {
            throw new JSONException(url.toString() + ':' + string, e);
        } catch (NullPointerException e) {
            throw new UnsupportedOperationException("此操作可能未被支持，或无法连接到服务器！error url: " + url + "; status: " + statusLine, e);
        }
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
     */
    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject inJson, InputStream inputStream) {
        throw unsupportedOperationException;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    /**
     * 路径处理器 接收一个路径 输出结果对象
     *
     * @param path   路径对象
     * @param inJson 文件输入的 json 对象
     * @return {"res": 创建结果}
     */
    @Override
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

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
     */
    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

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
     */
    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    /**
     * 路径处理器 接收一个路径 输出路径中的资源占用量
     *
     * @param path   路径对象 不包含文件名称
     * @param inJson 文件输入的 json 对象 包含空间id 以及 文件类型
     * @return 用户空间的存储占用大小 字节为单位
     */
    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        final JSONObject request = request(inJson, this.useSize);
        final String string = request.getString(this.config.getString(Config.RES_KEY));
        if (string.equals(this.config.getString(Config.OK_VALUE))) {
            return request.getLongValue("useSize");
        }
        throw new IOException(string);
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
        if (jsonObject != null && jsonObject.containsKey("fileName")) {
            // 开始上传
            this.httpPost.setURI(this.upload);
            this.httpPost.setEntity(
                    MultipartEntityBuilder.create()
                            .addBinaryBody("params", jsonObject.toString().getBytes(this.charset), ContentType.APPLICATION_JSON.withCharset(this.charset), "params")
                            .addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, "file")
                            .setContentType(ContentType.MULTIPART_FORM_DATA)
                            .build()
            );
            try (final CloseableHttpResponse execute = httpClient.execute(this.httpPost)) {
                final HttpEntity entity = execute.getEntity();
                final String string = EntityUtils.toString(entity);
                IOUtils.close(inputStream);
                EntityUtils.consume(entity);
                return getJsonObject(string, this.upload, execute.getStatusLine(), true);
            }
        } else {
            throw new UnsupportedOperationException("您的参数中没有包含 fileName 或者 参数为 空，因此无法将您的请求交由后端服务器处理！\nYour parameter does not include fileName or is empty, so your request cannot be handed over to the backend server for processing!\nerror params => " + jsonObject);
        }
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
     */
    @Override
    public JSONObject remove(JSONObject jsonObject) throws IOException {
        // 开始请求
        return this.request(jsonObject, this.remove);
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
        // 开始请求
        return this.request(jsonObject, this.reName);
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
        // 开始请求
        return this.request(jsonObject, this.getUrls);
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
        return this.request(jsonObject, this.mkdirs);
    }

    /**
     * @return 当前适配器对应的 toString 以及 版本号
     * <p>
     * The toString and version number corresponding to the current adapter
     */
    @Override
    public String version() {
        // 开始上传
        this.httpPost.setURI(this.version);
        String string;
        try {
            final HttpEntity entity = httpClient.execute(this.httpPost).getEntity();
            string = "remote → ↘↘\n" + EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            string = "error → " + e;
        }
        return super.version() + "\n ↓↓↓ \n" + string;
    }

    @Override
    public int setSpaceSk(String id, int sk) throws IOException {
        final Object orDefault = this.request(DiskMirrorRequest.setSpaceSk(Integer.parseInt(id)).setSk(sk), setSpaceSk).get(Config.SECURE_KEY);
        if (orDefault instanceof Integer) {
            return (int) orDefault;
        }
        throw new UnsupportedOperationException(orDefault.toString());
    }

    @Override
    public InputStream downLoad(JSONObject jsonObject) throws IOException {
        // 开远程的数据流
        String path = this.downLoad + jsonObject.getString("userId") +
                '/' + jsonObject.getString("type") +
                "?fileName=" + jsonObject.getString("fileName") +
                "&sk=" + jsonObject.getOrDefault("secure.key", 0);
        return requestGetStream(new URL(path));
    }

    /**
     * 统一的请求发送格式
     *
     * @param jsonObject 请求参数
     * @param conPath    控制器处理服务路径
     * @return 从远程服务器获取的响应处理结果
     * @throws IOException 请求发送过程出现错误则返回此异常对象
     */
    protected JSONObject request(JSONObject jsonObject, URI conPath) throws IOException {
        return request(jsonObject, conPath, true);
    }

    /**
     * 统一的请求发送格式
     *
     * @param jsonObject 请求参数
     * @param conPath    控制器处理服务路径
     * @param check      是否检查返回结果中的okValue
     * @return 从远程服务器获取的响应处理结果
     * @throws IOException 请求发送过程出现错误则返回此异常对象
     */
    protected JSONObject request(JSONObject jsonObject, URI conPath, boolean check) throws IOException {
        this.httpPost.setURI(conPath);
        this.httpPost.setEntity(
                MultipartEntityBuilder.create()
                        .addBinaryBody("params", jsonObject.toString().getBytes(this.charset), ContentType.APPLICATION_JSON.withCharset(this.charset), "params")
                        .setContentType(ContentType.MULTIPART_FORM_DATA)
                        .build()
        );
        try (final CloseableHttpResponse execute = httpClient.execute(this.httpPost)) {
            final HttpEntity entity = execute.getEntity();
            final String string = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return getJsonObject(string, this.httpPost.getURI(), execute.getStatusLine(), check);
        }
    }

    /**
     * 统一的请求发送格式 并以数据流做返回值
     *
     * @param urlPath 数据对应的路径 的 url 对象
     * @return 从远程服务器获取的响应处理结果
     * @throws IOException 请求发送过程出现错误则返回此异常对象
     */
    protected InputStream requestGetStream(URL urlPath) throws IOException {
        try {
            return urlPath.openStream();
        } catch (IOException e) {
            throw new IOException("error:" + urlPath, e);
        }
    }

    /**
     * 设置指定空间的最大使用量
     *
     * @param spaceId 指定空间的 id
     * @param maxSize 指定空间的最大使用量
     */
    @Override
    public void setSpaceMaxSize(String spaceId, long maxSize) {
        throw unsupportedOperationException;
    }

    /**
     * 获取指定空间 id 的最大占用量，此函数的返回值是空间最大容量的字节数值。
     * <p>
     * Get the maximum usage of the specified space ID, and the return value of this function is the byte value of the maximum capacity of the space.
     * <p>
     * 此函数 需要确保远程服务器是 2024年02月17日 以及此日期以后 发布的 DiskMirrorBackEnd 包 因为此服务在旧版本中可能不存在!!!
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
        try {
            this.httpPost.setURI(new URI(this.getSpaceMaxSizeURL + "spaceId=" + id));
            return JSONObject.parseObject(EntityUtils.toString(httpClient.execute(this.httpPost).getEntity())).getLong(this.config.getString(Config.RES_KEY));
        } catch (URISyntaxException | IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public JSONObject transferDeposit(JSONObject jsonObject) throws IOException {
        return this.request(jsonObject, this.transferDeposit);
    }

    @Override
    public JSONObject transferDeposit(JSONObject jsonObject, URL url) throws IOException {
        return super.transferDeposit(DiskMirrorRequest.createFrom(jsonObject).setUrl(url.toString()));
    }

    @Override
    public JSONObject transferDepositStatus(JSONObject jsonObject) {
        try {
            return this.request(jsonObject, this.transferDepositStatus, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSpaceMaxSize(String spaceId, long maxSize, int sk) {
        throw unsupportedOperationException;
    }

    @Override
    public JSONObject getAllProgressBar(String id) {
        throw unsupportedOperationException;
    }
}
