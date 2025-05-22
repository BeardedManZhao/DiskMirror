package top.lingyuzhao.diskMirror.conf;

import com.alibaba.fastjson2.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.lingyuzhao.utils.CacheUtils;
import top.lingyuzhao.utils.dataContainer.KeyValue;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;

/**
 * Redis 配置映射器
 *
 * @author 赵凌宇
 */
public class JedisMapper implements ConfigMapper {

    private final static String KEY = "DiskMirror.SpaceConfig";
    private final JedisPool jedisPool;
    private final int dbNum;

    /**
     * 缓存工具 设置有效期为 60 分钟
     */
    private final CacheUtils cacheUtils = CacheUtils.getCacheUtils("RedisMapper", 60 * 60 * 1000);


    public JedisMapper(JedisPool jedisPool, int dbNum) {
        this.jedisPool = jedisPool;
        this.dbNum = dbNum;
    }

    /**
     *
     * @return 从连接池中获取到的一个 Redis 连接
     */
    public Jedis getJedis() {
        final Jedis resource = jedisPool.getResource();
        resource.select(dbNum);
        return resource;
    }

    /**
     * 设置空间配置信息
     *
     * @param spaceId 需要被设置的空间 id
     * @param value   需要被设置的值
     */
    @Override
    public void set(String spaceId, JSONObject value) {
        try (final Jedis jedis = getJedis()) {
            jedis.hset(KEY, spaceId, value.toString());
            this.cacheUtils.put(spaceId, value);
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    @Nonnull
    public Iterator<KeyValue<String, JSONObject>> iterator() {
        return new RedisIterator();
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
        // 先看缓存中有没有
        JSONObject cache = (JSONObject) cacheUtils.get(s);
        if (cache != null) {
            return cache;
        }
        // 这里就需要从 redis 获取了
        try (final Jedis jedis = getJedis()){
            final String hGet = jedis.hget(KEY, s);
            if (hGet == null) {
                return null;
            }
            // 将从 redis 中获取的 jsonObject 放入缓存中 并返回出去
            final JSONObject jsonObject = JSONObject.parseObject(hGet);
            this.cacheUtils.put(s, jsonObject);
            return jsonObject;
        }
    }

    @Override
    public void reSave(String spaceId) {
        final Object o = this.cacheUtils.get(spaceId);
        if (o != null) {
            try (final Jedis jedis = getJedis()) {
                jedis.hset(KEY, spaceId, o.toString());
            }
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        jedisPool.close();
        ConfigMapper.super.close();
    }

    private final class RedisIterator implements Iterator<KeyValue<String, JSONObject>> {
        private final Map<String, String> map;
        private final Iterator<String> keys;

        private RedisIterator() {
            try (final Jedis jedis = getJedis()) {
                this.map = jedis.hgetAll(KEY);
                this.keys = map.keySet().iterator();
            }
        }

        @Override
        public boolean hasNext() {
            return keys.hasNext();
        }

        @Override
        public KeyValue<String, JSONObject> next() {
            final String next = keys.next();
            return new KeyValue<>(next, JSONObject.parseObject(map.get(next)));
        }
    }
}
