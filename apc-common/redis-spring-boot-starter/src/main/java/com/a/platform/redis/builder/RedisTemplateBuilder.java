package com.a.platform.redis.builder;


import com.a.platform.redis.configure.IRedisBuilder;
import com.a.platform.redis.configure.JedisSetting;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.exception.SystemErrorCode;
import com.a.platform.redis.configure.RedisConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * redisTemplateBuilder 待优化
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:13
 */
@Component
public class RedisTemplateBuilder {
	private static Logger logger = LoggerFactory.getLogger(RedisTemplateBuilder.class);

	@Autowired
	private List<IRedisBuilder> redisBuilder;

	@Autowired
	private RedisConnectionConfig config;

	/**
	 * 构建锁
	 */
	private static final Lock LOCK = new ReentrantLock();

	public RedisTemplate<String, Object> build() {


		JedisSetting.loadPoolConfig(config);

		RedisTemplate<String, Object> redisTemplate = null;

		while (true) {
			try {
				LOCK.tryLock(10, TimeUnit.MILLISECONDS);
				if (redisTemplate == null) {

					IRedisBuilder redisBuilder = this.getRedisBuilder();
					JedisConnectionFactory jedisConnectionFactory = redisBuilder.buildConnectionFactory(config );

					// 初始化连接pool
					jedisConnectionFactory.afterPropertiesSet();
					redisTemplate = new RedisTemplate<String, Object>();
					redisTemplate.setConnectionFactory(jedisConnectionFactory);
					redisTemplate.setKeySerializer(new StringRedisSerializer());
					redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
					redisTemplate.setEnableTransactionSupport(false);
					return redisTemplate;
				}
			} catch (Throwable e) {
				// 容错
				logger.error(e.getMessage(), e);

				break;
			} finally {
				LOCK.unlock();
			}
			try {
				// 活锁
				TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return redisTemplate;
	}

	private IRedisBuilder getRedisBuilder() {
		for (IRedisBuilder builder : redisBuilder) {
			if (builder.getType().name().equals(config.getType())) {
				return builder;
			}
		}
		throw new ServiceException(SystemErrorCode.INVALID_CONFIG_PARAMETER, "错误的redis 配置类型，请检查");
	}

}
