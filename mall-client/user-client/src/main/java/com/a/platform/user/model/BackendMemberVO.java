package com.a.platform.user.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 后台首页会员vo
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:43
 */
@Getter
@Setter
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BackendMemberVO implements Serializable {

    /**
     * 昵称
     */
    @ApiModelProperty(name = "nickname", value = "昵称", required = false)
    private String nickname;
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱", required = false)
    private String email;
    /**
     * 会员注册时间
     */
    @ApiModelProperty(name = "createTime", value = "会员注册时间", required = false)
    private Long createTime;

    /**
     * 手机号码
     */
    @ApiModelProperty(name = "mobile", value = "手机号码", required = true)
    private String mobile;

}