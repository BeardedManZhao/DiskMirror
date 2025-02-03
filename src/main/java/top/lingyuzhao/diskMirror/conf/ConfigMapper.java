package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import top.lingyuzhao.utils.dataContainer.KeyValue;
import top.lingyuzhao.utils.transformation.Transformation;

/**
 * 用于将一个空间的一些配置信息获取到和存储
 *
 * @author 赵凌宇
 */
public interface ConfigMapper extends Transformation<String, JSONObject>, Iterable<KeyValue<String, JSONObject>> {

    /**
     * 设置空间配置信息
     *
     * @param spaceId 需要被设置的空间 id
     * @param value   需要被设置的值
     */
    void set(String spaceId, JSONObject value);

    /**
     * 复制一个配置信息 到另一个实现中 通常是用来切换 mapper 实现的时候需要调用
     *
     * @param configMapper 需要被复制的配置信息
     */
    default void copy(ConfigMapper configMapper) {
        for (KeyValue<String, JSONObject> keyValue : configMapper) {
            this.set(keyValue.getKey(), keyValue.getValue());
        }
    }

    /**
     * 释放资源
     */
    default void close() {

    }
}
