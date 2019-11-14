package com.a.platform.system.service;

import com.a.platform.buyer.client.HotKeywordClient;
import com.a.platform.buyer.enums.SystemErrorCode;
import com.a.platform.buyer.model.HotKeywordBO;
import com.a.platform.buyer.model.HotKeywordDTO;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.result.Page;
import com.a.platform.database.DaoSupport;
import com.a.platform.system.convert.HotKeywordConvert;
import com.a.platform.system.model.HotKeywordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 热门关键字
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:27
 */
@Service
public class HotKeywordService implements HotKeywordClient {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;

    @Override
    public Page list(int page, int pageSize) {
        String sql = "select * from es_hot_keyword  ";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, HotKeywordDO.class);
        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HotKeywordBO add(HotKeywordDTO hotKeyword) {

        HotKeywordDO keyword = HotKeywordConvert.INSTANCE.convert(hotKeyword);

        this.daoSupport.insert(keyword);
        keyword.setId(this.daoSupport.getLastId(""));

        return HotKeywordConvert.INSTANCE.convert(keyword);
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HotKeywordBO edit(HotKeywordDTO hotKeyword, Integer id) {

        HotKeywordDO keyword = this.getModelById(id);
        if (keyword == null) {
            throw new ServiceException(SystemErrorCode.E954.code(), "该记录不存在，请正确操作");
        }

        this.daoSupport.update(hotKeyword, id);

        return HotKeywordConvert.INSTANCE.convert(keyword);
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        this.daoSupport.delete(HotKeywordDO.class, id);
    }

    public HotKeywordDO getModelById(Integer id) {
        return this.daoSupport.queryForObject(HotKeywordDO.class, id);
    }

    @Override
    public HotKeywordBO getModel(Integer id) {
        return HotKeywordConvert.INSTANCE.convert(getModelById(id));
    }

    @Override
    public List<HotKeywordBO> listByNum(Integer num) {
        String sql = " select * from es_hot_keyword order by sort asc  limit 0,? ";
        List<HotKeywordDO> hotKeywordDOList = this.daoSupport.queryForList(sql, HotKeywordDO.class, num);
        if(hotKeywordDOList != null){
            return HotKeywordConvert.INSTANCE.convert(hotKeywordDOList);
        }
        return new ArrayList<>();
    }
}
