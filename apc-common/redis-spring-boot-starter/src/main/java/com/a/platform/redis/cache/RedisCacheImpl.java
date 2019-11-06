package com.a.platform.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis的cache实现
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:14
 */
@Component
public class RedisCacheImpl implements Cache {

    @Autowired
    private RedisTemplate redisTemplate;

    public RedisCacheImpl() {

    }

    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }


    @Override
    public Long getNo(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        //初始设置过期时间
        if (null == increment || increment.longValue() == 0) {
            entityIdCounter.expire(3600 * 24, TimeUnit.SECONDS);
        }
        return increment;
    }

    public boolean set(String key, String value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    @Override
    public void put(Object key, Object value) {

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(Object key, Object value, int exp) {

        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
    }

    @Override
    public void remove(Object key) {

        redisTemplate.delete(key);
    }

    /**
     * 删除
     *
     * @param key
     */
    @Override
    public void vagueDel(Object key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
    }

    @Override
    public void clear() {

        Set keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    @Override
    public void putHash(Object key, Object hashKey, Object hashValue) {
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    @Override
    public void putAllHash(Object key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public Object getHash(Object key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<Object, Object> getHash(Object key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

}
