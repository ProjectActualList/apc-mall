package com.a.platform.user.model;

import com.a.platform.common.model.Region;
import com.a.platform.common.model.RegionFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 会员参数传递
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-30 14:27:48
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberQueryParam {
    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(name = "uname", value = "会员登陆用户名")
    private String uname;
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱")
    private String email;
    /**
     * 会员手机号码
     */
    @ApiModelProperty(name = "mobile", value = "会员手机号码")
    private String mobile;
    /**
     * 会员性别
     */
    @ApiModelProperty(name = "sex", value = "会员性别,1为男，0为女")
    private Integer sex;

    @RegionFormat
    @ApiModelProperty(name = "region", value = "地区")
    private Region region;
    /**
     * 开始时间
     */
    @ApiModelProperty(name = "start_time", value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(name = "end_time", value = "结束时间")
    private String endTime;
    /**
     * 关键字
     */
    @ApiModelProperty(name = "keyword", value = "关键字")
    private String keyword;
    /**
     * 页码
     */
    @ApiModelProperty(name = "page_no", value = "页码", required = true)
    private Integer pageNo;

    /**
     * 会员状态
     */
    @ApiModelProperty(name = "disabled", value = "会员状态,0为正常.-1为删除，在会员回收站中", required = false)
    private Integer disabled;

    /**
     * 分页数
     */
    @ApiModelProperty(name = "page_size", value = "分页数", required = true)
    private Integer pageSize;


}