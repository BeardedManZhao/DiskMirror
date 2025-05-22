package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.lingyuzhao.utils.StrUtils;

import java.time.Duration;

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
    public SpaceConfig(final Config config) {
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
                final JedisPoolConfig poolConfig = getJedisPoolConfig(config);
                this.configMapper = new JedisMapper(new JedisPool(poolConfig, strings[0], Integer.parseInt(strings[1]), 60000, config.getString(Config.REDIS_PASSWORD)), Integer.parseInt(strings[2]));
                break;
            default:
                throw new RuntimeException("配置映射模式错误，没有找到您期望的映射器：" + config.getString(Config.USE_SPACE_CONFIG_MODE));
        }
    }

    /**
     * 获取redis连接池的配置
     * @param config 配置对象
     * @return redis连接池的配置
     */
    private static JedisPoolConfig getJedisPoolConfig(final Config config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 设置最大空闲连接数
        poolConfig.setMaxIdle((Integer) config.getOrDefault(Config.REDIS_POOL_MAX_IDLE, 10));
        // 设置最小空闲连接数
        poolConfig.setMinIdle((Integer) config.getOrDefault(Config.REDIS_POOL_MIN_IDLE, 5));
        // 设置最大总连接数
        poolConfig.setMaxTotal((Integer) config.getOrDefault(Config.REDIS_POOL_MAX_TOTAL, 20));
        // 替代 setTimeBetweenEvictionRunsMillis(30000)
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(60)); // 每 60 秒运行一次驱逐任务
        // 替代 setMinEvictableIdleTimeMillis(60000)
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60)); // 空闲超过 60 秒的连接可被驱逐
        // 启用空闲连接检查
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        return poolConfig;
    }

    /**
     * 根据空间ID和key获取配置
     *
     * @param spaceId 需要被获取的配置对应的空间ID
     * @param key     需要被获取的配置对应的key
     * @return 指定的空间ID和key对应的配置
     */
    public Object getSpaceConfigByKey(final String spaceId, final String key) {
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
    public void setSpaceConfigByKey(final String spaceId, final String key, final Object value) {
        JSONObject function = configMapper.function(spaceId);
        if (function == null) {
            function = new JSONObject();
            configMapper.set(spaceId, function);
        }
        function.put(key, value);
        configMapper.reSave(spaceId);
    }

    /**
     * @return 当前的配置映射器
     */
    public ConfigMapper getConfigMapper() {
        return configMapper;
    }

    /**
     * 设置配置映射器 您可以将一个新的配置映射器赋值给当前的配置映射器
     *
     * @param configMapper 新的配置映射器
     */
    public void setConfigMapper(final ConfigMapper configMapper) {
        // 将当前的mapper数据 拷贝到新的 mapper
        this.configMapper.copyTo(configMapper);
        // 关掉旧 mapper
        this.configMapper.close();
        // 将新的 mapper 赋值给当前的 mapper
        this.configMapper = configMapper;
    }
}
