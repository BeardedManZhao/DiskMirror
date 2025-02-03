package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.dataContainer.KeyValue;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;

/**
 * HashMapper 使用内存记录空间配置信息
 *
 * @author 赵凌宇
 */
public class HashMapper implements ConfigMapper {

    private final HashMap<String, JSONObject> hashMap = new HashMap<>();

    /**
     * 设置空间配置信息
     *
     * @param spaceId 需要被设置的空间 id
     * @param value   需要被设置的值
     */
    @Override
    public void set(String spaceId, JSONObject value) {
        this.hashMap.put(spaceId, value);
    }

    /**
     * 、
     *
     * @param s 来自内部的待转换数据。
     *          Data to be converted from inside.
     * @return 转换之后的数据。
     * <p>
     * Data after conversion.
     */
    @Override
    public JSONObject function(String s) {
        return hashMap.get(s);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    @Nonnull
    public Iterator<KeyValue<String, JSONObject>> iterator() {
        return this.hashMap.keySet().stream().map(key -> new KeyValue<>(key, hashMap.get(key))).iterator();
    }
}
