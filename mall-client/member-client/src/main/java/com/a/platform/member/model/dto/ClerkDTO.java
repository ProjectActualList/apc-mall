package com.a.platform.member.model.dto;

import com.a.platform.common.validation.annotation.Mobile;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 店员DTO
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 18:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClerkDTO implements Serializable{
    /**
     * 手机号码
     */
    @Mobile(message = "手机格式不正确")
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;
    /**
     * 权限id
     */
    @NotNull(message = "角色不能为空")
    @ApiModelProperty(name = "role_id", value = "角色id,如果是店主则传0", required = true)
    private Integer roleId;

    /**
     * 店员名称
     */
    @ApiModelProperty(value = "账号名称", required = true)
    @NotNull(message = "账号名称不能为空")
    private String uname;
    /**
     * 账号密码
     */
    @ApiModelProperty(value = "账号密码")
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;


}