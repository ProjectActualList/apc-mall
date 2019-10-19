package com.a.platform.security.model;


/**
 * 买家角色
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:39
 */
public class Buyer extends User {


    /**
     * 定义买家的角色
     */
    public Buyer() {
        this.add(Role.BUYER);
    }


}
