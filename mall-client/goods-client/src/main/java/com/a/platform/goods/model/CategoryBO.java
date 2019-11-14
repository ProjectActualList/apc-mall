package com.a.platform.goods.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 商品分类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryBO implements Serializable {

    private static final long serialVersionUID = 1964321416223565L;

    /**
     * 主键
     */
    @ApiModelProperty(hidden = true)
    private Integer categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", required = true)
    @NotEmpty(message = "分类名称不能为空")
    private String name;

    /**
     * 分类父id
     */
    @ApiModelProperty(name = "parent_id", value = "分类父id，顶 0", required = true)
    @NotNull(message = "父分类不能为空")
    private Integer parentId;

    /**
     * 分类父子路径
     */
    @ApiModelProperty(hidden = true)
    private String categoryPath;

    /**
     * 该分类下商品数量
     */
    @ApiModelProperty(hidden = true)
    private Integer goodsCount;

    /**
     * 分类排序
     */
    @ApiModelProperty(name = "category_order", value = "分类排序", required = false)
    private Integer categoryOrder;

    /**
     * 分类图标
     */
    @ApiModelProperty(value = "分类图标", required = false)
    private String image;


}