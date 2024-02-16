package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
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
    private final UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException("DiskMirrorHttpAdapter 不支持此函数操作!!! 这是因为 DiskMirror 中暂时没有对应的服务!!");

    private final CloseableHttpClient httpClient;
    private final HttpPost httpPost;
    private final URI upload, remove, getUrls, mkdirs, reName, version;

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
        } catch (URISyntaxException e) {
            throw new UnsupportedOperationException("error url => " + url, e);
        }
    }

    /**
     * 递归删除一个目录 并将删除的字节数值返回
     *
     * @param path 需要被删除的文件目录
     * @return 被删除的所有文件所展示用的空间字节数
     */
    @Override
    public long rDelete(String path) {
        throw unsupportedOperationException;
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
     */
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
    protected long pathProcessorUseSize(String path, JSONObject inJson) {
        throw unsupportedOperationException;
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
                            .addTextBody("params", jsonObject.toString())
                            .addBinaryBody("file", inputStream)
                            .setContentType(ContentType.MULTIPART_FORM_DATA)
                            .build()
            );
            final HttpEntity entity = httpClient.execute(this.httpPost).getEntity();
            final String string = EntityUtils.toString(entity);
            IOUtils.close(inputStream);
            EntityUtils.consume(entity);
            return JSONObject.parseObject(string);
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

    /**
     * 统一的请求发送格式
     *
     * @param jsonObject 请求参数
     * @param conPath    控制器处理服务路径
     * @return 从远程服务器获取的响应处理结果
     * @throws IOException 请求发送过程出现错误则返回此异常对象
     */
    protected JSONObject request(JSONObject jsonObject, URI conPath) throws IOException {
        this.httpPost.setURI(conPath);
        this.httpPost.setEntity(
                MultipartEntityBuilder.create()
                        .addTextBody("params", jsonObject.toString())
                        .setContentType(ContentType.MULTIPART_FORM_DATA)
                        .build()
        );
        final HttpEntity entity = httpClient.execute(this.httpPost).getEntity();
        final String string = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return JSONObject.parseObject(string);
    }
}
