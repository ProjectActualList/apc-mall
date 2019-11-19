package com.a.platform.system.service;

import com.a.platform.buyer.client.SiteNavigationClient;
import com.a.platform.buyer.enums.ClientType;
import com.a.platform.buyer.enums.SystemErrorCode;
import com.a.platform.buyer.model.SiteNavigationBO;
import com.a.platform.buyer.model.SiteNavigationDTO;
import com.a.platform.common.core.CachePrefix;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.result.Page;
import com.a.platform.common.util.StringUtil;
import com.a.platform.database.DaoSupport;
import com.a.platform.mq.rabbitmq.AmqpExchange;
import com.a.platform.redis.cache.Cache;
import com.a.platform.system.convert.SiteNavigationConvert;
import com.a.platform.system.model.SiteNavigationDO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏业务类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 15:00
 */
@Component
@Service(validation = "true", version = "${dubbo.provider.SiteNavigationClient.version}")
public class SiteNavigationService implements SiteNavigationClient {

    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private Cache cache;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Page list(int page, int pageSize, String clientType) {
        String sql = "select * from es_site_navigation  where  client_type = ? order by sort desc";
        Page webPage = this.daoSupport.queryForPage(sql, page, pageSize, SiteNavigationDO.class,clientType);
        return webPage;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SiteNavigationBO add(SiteNavigationDTO siteNavigation) {

        //移动端图片地址必填
        if(ClientType.MOBILE.name().equals(siteNavigation.getClientType())){
            if(StringUtil.isEmpty(siteNavigation.getImage())){
                throw new ServiceException(SystemErrorCode.E953.code(),"移动端导航，图片必传");
            }
        }
        //导航名称长度不能超过6
        if(siteNavigation.getNavigationName().length()>6){
            throw new ServiceException(SystemErrorCode.E953.code(),"导航栏菜单名称已经超出最大限制");
        }

        /**查询数据库sort最大值方便给新添加的数据赋值*/
        Integer sort = this.daoSupport.queryForInt("select MAX(sort) from es_site_navigation");
        siteNavigation.setSort(sort+1);

        SiteNavigationDO siteNavigationDO = SiteNavigationConvert.INSTANCE.convert(siteNavigation);
        this.daoSupport.insert(siteNavigationDO);

        int id = this.daoSupport.getLastId("");

        siteNavigationDO.setNavigationId(id);

        this.avigationChange(SiteNavigationConvert.INSTANCE.convert(siteNavigationDO));

        return SiteNavigationConvert.INSTANCE.convert(siteNavigationDO);
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SiteNavigationBO edit(SiteNavigationDTO siteNavigation, Integer id) {

        SiteNavigationDO siteNav = getModelById(id);
        if(siteNav == null){
            throw new ServiceException(SystemErrorCode.E953.code(),"导航栏不存在，请正确操作");
        }

        //移动端图片地址必填
        if(ClientType.MOBILE.name().equals(siteNavigation.getClientType())){
            if(StringUtil.isEmpty(siteNavigation.getImage())){
                throw new ServiceException(SystemErrorCode.E953.code(),"移动端导航，图片必传");
            }
        }
        //导航名称长度不能超过6
        if(siteNavigation.getNavigationName().length()>6){
            throw new ServiceException(SystemErrorCode.E953.code(),"导航栏菜单名称已经超出最大限制");
        }


        siteNavigation.setSort(siteNav.getSort());
        SiteNavigationDO  siteNavigationDO = SiteNavigationConvert.INSTANCE.convert(siteNavigation);
        SiteNavigationBO siteNavigationBO = SiteNavigationConvert.INSTANCE.convert(siteNav);

        this.avigationChange(siteNavigationBO);

        this.daoSupport.update(siteNavigationDO, id);

        return siteNavigationBO;
    }

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {

        SiteNavigationDO siteNav = getModelById(id);
        if(siteNav == null){
            throw new ServiceException(SystemErrorCode.E953.code(),"导航栏不存在，请正确操作");
        }

        this.avigationChange(SiteNavigationConvert.INSTANCE.convert(siteNav));

        this.daoSupport.delete(SiteNavigationDO.class, id);
    }

    public SiteNavigationDO getModelById(Integer id) {
        return this.daoSupport.queryForObject(SiteNavigationDO.class, id);
    }

    @Override
    public SiteNavigationBO getModel(Integer id) {
        SiteNavigationDO siteNavigationDO =  getModelById(id);
        return SiteNavigationConvert.INSTANCE.convert(siteNavigationDO);
    }



    @Override
    public SiteNavigationBO updateSort(Integer id, String sort) {

        SiteNavigationDO siteNav = getModelById(id);
        if(siteNav == null){
            throw new ServiceException(SystemErrorCode.E953.code(),"导航栏不存在，请正确操作");
        }

        Integer menuSort = siteNav.getSort();
        String sql =  "";
        /**判断是否操作是下移或者上移 up上移 否则 下移*/
        if("up".equals(sort)) {
            sql="select * from es_site_navigation where sort >? and client_type=? order by sort asc limit 1";
        }else {
            sql="select * from es_site_navigation where sort < ? and client_type= ? order by sort desc limit 1 ";
        }
        /** 当前记录的上或者下一条记录 */
        SiteNavigationDO operationSiteMenu = this.daoSupport.queryForObject(sql, SiteNavigationDO.class, siteNav.getSort(),siteNav.getClientType());
        /**如果为null 则为最顶级或者最下级*/
        if(operationSiteMenu != null) {
            Integer operMenuSort = operationSiteMenu.getSort();
            /** 改变当前记录的排序 */
            siteNav.setSort(operMenuSort);
            this.daoSupport.update(siteNav, siteNav.getNavigationId());
            operationSiteMenu.setSort(menuSort);
            this.daoSupport.update(operationSiteMenu, operationSiteMenu.getNavigationId());
        }

        this.avigationChange(SiteNavigationConvert.INSTANCE.convert(siteNav));

        return SiteNavigationConvert.INSTANCE.convert(siteNav);
    }

    @Override
    public List<SiteNavigationBO> listByClientType(String clientType) {

        List<SiteNavigationDO> list  = (List<SiteNavigationDO>)cache.get(CachePrefix.SITE_NAVIGATION.getPrefix()+clientType);

        if(list == null){
            String sql = "select * from es_site_navigation  where  client_type = ? order by sort desc";
            list = this.daoSupport.queryForList(sql,SiteNavigationDO.class,clientType);
            cache.put(CachePrefix.SITE_NAVIGATION.getPrefix()+clientType,list);
        }
        if(list != null) {
            return SiteNavigationConvert.INSTANCE.convert(list);
        }
        return new ArrayList<>();
    }

    /**
     * 导航栏变化清除缓存，发送mq消息
     * @param siteNav
     */
    private void avigationChange(SiteNavigationBO siteNav){

        this.cache.remove(CachePrefix.SITE_NAVIGATION.getPrefix()+siteNav.getClientType());

        this.amqpTemplate.convertAndSend(AmqpExchange.TEST_EXCHANGE,
                AmqpExchange.TEST_EXCHANGE + "_ROUTING",
                siteNav.getClientType());
    }


}
