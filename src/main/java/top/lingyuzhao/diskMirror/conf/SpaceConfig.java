package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import redis.clients.jedis.Jedis;
import top.lingyuzhao.utils.StrUtils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 用户配置模块
 *
 * @author 赵凌宇
 */
public class SpaceConfig implements Closeable {

    private final Config config;
    private ConfigMapper configMapper = new HashMapper();

    /**
     * @param config 配置 当空间特有的配置中不存在的时候，则返回全局配置
     */
    public SpaceConfig(Config config) {
        this.config = config;
        switch (config.getString(Config.USE_SPACE_CONFIG_MODE)) {
            case "HashMapper":
                break;
            case "JedisMapper":
                // 代表使用redis做配置映射
                String[] strings = StrUtils.splitBy(config.getString(Config.REDIS_HOST_PORT_DB), ':');
                if (strings.length != 3) {
                    throw new RuntimeException("redis配置格式错误，示例格式：127.0.0.1:6379:0  分别代表 redis主机 redis端口 redis数据库的编号");
                }
                Jedis jedis = new Jedis(strings[0], Integer.parseInt(strings[1]));
                jedis.auth(config.getString(Config.REDIS_PASSWORD));
                jedis.select(Integer.parseInt(strings[2]));
                this.configMapper = new JedisMapper(jedis);
            default:
                throw new RuntimeException("配置映射模式错误，没有找到您期望的映射器：" + config.getString(Config.USE_SPACE_CONFIG_MODE));
        }
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
        configMapper.reSave(spaceId);
    }

    public void setConfigMapper(ConfigMapper configMapper) {
        // 将当前的mapper 拷贝到新的 mapper
        this.configMapper.copy(configMapper);
        // 关掉旧 mapper
        configMapper.close();
        // 将新的 mapper 赋值给当前的 mapper
        this.configMapper = configMapper;
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        this.configMapper.close();
    }
}
