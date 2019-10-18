package com.a.platform.redis.configure;

/**
 * Redis 配置类型
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:20
 */
public enum RedisConfigType {

    //手动指定
    manual,

    //通过cache cloud 的rest api来获取配置
    rest

}
