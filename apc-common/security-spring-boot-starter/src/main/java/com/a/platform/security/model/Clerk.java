package com.a.platform.security.model;

import lombok.*;

/**
 * 店员
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:39
 */
@Data
@AllArgsConstructor
public class Clerk extends Seller {

    /**
     * 店员id
     */
    private Integer clerkId;
    /**
     * 店员名称
     */
    private String clerkName;
    /**
     * 是否是超级店员
     */
    private Integer founder;
    /**
     * 权限
     */
    private String role;

    /**
     * clerk有 买家的角色和卖家角色
     */
    public Clerk() {
        add(Role.CLERK);
    }

}
