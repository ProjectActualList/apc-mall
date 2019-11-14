package com.a.platform.goods.service;

import com.a.platform.common.core.CachePrefix;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.database.DaoSupport;
import com.a.platform.goods.client.CategoryClient;
import com.a.platform.goods.convert.CategoryConvert;
import com.a.platform.goods.enums.GoodsErrorCode;
import com.a.platform.goods.model.*;
import com.a.platform.mq.rabbitmq.AmqpExchange;
import com.a.platform.redis.cache.Cache;
import com.a.platform.security.MemberContext;
import com.a.platform.security.model.Seller;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0 2018-03-15 17:22:06
 */
@Component
@com.alibaba.dubbo.config.annotation.Service(validation = "true", version = "${dubbo.provider.CategoryService.version}")
public class CategoryService implements CategoryClient {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport goodsDaoSupport;

    @Autowired
    @Qualifier("tradeDaoSupport")
    private DaoSupport tradedaoSupport;

    @Autowired
    private Cache cache;

    @Autowired
    private AmqpTemplate amqpTemplate;

//    @Autowired
//    private ShopClient shopClient;

    private final String CATEGORY_CACHE_ALL = CachePrefix.GOODS_CAT.getPrefix() + "ALL";

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CategoryBO add(CategoryDTO category) {

        CategoryDO parent = null;

        // 非顶级分类
        if (category.getParentId() != null && category.getParentId() != 0) {
            parent = this.getModelById(category.getParentId());
            if (parent == null) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "父分类不存在");
            }
            // 替换catPath 根据catPath规则来匹配级别
            String catPath = parent.getCategoryPath().replace("|", ",");
            String[] str = catPath.split(",");
            // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
            if (str.length >= 4) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
            }
        }

        this.goodsDaoSupport.insert(category);
        int categoryId = this.goodsDaoSupport.getLastId("");
        category.setCategoryId(categoryId);

        String sql = "";
        // 判断是否是顶级类似别，如果parentid为空或为0则为顶级类似别
        // 注意末尾都要加|，以防止查询子孙时出错
        // 不是顶级类别，有父
        if (parent != null) {
            category.setCategoryPath(parent.getCategoryPath() + categoryId + "|");
        } else {// 是顶级类别
            category.setCategoryPath("0|" + categoryId + "|");
        }

        sql = "update es_category set  category_path=? where  category_id=?";
        this.goodsDaoSupport.execute(sql, category.getCategoryPath(), categoryId);

        cache.remove(CATEGORY_CACHE_ALL);

        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(categoryId, CategoryChangeMsg.ADD_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg);

        CategoryDO categoryDO = CategoryConvert.INSTANCE.convert(category);
        return CategoryConvert.INSTANCE.convert(categoryDO);
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CategoryBO edit(CategoryDTO category, Integer id) {

        CategoryDO parent = null;

        CategoryDO catTemp = this.getModelById(id);

        // 如果有子分类则不能更换上级分类
        // 更换上级分类
        if (!category.getParentId().equals(catTemp.getParentId())) {
            // 查看是否有子分类
            List list = this.list(id, null);
            if (list != null && list.size() > 0) {
                throw new ServiceException(GoodsErrorCode.E300.code(), "当前分类有子分类，不能更换上级分类");
            } else {
                parent = this.getModelById(category.getParentId());
                if (parent == null) {
                    throw new ServiceException(GoodsErrorCode.E300.code(), "父分类不存在");
                }
                // 替换catPath 根据catPath规则来匹配级别
                String catPath = parent.getCategoryPath().replace("|", ",");
                String[] str = catPath.split(",");
                // 如果当前的catPath length 大于4 证明当前分类级别大于五级 提示
                if (str.length >= 4) {
                    throw new ServiceException(GoodsErrorCode.E300.code(), "最多为三级分类,添加失败");
                }
                category.setCategoryPath(parent.getCategoryPath() + category.getCategoryId() + "|");
            }
        }

        this.goodsDaoSupport.update(category, id);

        cache.remove(CATEGORY_CACHE_ALL);

        CategoryChangeMsg categoryChangeMsg = new CategoryChangeMsg(id, CategoryChangeMsg.UPDATE_OPERATION);
        this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CATEGORY_CHANGE, AmqpExchange.GOODS_CATEGORY_CHANGE + "_ROUTING", categoryChangeMsg);


        CategoryDO categoryDO = CategoryConvert.INSTANCE.convert(category);
        return CategoryConvert.INSTANCE.convert(categoryDO);
    }

    @Override
    public void delete(Integer id) {

        List<CategoryBrandBO> list = this.listAllChildren(id);
        if (list != null && list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在子类别不能删除");
        }
        // 查询某商品分类的商品
        String goodsSql = "select count(0) from es_goods where category_id = ?";
        Integer count = this.goodsDaoSupport.queryForInt(goodsSql, id);

        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E300.code(), "此类别下存在商品不能删除");
        }

        // 缓存
        cache.remove(CachePrefix.GOODS_CAT.getPrefix() + id);
        cache.remove(CATEGORY_CACHE_ALL);

        this.goodsDaoSupport.delete(CategoryDO.class, id);
    }

    public CategoryDO getModelById(Integer id) {
        return this.goodsDaoSupport.queryForObject(CategoryDO.class, id);
    }

    @Override
    public CategoryBO getModel(Integer id) {
        return CategoryConvert.INSTANCE.convert(getModelById(id));
    }

    @Override
    public List list(Integer parentId, String format) {
        if (parentId == null) {
            return null;
        }
        String sql = "select c.* from es_category c  where c.parent_id = ? order by c.category_order asc";
        /*if (format != null) {
            List<CategoryPluginVO> catlist = this.goodsDaoSupport.queryForList(sql, CategoryPluginVO.class, parentId);
            return catlist;
        } else {
            List<CategoryBrandBO> catlist = this.goodsDaoSupport.queryForList(sql, CategoryBrandBO.class, parentId);

            for (CategoryBrandBO categoryVo : catlist) {
                List<CategoryBrandBO> listAllChildren = this.listChildren(categoryVo.getCategoryId());
                if (listAllChildren.size() > 0) {
                    categoryVo.setChildren(listAllChildren);
                }
            }
            return catlist;
        }*/
        return null;
    }

    /**
     * 获取某个类别的所有子类
     *
     * @param parentId
     * @return
     */
    private List<CategoryBrandBO> listChildren(Integer parentId) {
        // 从缓存取所有的分类
        List<CategoryBO> list = (List<CategoryBO>) cache.get(CATEGORY_CACHE_ALL);
        if (list == null) {
            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryBrandBO> topCatList = new ArrayList<>();

        for (CategoryBO cat : list) {
            CategoryBrandBO categoryVo = new CategoryBrandBO(cat);
            if (cat.getParentId().compareTo(parentId) == 0) {
                topCatList.add(categoryVo);
            }
        }
        return topCatList;
    }

    /**
     * 初始化分类缓存
     *
     * @return
     */
    private List<CategoryBO> initCategory() {
        List<CategoryBO> list = this.getCategoryList();
        if (list != null && list.size() > 0) {
            for (CategoryBO cat : list) {
                cache.put(CachePrefix.GOODS_CAT.getPrefix() + cat.getCategoryId(), cat);
            }
            cache.put(CATEGORY_CACHE_ALL, list);
        }
        return list;
    }

    /**
     * 查询分类列表
     *
     * @return
     */
    private List<CategoryBO> getCategoryList() {
        // 不能修改为缓存读取
        String sql = "select * from es_category order by category_order asc";
        List<CategoryDO> list = this.goodsDaoSupport.queryForList(sql, CategoryDO.class);
        return CategoryConvert.INSTANCE.convert(list);
    }

    @Override
    public List<CategoryBrandBO> listAllChildren(Integer parentid) {
        // 从缓存取所有的分类
        List<CategoryBO> list = (List<CategoryBO>) cache.get(CATEGORY_CACHE_ALL);
        if (list == null) {

            // 调用初始化分类缓存方法
            list = initCategory();
        }
        List<CategoryBrandBO> topCatList = new ArrayList<CategoryBrandBO>();

        for (CategoryBO cat : list) {
            CategoryBrandBO categoryBrandBO = new CategoryBrandBO(cat);
            if (cat.getParentId().compareTo(parentid) == 0) {
                List<CategoryBrandBO> children = this.getChildren(list, cat.getCategoryId());
                categoryBrandBO.setChildren(children);
                topCatList.add(categoryBrandBO);
            }
        }
        return topCatList;
    }


    @Override
    public List<CategoryBrandBO> listAllSellerChildren() {

        Seller seller = MemberContext.getSeller();

        /*ShopVO shop = shopClient.getShop(seller.getSellerId());

        String goodsManagementCategory = shop.getGoodsManagementCategory();*/

        StringBuffer likeSql = new StringBuffer();
        /*String[] categoryId = goodsManagementCategory.split(",");
        if (categoryId.length > 0) {
            likeSql.append("where ");
        }
        for (String category : categoryId) {
            likeSql.append(" category_path like ('0|" + category + "%') or ");
        }*/
        String like = "";
        if (likeSql.length() > 2) {
            like = likeSql.substring(0, likeSql.length() - 3);
        }

        String sql = "select * from es_category c " +
                like +
                "order by category_order";
        List<CategoryBO> list = this.goodsDaoSupport.queryForList(sql, CategoryBO.class);

        List<CategoryBrandBO> vos = new ArrayList<>();
        for (CategoryBO categoryDO : list) {
            if (categoryDO.getParentId().equals(0)) {
                vos.add(new CategoryBrandBO(categoryDO));
            }
        }
        vos = reSet(list, vos);

        return vos;
    }

    private List<CategoryBrandBO> reSet(List<CategoryBO> list, List<CategoryBrandBO> vos) {
        for (CategoryBrandBO vo : vos) {
            List<CategoryBrandBO> child = new ArrayList<>();
            for (CategoryBO cat : list) {
                if (cat.getParentId().equals(vo.getCategoryId())) {
                    CategoryBrandBO categoryVo = new CategoryBrandBO(cat);
                    child.add(categoryVo);
                }
            }
            if (child.size() > 0) {
                vo.setChildren(child);
                this.reSet(list, child);
            }
        }
        return vos;
    }

    /**
     * 得到当前分类的子孙
     *
     * @param catList  分类集合
     * @param parentid 父分类id
     * @return 带子分类的集合
     */
    private List<CategoryBrandBO> getChildren(List<CategoryBO> catList, Integer parentid) {
        List<CategoryBrandBO> children = new ArrayList<>();
        for (CategoryBO cat : catList) {
            CategoryBrandBO categoryVo = new CategoryBrandBO(cat);
            if (cat.getParentId().compareTo(parentid) == 0) {
                categoryVo.setChildren(this.getChildren(catList, cat.getCategoryId()));
                children.add(categoryVo);
            }
        }
        return children;
    }

    @Override
    public List<CategoryBO> getSellerCategory(Integer categoryId) {
        String sql = "";
        Seller seller = MemberContext.getSeller();

        /*ShopVO shop = shopClient.getShop(seller.getSellerId());

        String goodsManagementCategory = shop.getGoodsManagementCategory();
        if (categoryId == 0 && StringUtil.notEmpty(goodsManagementCategory)) {
            sql = "select * from es_category c where category_id in (" + goodsManagementCategory + ") and parent_id=? order by category_order";
            return this.goodsDaoSupport.queryForList(sql, CategoryDO.class, categoryId);
        } else {
            sql = "select * from es_category  where parent_id=?";
            return this.goodsDaoSupport.queryForList(sql, CategoryDO.class, categoryId);
        }*/
        return null;
    }

//    @Override
//    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public List<CategoryBrandDO> saveBrand(Integer categoryId, Integer[] chooseBrands) {
//
//        CategoryDO category = this.getModel(categoryId);
//        if (category == null) {
//            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
//        }
//        if(chooseBrands==null){
//            throw new ServiceException(GoodsErrorCode.E304.code(),"至少选择一个品牌");
//        }
//
//
//        //查看所选品牌是否存在
//        List<Object> term = new ArrayList<>();
//        String str = SqlUtil.getInSql(chooseBrands, term);
//        Integer count = this.goodsDaoSupport.queryForInt("select count(0) from es_brand where brand_id in(" + str + ")", term.toArray());
//
//        if (count < chooseBrands.length) {
//            throw new ServiceException(GoodsErrorCode.E300.code(), "品牌传参错误");
//        }
//        String sql = "delete from es_category_brand where category_id = ?";
//        this.goodsDaoSupport.execute(sql, categoryId);
//
//        List<CategoryBrandDO> res = new ArrayList<>();
//        for (int i = 0; i < chooseBrands.length; i++) {
//            CategoryBrandDO categoryBrand = new CategoryBrandDO(categoryId, chooseBrands[i]);
//            this.goodsDaoSupport.insert(categoryBrand);
//
//            res.add(categoryBrand);
//        }
//
//        return res;
//    }
//
//    @Override
//    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public List<CategorySpecDO> saveSpec(Integer categoryId, Integer[] chooseSpecs) {
//
//        CategoryDO category = this.getModel(categoryId);
//        if (category == null) {
//            throw new ServiceException(GoodsErrorCode.E300.code(), "该分类不存在");
//        }
//
//        if(chooseSpecs==null){
//            throw new ServiceException(GoodsErrorCode.E304.code(),"至少选择一个规格");
//        }
//
//        //查看所选规格是否存在
//        List<Object> term = new ArrayList<>();
//        String str = SqlUtil.getInSql(chooseSpecs, term);
//        Integer count = this.goodsDaoSupport.queryForInt("select count(0) from es_specification where spec_id in(" + str + ")", term.toArray());
//
//        if (count < chooseSpecs.length) {
//            throw new ServiceException(GoodsErrorCode.E300.code(), "规格绑定传参错误");
//        }
//
//        String sql = "delete from es_category_spec where category_id = ?";
//        this.goodsDaoSupport.execute(sql, categoryId);
//
//        List<CategorySpecDO> res = new ArrayList<>();
//
//        for (Integer specId : chooseSpecs) {
//            CategorySpecDO categorySpec = new CategorySpecDO(categoryId, specId);
//            this.goodsDaoSupport.insert(categorySpec);
//            res.add(categorySpec);
//        }
//        return res;
//    }

}
