package com.a.platform.buyer.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 导航栏
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SiteNavigationDTO implements Serializable {

    private static final long serialVersionUID = 964913498755358L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    private Integer navigationId;
    /**
     * 导航名称
     */
    @ApiModelProperty(name = "navigation_name", value = "导航名称", required = true)
    @Size(max = 15, message = "导航名称字符长度校验，不可超过15个字符")
    @NotEmpty(message = "导航名称不能为空")
    private String navigationName;
    /**
     * 导航地址
     */
    @ApiModelProperty(name = "url", value = "导航地址", required = true)
    @Size(max = 255, message = "导航地址长度校验，不可超过255个字符")
    @NotEmpty(message = "导航地址不能为空")
    private String url;
    /**
     * 客户端类型
     */
    @ApiModelProperty(name = "client_type", value = "客户端类型 PC/MOBILE", required = true)
    private String clientType;
    /**
     * 图片
     */
    @ApiModelProperty(name = "image", value = "图片", required = false)
    private String image;
    /**
     * 排序
     */
    @ApiModelProperty(name = "sort", value = "排序", hidden = true)
    private Integer sort;


}