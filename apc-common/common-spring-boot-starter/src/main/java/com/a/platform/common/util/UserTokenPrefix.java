package com.a.platform.common.util;

/**
 * 描述
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 11:21
 */
public enum UserTokenPrefix {

    /**
     * 访问令牌
     */
    ACCESS_TOKEN,
    /**
     * 刷新令牌
     */
    REFRESH_TOKEN;



    public String getPrefix() {
        return this.name() + "_";
    }
}
