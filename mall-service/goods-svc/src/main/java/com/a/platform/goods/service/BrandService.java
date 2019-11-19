package com.a.platform.goods.service;

import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.result.Page;
import com.a.platform.database.DaoSupport;
import com.a.platform.database.util.SqlUtil;
import com.a.platform.goods.client.BrandClient;
import com.a.platform.goods.convert.BrandConvert;
import com.a.platform.goods.enums.GoodsErrorCode;
import com.a.platform.goods.model.BrandBO;
import com.a.platform.goods.model.BrandDO;
import com.a.platform.goods.model.BrandDTO;
import com.a.platform.goods.model.SelectBO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/19 16:32
 */
@Component
@Service(validation = "true", version = "${dubbo.provider.BrandClient.version}")
public class BrandService implements BrandClient {

    @Autowired
    @Qualifier("goodsDaoSupport")
    private DaoSupport daoSupport;


    @Override
    public Page list(int page, int pageSize, String name) {

        StringBuffer sqlBuffer = new StringBuffer("select * from es_brand ");
        List<Object> term = new ArrayList<>();
        if (name != null) {
            sqlBuffer.append(" where name like ? ");
            term.add("%" + name + "%");
        }

        sqlBuffer.append(" order by brand_id desc ");

        Page webPage = this.daoSupport.queryForPage(sqlBuffer.toString(), page, pageSize, BrandDO.class, term.toArray());

        return webPage;
    }

    @Override
    @Transactional(value = "goodsTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BrandBO add(BrandDTO brand) {

        String sql = "select * from es_brand where name = ? ";
        List list = this.daoSupport.queryForList(sql, brand.getName());
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌名称重复");
        }

        brand.setDisabled(1);
        BrandDO brandDO = BrandConvert.INSTANCE.convert(brand);
        this.daoSupport.insert(brandDO);
        brandDO.setBrandId(this.daoSupport.getLastId(""));

        return BrandConvert.INSTANCE.convert(brandDO);
    }

    @Override
    public BrandBO edit(BrandDTO brand, Integer id) {
        BrandDO brandDO = this.getModelById(id);
        if (brandDO == null) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌不存在");
        }

        String sql = "select * from es_brand where name = ? and brand_id != ? ";
        List list = this.daoSupport.queryForList(sql, brand.getName(), id);
        if (list.size() > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "品牌名称重复");
        }

        brandDO = BrandConvert.INSTANCE.convert(brand);
        this.daoSupport.update(brandDO, id);
        return BrandConvert.INSTANCE.convert(brandDO);
    }

    @Override
    public void delete(Integer[] ids) {
        List term = new ArrayList<>();

        String idsStr = SqlUtil.getInSql(ids, term);

        //检测是否有分类关联
        String sql = "select count(0) from es_category_brand where brand_id in (" + idsStr + ")";
        Integer count = this.daoSupport.queryForInt(sql, term.toArray());
        if (count > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有分类关联，不能删除");
        }
        // 检测是否有商品关联
        String checkSql = "select count(0) from es_goods where brand_id in (" + idsStr + ")";
        int hasRel = this.daoSupport.queryForInt(checkSql, term.toArray());
        if (hasRel > 0) {
            throw new ServiceException(GoodsErrorCode.E302.code(), "已有商品关联，不能删除");
        }

        sql = "delete from es_brand where brand_id in (" + idsStr + ") ";

        this.daoSupport.execute(sql, term.toArray());
    }

    public BrandDO getModelById(Integer id) {
        return this.daoSupport.queryForObject(BrandDO.class, id);
    }

    @Override
    public BrandBO getModel(Integer id) {
        return BrandConvert.INSTANCE.convert(getModelById(id));
    }

    @Override
    public List<BrandBO> getBrandsByCategory(Integer categoryId) {

        String sql = "select b.brand_id,b.`name`,b.logo "
                + "from es_category_brand cb inner join es_brand b on cb.brand_id=b.brand_id "
                + "where cb.category_id=? and b.disabled = 1 ";
        List<BrandBO> list = this.daoSupport.queryForList(sql, BrandBO.class, categoryId);
        return list;

    }

    @Override
    public List<SelectBO> getCatBrand(Integer categoryId) {

        String sql = "select b.brand_id id ,b.`name` text,case category_id when ?  then true else false end selected "
                + " from es_brand b left join  es_category_brand cb on b.brand_id=cb.brand_id and category_id = ?"
                + " where b.disabled=1 ";

        return this.daoSupport.queryForList(sql, SelectBO.class, categoryId, categoryId);

    }

    @Override
    public List<BrandBO> getAllBrands() {

        String sql = "select * from es_brand order by brand_id desc ";

        return this.daoSupport.queryForList(sql, BrandBO.class);
    }
}