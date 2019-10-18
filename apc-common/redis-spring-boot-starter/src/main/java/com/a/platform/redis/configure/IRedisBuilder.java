package com.a.platform.redis.configure;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
  * redis构建接口
  *
  * @param
  * @return
  * @author weixing.yang
  * @version 1.0
  * @date 2019/10/18 18:19
  */
public interface IRedisBuilder {

	/**
	 * 构建连接
	 * @param  config redis配置
	 * @return
	 */
	public  JedisConnectionFactory buildConnectionFactory(RedisConnectionConfig config);
	
	/**
	 * 类型
	 * @return
	 */
	public RedisType getType();
}
