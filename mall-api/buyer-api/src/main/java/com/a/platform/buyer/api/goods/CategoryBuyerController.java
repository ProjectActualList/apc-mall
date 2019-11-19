package com.a.platform.buyer.api.goods;

import com.a.platform.goods.client.BrandClient;
import com.a.platform.goods.client.CategoryClient;
import com.a.platform.goods.model.BrandBO;
import com.a.platform.goods.model.CategoryBrandBO;
import com.alibaba.dubbo.config.annotation.Reference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品分类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:36
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/goods/categories")
@Api(description = "商品分类相关API")
public class CategoryBuyerController {

    @Reference(validation = "true", version = "${dubbo.consumer.CategoryClient.version:1.0}")
    private CategoryClient categoryClient;

    @Reference(validation = "true", version = "${dubbo.consumer.BrandClient.version:1.0}")
    private BrandClient brandClient;

    @ApiOperation(value = "首页等商品分类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parent_id", value = "分类id，顶级为0", required = true, dataType = "int", paramType = "path"),})
    @GetMapping(value = "/{parent_id}/children")
    public List<CategoryBrandBO> list(@PathVariable("parent_id") Integer parentId) {
        List<CategoryBrandBO> catTree = categoryClient.listAllChildren(parentId);

        for (CategoryBrandBO cat : catTree) {
            // 一级分类显示关联的品牌
            if (cat.getParentId().compareTo(parentId) == 0) {

                List<BrandBO> brandList = brandClient.getBrandsByCategory(cat.getCategoryId());

                cat.setBrandList(brandList);
            }
        }
        if (catTree != null) {
            return catTree;
        }
        return new ArrayList<>();
    }
}