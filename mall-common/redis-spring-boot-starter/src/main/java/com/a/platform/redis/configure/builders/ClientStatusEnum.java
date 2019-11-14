package com.a.platform.redis.configure.builders;

/**
 * 客户端口状态类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:14
 */
public enum ClientStatusEnum {
    /**
     * 正确
     */
    GOOD(1),
    /**
     * 警告
     */
    WARN(0),
    /**
     * 错误
     */
    ERROR(-1);

    int status;

    ClientStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}