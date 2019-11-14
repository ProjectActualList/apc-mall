package com.a.platform.goods.client;


import com.a.platform.common.result.Page;
import com.a.platform.goods.model.BrandBO;
import com.a.platform.goods.model.BrandDTO;
import com.a.platform.goods.model.SelectBO;

import java.util.List;

/**
 * 品牌
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:44
 */
public interface BrandClient {

    /**
     * 查询品牌列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param name
     * @return Page
     */
    Page list(int page, int pageSize, String name);

    /**
     * 添加品牌
     *
     * @param brand 品牌
     * @return Brand 品牌
     */
    BrandBO add(BrandDTO brand);

    /**
     * 修改品牌
     *
     * @param brand 品牌
     * @param id    品牌主键
     * @return Brand 品牌
     */
    BrandBO edit(BrandDTO brand, Integer id);

    /**
     * 删除品牌
     *
     * @param ids 品牌主键
     */
    void delete(Integer[] ids);

    /**
     * 获取品牌
     *
     * @param id 品牌主键
     * @return Brand 品牌
     */
    BrandBO getModel(Integer id);

    /**
     * 查询某分类下的品牌
     *
     * @param categoryId
     * @return
     */
    List<BrandBO> getBrandsByCategory(Integer categoryId);

    /**
     * 查询分类品牌，所有品牌，分类绑定的品牌为已选中状态
     *
     * @param categoryId
     * @return
     */
    List<SelectBO> getCatBrand(Integer categoryId);


    /**
     * 查询全部的品牌
     *
     * @return
     */
    List<BrandBO> getAllBrands();
}