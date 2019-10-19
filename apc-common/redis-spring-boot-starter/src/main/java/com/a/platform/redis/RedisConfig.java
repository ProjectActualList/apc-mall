package com.a.platform.redis;

import com.a.platform.redis.builder.RedisTemplateBuilder;
import com.a.platform.redis.builder.StringRedisTemplateBuilder;
import com.a.platform.redis.configure.RedisConnectionConfig;
import com.a.platform.redis.configure.RedisType;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * Redis配置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:00
 */
@SuppressWarnings("ALL")
@Configuration
public class RedisConfig {

	@Autowired
	private RedisTemplateBuilder redisTemplateBuilder;

	@Autowired
	private StringRedisTemplateBuilder stringRedisTemplateBuilder;

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {

		RedisTemplate<String,Object> redisTemplate = null;

		redisTemplate = redisTemplateBuilder.build();

		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {

		StringRedisTemplate redisTemplate = null;

		redisTemplate = stringRedisTemplateBuilder.build();

		return redisTemplate;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return (JedisConnectionFactory) redisTemplate().getConnectionFactory();
	}


	@Bean
	public RedissonClient redissonClient(JedisConnectionFactory jedisConnectionFactory, RedisConnectionConfig config) {
 		Config rconfig = null;
		String type  = config.getType();


		//独立模式
		if( RedisType.standalone.name().equals(type) ){
			rconfig = new Config();
			RedisStandaloneConfiguration standaloneConfiguration = jedisConnectionFactory.getStandaloneConfiguration();
			String host  = standaloneConfiguration.getHostName();
			int port = standaloneConfiguration.getPort();
			SingleServerConfig singleServerConfig =  rconfig.useSingleServer().setAddress("redis://" + host+":" + port);
			if(standaloneConfiguration.getPassword().isPresent()){
				String password  = new String(standaloneConfiguration.getPassword().get() );
				singleServerConfig.setPassword(password);
			}

		}


		//哨兵模式
		if( RedisType.sentinel.name().equals(type) ){
			rconfig = new Config();
			RedisSentinelConfiguration sentinelConfiguration =  jedisConnectionFactory.getSentinelConfiguration();
			String masterName  =  sentinelConfiguration.getMaster().getName();
			Set<RedisNode> nodeSet =sentinelConfiguration.getSentinels();

			SentinelServersConfig sentinelServersConfig = rconfig.useSentinelServers().setMasterName(masterName);

			for (RedisNode node : nodeSet){
				sentinelServersConfig.addSentinelAddress("redis://"+node.asString());
			}
		}

		//集群模式
		if( RedisType.cluster.name().equals(type) ){
			rconfig = new Config();
			RedisClusterConfiguration clusterConfiguration =  jedisConnectionFactory.getClusterConfiguration();
			Set<RedisNode> nodeSet = clusterConfiguration.getClusterNodes();
			ClusterServersConfig clusterServersConfig =  rconfig.useClusterServers();
			for (RedisNode node : nodeSet){
				clusterServersConfig.addNodeAddress("redis://"+node.asString());
			}
		}

		if(  rconfig == null){
			throw  new RuntimeException("错误的redis 类型，请检查 redis.type参数");
		}
		RedissonClient redisson = Redisson.create(rconfig);
		return  redisson;
	}


}
