package com.a.platform.member.model.bo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 店员实体
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 18:33
 */
public class ClerkBO implements Serializable {
    /**
     * 店员id
     */
    @ApiModelProperty(value = "店员id")
    private Integer clerkId;
    /**
     * 店员名称
     */
    @ApiModelProperty(value = "账号名称")
    private String uname;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private String role;
    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限id")
    private Integer roleId;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private Integer memberId;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Integer shopId;
    /**
     * 角色标识
     */
    @ApiModelProperty(value = "1:超级店员 0:普通店员")
    private Integer founder;

    /**
     * 店员状态，-1为禁用，1为正常
     */
    @ApiModelProperty(name = "user_state", value = "店员状态，-1为禁用，0为正常")
    private Integer userState;

    public Integer getClerkId() {
        return clerkId;
    }

    public void setClerkId(Integer clerkId) {
        this.clerkId = clerkId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getFounder() {
        return founder;
    }

    public void setFounder(Integer founder) {
        this.founder = founder;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        return "ClerkShowVO{" +
                "clerkId=" + clerkId +
                ", uname='" + uname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", role='" + role + '\'' +
                ", roleId=" + roleId +
                ", email='" + email + '\'' +
                ", memberId=" + memberId +
                ", shopId=" + shopId +
                ", founder=" + founder +
                ", userState=" + userState +
                '}';
    }
}