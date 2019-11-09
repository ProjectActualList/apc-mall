package com.a.platform.member.model;

import com.a.platform.database.annotation.Column;
import com.a.platform.database.annotation.Id;
import com.a.platform.database.annotation.PrimaryKeyField;
import com.a.platform.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 店员实体
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-08-04 18:48:39
 */
@Table(name = "es_clerk")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ClerkDO implements Serializable {

    private static final long serialVersionUID = 3180740947538166L;

    /**
     * 店员id
     */
    @Id(name = "clerk_id")
    @ApiModelProperty(hidden = true)
    private Integer clerkId;
    /**
     * 会员id
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员id", required = false)
    private Integer memberId;
    /**
     * 店员名称
     */
    @Column(name = "clerk_name")
    @ApiModelProperty(name = "clerk_name", value = "店员名称", required = false)
    private String clerkName;
    /**
     * 是否为超级管理员，1为超级管理员 0为其他管理员
     */
    @Column(name = "founder")
    @ApiModelProperty(name = "founder", value = "是否为超级管理员，1为超级管理员 0为普通管理员", required = false)
    private Integer founder;
    /**
     * 权限id
     */
    @Column(name = "role_id")
    @ApiModelProperty(name = "role_id", value = "权限id", required = false)
    private Integer roleId;
    /**
     * 店员状态，-1为禁用，0为正常
     */
    @Column(name = "user_state")
    @ApiModelProperty(name = "user_state", value = "店员状态，-1为禁用，0为正常", required = false)
    private Integer userState;
    /**
     * 创建日期
     */
    @Column(name = "create_time")
    @ApiModelProperty(name = "create_time", value = "创建日期", required = false)
    private Long createTime;
    /**
     * 店铺id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(name = "shop_id", value = "店铺id", required = false)
    private Integer shopId;

    @PrimaryKeyField
    public Integer getClerkId() {
        return clerkId;
    }

    public void setClerkId(Integer clerkId) {
        this.clerkId = clerkId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public Integer getFounder() {
        return founder;
    }

    public void setFounder(Integer founder) {
        this.founder = founder;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClerkDO that = (ClerkDO) o;
        if (clerkId != null ? !clerkId.equals(that.clerkId) : that.clerkId != null) {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) {
            return false;
        }
        if (clerkName != null ? !clerkName.equals(that.clerkName) : that.clerkName != null) {
            return false;
        }
        if (founder != null ? !founder.equals(that.founder) : that.founder != null) {
            return false;
        }
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }
        if (userState != null ? !userState.equals(that.userState) : that.userState != null) {
            return false;
        }
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
            return false;
        }
        return shopId != null ? shopId.equals(that.shopId) : that.shopId == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (clerkId != null ? clerkId.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (clerkName != null ? clerkName.hashCode() : 0);
        result = 31 * result + (founder != null ? founder.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (userState != null ? userState.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "clerkId=" + clerkId +
                ", memberId=" + memberId +
                ", clerkName='" + clerkName + '\'' +
                ", founder=" + founder +
                ", roleId=" + roleId +
                ", userState=" + userState +
                ", createTime=" + createTime +
                ", shopId=" + shopId +
                '}';
    }

}