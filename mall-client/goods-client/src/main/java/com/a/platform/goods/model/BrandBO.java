package com.a.platform.goods.model;

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
 * 品牌
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:45
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrandBO implements Serializable {

    private static final long serialVersionUID = 9122931201151887L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    private Integer brandId;
    /**
     * 品牌名称
     */
    @NotEmpty(message = "品牌名称不能为空")
    @ApiModelProperty(value = "品牌名称", required = true)
    private String name;
    /**
     * 品牌图标
     */
    @NotEmpty(message = "品牌图标不能为空")
    @ApiModelProperty(value = "品牌图标", required = true)
    private String logo;
    /**
     * 是否删除，0删除1未删除
     */
    @ApiModelProperty(hidden = true)
    private Integer disabled;


}