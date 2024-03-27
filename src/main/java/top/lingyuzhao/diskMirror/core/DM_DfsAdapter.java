package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.diskMirror.conf.Config;
import top.lingyuzhao.utils.JsonUtils;
import top.lingyuzhao.utils.StrUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * DM-DFS 分布式集群存储适配器，此适配器可以实现将多个子适配器统一管理，实现多个适配器一起进行数据存储的效果！
 * <p>
 * DM-DFS distributed storage adapter, this adapter can achieve unified management of multiple sub adapters, achieving the effect of multiple adapters storing data together!
 *
 * @author zhao
 */
public class DM_DfsAdapter extends FSAdapter {

    /**
     * 有一些操作是 DM_DfsAdapter 版本不适用的 遇到这类函数就会在这里直接抛出错误
     * <p>
     * There are some operations that are not applicable to the backend server version. When encountering such functions, an error will be directly thrown here
     */
    private final static UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException("DM_DfsAdapter 不支持此函数操作!!! 您不需要调用这些底层函数，上层已经实现好啦!!");
    /**
     * 准备一个 Hash Map 存储文件的元数据信息
     */
    protected final HashMap<String, Adapter> META_MAP = new HashMap<>();
    /**
     * 所有被扫描过的空间记录，在这里被记录的所有空间，将不会重复狗构建元数据
     */
    protected final HashSet<String> SCAN_OK_MAP = new HashSet<>();
    /**
     * 准备一个 ArrayList 存储所有的 Adapter 对象
     */
    protected final ArrayList<Adapter> ADAPTER_LIST = new ArrayList<>();
    /**
     * 当前适配器对象中要使用的存储策略，默认是轮询
     */
    protected StorageStrategy storageStrategy;

    /**
     * 当前此适配器已经存储的文件的数量！
     */
    protected int now_okSize = 0;

    public DM_DfsAdapter(Config config) {
        super(config);
        // 计算出所有子节点的适配器对象
        final String string1 = config.get(Config.FS_DEFAULT_FS).toString();
        final String[] strings = StrUtils.splitBy(string1, ',');
        for (String string : strings) {
            // 初始化所有的配置对象 赋予子节点的配置
            config.put(Config.FS_DEFAULT_FS, string);
            // 创建子节点的适配器对象
            final Adapter adapter = DiskMirror.DiskMirrorHttpAdapter.getAdapter(new Config(config));
            // 进行存储
            this.appendAdapter(adapter);
        }
        config.put(Config.FS_DEFAULT_FS, string1);
        // 设置默认存储策略
        this.setStorageStrategy(StorageStrategy.polling);
    }

    /**
     * 新追加一个适配器到适配器列表中，此操作能够允许您将一个新的适配器对象提供给 DM-DFS 进行管理！
     * <p>
     * Add a new adapter to the adapter list, which allows you to provide a new adapter object to DM-DFS for management!
     *
     * @param adapter 适配器对象
     *                <p>
     *                Adaptor object
     */
    public void appendAdapter(Adapter adapter) {
        ADAPTER_LIST.add(adapter);
    }

