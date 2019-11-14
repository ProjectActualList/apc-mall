package com.a.platform.buyer.enums;


/**
 * 客户端类型
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:04
 */
public enum ClientType {

    /**
     * pc客户端
     */
    PC,
    /**
     * MOBILE
     */
    MOBILE;

    ClientType() {

    }

    public String value() {
        return this.name();
    }


}
