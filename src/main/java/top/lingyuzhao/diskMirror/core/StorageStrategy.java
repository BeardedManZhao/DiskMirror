package top.lingyuzhao.diskMirror.core;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.transformation.Transformation;

public enum StorageStrategy {


    /**
     * 轮询存储策略
     */
    polling(j -> Math.max(j.getIntValue("okSize") % j.getIntValue("adapterSize"), 0)),

    /**
     * hash存储策略 以文件名字做为 hash 值进行存储
     */
    fileNameHash(j -> Math.max(j.getString("fileName").hashCode() % j.getIntValue("adapterSize"), 0));

    private final Transformation<JSONObject, Integer> storageStrategy;

    /**
     * 构造一个存储策略枚举对象，其中需要包含一个 lambda 类。
     * <p>
     * Construct a storage policy enumeration object that needs to include a lambda class.
     *
     * @param storageStrategy lambda 的输入是当前上传的文件的元数据，以及当前适配器的已写入文件的数量(okSize) 当前适配器中所包含的子适配器的数量(adapterSize)，输出的是一个索引编号，代表的就是要存储在第几个子适配器中，此编号不应该超过 adapterSize
     *                        <p>
     *                        The input of lambda is the metadata of the currently uploaded file, as well as the number of files written to the current adapter (okSize). The number of sub adapters included in the current adapter (adapterSize) is the output, which is an index number representing the number of sub adapters to store, and this number should not exceed adapterSize
     */
    StorageStrategy(Transformation<JSONObject, Integer> storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    public Transformation<JSONObject, Integer> getStorageStrategy() {
        return storageStrategy;
    }
}
