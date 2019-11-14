package com.a.platform.goods.client;

import com.a.platform.goods.model.CategoryBO;
import com.a.platform.goods.model.CategoryBrandBO;
import com.a.platform.goods.model.CategoryDTO;

import java.util.List;

/**
 * 商品分类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:42
 */
public interface CategoryClient {

    /**
     * 添加商品分类
     *
     * @param category
     *            商品分类
     * @return Category 商品分类
     */
    CategoryBO add(CategoryDTO category);

    /**
     * 修改商品分类
     *
     * @param category
     *            商品分类
     * @param id
     *            商品分类主键
     * @return Category 商品分类
     */
    CategoryBO edit(CategoryDTO category, Integer id);

    /**
     * 删除商品分类
     *
     * @param id
     *            商品分类主键
     */
    void delete(Integer id);

    /**
     * 获取商品分类
     *
     * @param id
     *            商品分类主键
     * @return Category 商品分类
     */
    CategoryBO getModel(Integer id);

    /**
     * 查询某分类下的子分类
     *
     * @param parentId
     * @param format
     * @return
     */
    List list(Integer parentId, String format);

    /**
     * 获取卖家经营类目对应的分类
     *
     * @param categoryId
     * @return
     */
    List<CategoryBO> getSellerCategory(Integer categoryId);

    /**
     * 查询所有的分类，父子关系
     *
     * @param parentId
     * @return
     */
    List<CategoryBrandBO> listAllChildren(Integer parentId);


    /**
     * 查询店铺
     *
     * @return
     */
    List<CategoryBrandBO> listAllSellerChildren();

    /**
     * 保存分类绑定的品牌
     *
     * @param categoryId
     * @param chooseBrands
     * @return
     */
    //List<CategoryBrandDO> saveBrand(Integer categoryId, Integer[] chooseBrands);

    /**
     * 保存分类绑定的规格
     *
     * @param categoryId
     * @param chooseSpecs
     * @return
     */
    //List<CategorySpecDO> saveSpec(Integer categoryId, Integer[] chooseSpecs);
}
