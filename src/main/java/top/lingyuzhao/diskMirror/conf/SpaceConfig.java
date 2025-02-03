package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;

/**
 * 用户配置模块
 *
 * @author 赵凌宇
 */
public class SpaceConfig {

    private final Config config;
    private ConfigMapper configMapper = new HashMapper();

    /**
     * @param config 配置 当空间特有的配置中不存在的时候，则返回全局配置
     */
    public SpaceConfig(Config config) {
        this.config = config;
    }

    /**
     * 根据空间ID和key获取配置
     *
     * @param spaceId 需要被获取的配置对应的空间ID
     * @param key     需要被获取的配置对应的key
     * @return 指定的空间ID和key对应的配置
     */
    public Object getSpaceConfigByKey(String spaceId, String key) {
        JSONObject function = configMapper.function(spaceId);
        Object res = null;
        if (function != null) {
            res = function.get(key);
        }
        if (res == null) {
            return config.get(key);
        }
        return res;
    }

    /**
     * 设置空间配置
     *
     * @param spaceId 空间ID
     * @param key     配置项目的 key
     * @param value   配置项目的 value
     */
    public void setSpaceConfigByKey(String spaceId, String key, Object value) {
        JSONObject function = configMapper.function(spaceId);
        if (function == null) {
            function = new JSONObject();
            configMapper.set(spaceId, function);
        }
        function.put(key, value);
    }

    public void setConfigMapper(ConfigMapper configMapper) {
        // 将当前的mapper 拷贝到新的 mapper
        this.configMapper.copy(configMapper);
        // 将新的 mapper 赋值给当前的 mapper
        this.configMapper = configMapper;
    }

}
