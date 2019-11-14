package com.a.platform.goods.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品分类vo
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018年3月16日 下午4:53:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CategoryBrandBO extends CategoryBO {

    private static final long serialVersionUID = 3843585201476087204L;


    @ApiModelProperty("子分类列表")
    private List<CategoryBrandBO> children;

    @ApiModelProperty("分类关联的品牌列表")
    private List<BrandBO> brandList;


    public CategoryBrandBO(CategoryBO cat) {
        this.setCategoryId(cat.getCategoryId());
        this.setCategoryPath(cat.getCategoryPath());
        this.setName(cat.getName());
        this.setParentId(cat.getParentId());
        this.setImage(cat.getImage());
    }

}
