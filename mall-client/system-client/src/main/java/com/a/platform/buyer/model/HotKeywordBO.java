package com.a.platform.buyer.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 热门关键字
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HotKeywordBO implements Serializable {

    private static final long serialVersionUID = 9828142765402249L;

    /**
     * 主键ID
     */
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 文字内容
     */
    @ApiModelProperty(name = "hot_name", value = "文字内容", required = true)
    @NotEmpty(message = "关键字不能为空")
    private String hotName;
    /**
     * 排序
     */
    @ApiModelProperty(name = "sort", value = "排序", required = false)
    private Integer sort;

}