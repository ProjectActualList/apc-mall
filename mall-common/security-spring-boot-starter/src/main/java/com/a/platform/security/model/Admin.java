package com.a.platform.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理员角色
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:43
 */
@Setter
@Getter
@AllArgsConstructor
public class Admin {
    /**
     * 管理员id
     */
    private Integer uid;
    /**
     * 管理员名称
     */
    private String username;
    /**
     * 是否是超级管理员
     */
    private Integer founder;
    /**
     * 权限
     */
    private String role;
    /**
     * uuid
     */
    private String uuid;

}
