package com.a.platform.redis.configure;

/**
 * Redis类型
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:20
 */
public enum RedisType {

    /**
     * 集群
     */
    cluster,
    /**
     * 哨兵
     */
    sentinel,
    /**
     * 独立
     */
    standalone



}