    /**
     * @param storageStrategy 新的存储策略，我们允许您在使用中的时候调用此函数来实现存储策略的修改！
     *                        <p>
     *                        New storage strategy, we allow you to call this function while in use to modify the storage strategy!
     */
    public void setStorageStrategy(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    @Override
    protected JSONObject pathProcessorUpload(String path, String path_res, JSONObject inJson, InputStream inputStream) {
        throw unsupportedOperationException;
    }

    @Override
    protected JSONObject pathProcessorGetUrls(String path, String path_res, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected JSONObject pathProcessorMkdirs(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected InputStream pathProcessorDownLoad(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected JSONObject pathProcessorRemove(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected JSONObject pathProcessorReName(String path, JSONObject inJson) {
        throw unsupportedOperationException;
    }

    @Override
    protected long pathProcessorUseSize(String path, JSONObject inJson) throws IOException {
        long useSize = 0;
        // 将这里所有适配器的 useSize 进行计算
        for (Adapter adapter : this.ADAPTER_LIST) {
            useSize += adapter.getUseSize(inJson);
        }
        return useSize;
    }

    @Override
    public JSONObject upload(InputStream inputStream, JSONObject jsonObject) throws IOException {
        // 计算出 okSize(已存储文件的数量) 和 adapterSize(所有适配器的数量)
        jsonObject.put("okSize", now_okSize);
        jsonObject.put("adapterSize", this.ADAPTER_LIST.size());
        // 使用存储策略计算出当前文件要存储的节点
        final Adapter adapter;
        final Integer function = this.storageStrategy.getStorageStrategy().function(jsonObject);
        try {
            adapter = this.ADAPTER_LIST.get(function);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new UnsupportedEncodingException("Storage strategy [" + this.storageStrategy + "] is incorrect, its returned value cannot be greater than the adapter list! error index = " + function);
        }
        final int available = inputStream.available();
        final JSONObject upload = adapter.upload(inputStream, jsonObject);
        CorrectUseSize(upload, true, false, available);
        // 记录文件所在的适配器索引
        META_MAP.put(jsonObject.getString("fileName"), adapter);
        ++now_okSize;
        return upload;
    }

    @Override
    public InputStream downLoad(JSONObject jsonObject) throws IOException {
        // 获取到文件所在适配器对象 并直接进行下载
        final Adapter adapter = checkFileIsExist(jsonObject);
        if (adapter == null) {
            throw new FileNotFoundException("File [" + jsonObject.getString("fileName") + "] is not exist!");
        }
        return adapter.downLoad(jsonObject);
    }

    /***
     * 检查要获取的文件是否已经存在，如果存在，则返回此文件对应的适配器对象 如果不存在，就返回 null;
     * <p>
     * Check if the file to be retrieved already exists. If it exists, return the adapter object corresponding to this file. If it does not exist, return null;
     * @param jsonObject 文件相关的数据 其中必须具有 fileName 字段
     *
     *                   The data related to the jsonObject file must have a fileName field
     */
    protected Adapter checkFileIsExist(JSONObject jsonObject) throws IOException {
        String string = (String) jsonObject.remove("fileName");
        if (string.charAt(0) != '/') {
            string = '/' + string;
        }
        // 首先检查当前的空间是否被扫描过
        final String s = jsonObject.getIntValue("userId") + jsonObject.getString("type");
        if (!SCAN_OK_MAP.contains(s)) {
            // 没被扫描过就先扫描
            for (Adapter adapter : this.ADAPTER_LIST) {
                try {
                    adapter.getFilesPath(jsonObject, p -> META_MAP.put(p, adapter));
                } catch (JSONException ignored) {
                }
            }
            // 扫描结束 做标记
            SCAN_OK_MAP.add(s);
        }
        // 计算文件是否存在
        jsonObject.put("fileName", string);
        return META_MAP.get(string);
    }

    @Override
    public JSONObject remove(JSONObject jsonObject) throws IOException {
        // 获取到文件所在适配器对象
        final Adapter adapter = checkFileIsExist(jsonObject);
        if (adapter == null) {
            throw new FileNotFoundException("File [" + jsonObject.getString("fileName") + "] is not exist!");
        }
        // 删除并矫正
        final JSONObject remove = adapter.remove(jsonObject);
        CorrectUseSize(remove, true, true, this.getUseSize(jsonObject) - remove.getLong("useSize"));
        // 删除元数据
        META_MAP.remove(jsonObject.getString("fileName"));
        return remove;
    }

    @Override
    public JSONObject reName(JSONObject jsonObject) throws IOException {
        // 获取到文件所在适配器对象
        final Adapter adapter = checkFileIsExist(jsonObject);
        if (adapter == null) {
            throw new FileNotFoundException("File [" + jsonObject.getString("fileName") + "] is not exist!");
        }
        final JSONObject jsonObject1 = adapter.reName(jsonObject);
        // 进行矫正 这里不需要进行修改 只需要获取
        CorrectUseSize(jsonObject1, false, false, 0);
        // 元数据标记
        META_MAP.put(jsonObject.getString("newName"), META_MAP.remove(jsonObject.getString("fileName")));
        return jsonObject1;
    }

    @Override
    public JSONObject getUrls(JSONObject jsonObject) throws IOException {
        // 获取到当前的文件目录结构
        for (Adapter adapter : this.ADAPTER_LIST) {
            // 合并结构
            try {
                JsonUtils.mergeJsonTrees(jsonObject, adapter.getUrls(jsonObject), jsonObject);
            } catch (JSONException ignored) {
            }
        }
        // 进行矫正 这里不需要进行修改 只需要获取
        CorrectUseSize(jsonObject, false, false, 0);
        return jsonObject;
    }

    @Override
    public JSONObject mkdirs(JSONObject jsonObject) throws IOException {
        // 使用存储策略计算出当前文件目录要存储的节点
        final Adapter adapter = this.ADAPTER_LIST.get(this.storageStrategy.getStorageStrategy().function(jsonObject));
        final JSONObject jsonObject1 = adapter.mkdirs(jsonObject);
        // 进行矫正 这里不需要进行修改 只需要获取
        CorrectUseSize(jsonObject1, false, false, 0);
        // 元数据标记
        META_MAP.put(jsonObject.getString("fileName"), adapter);
        return jsonObject1;
    }

    /**
     * 矫正使用大小 因为每个子适配器计算之后会返回一个数值，这个数值并不一定是正确的，只能说是站在 子适配器的角度来看是正确的，因此我们需要一个累加的矫正操作！
     *
     * @param jsonObject 输入的 jsonObject
     * @param isUpdate   使用大小是否发生了变化
     * @param isDiff     变化操作是否是做差
     * @param updateSize 变化的数值
     * @throws IOException 矫正操作出现异常的时候抛出！
     */
    protected void CorrectUseSize(JSONObject jsonObject, boolean isUpdate, boolean isDiff, long updateSize) throws IOException {
        final String string = jsonObject.getString(this.config.getString(Config.RES_KEY));
        if (!string.equals(this.config.getString(Config.OK_VALUE))) {
            throw new IOException(string);
        }
        if (isUpdate) {
            jsonObject.put("useSize", isDiff ?
                    this.diffUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), updateSize) :
                    this.addUseSize(jsonObject.getIntValue("userId"), jsonObject.getString("type"), updateSize));
            return;
        }
        jsonObject.put("useSize", this.getUseSize(jsonObject));
    }
}
