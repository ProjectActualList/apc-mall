package com.a.platform.base.impl;

import com.a.platform.base.client.SettingClient;
import com.a.platform.base.model.SettingGroup;
import com.a.platform.base.model.SettingsDO;
import com.a.platform.common.core.CachePrefix;
import com.a.platform.common.util.JsonUtil;
import com.a.platform.common.util.StringUtil;
import com.a.platform.database.DaoSupport;
import com.a.platform.redis.cache.Cache;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统设置接口实现
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:31
 */
@Service(validation = "true", version = "${dubbo.provider.SettingClient.version}")
@Component
public class SettingService implements SettingClient {


    @Autowired
    @Qualifier("systemDaoSupport")
    private DaoSupport systemDaoSupport;

    @Autowired
    private Cache cache;

    @Override
    @Transactional(value = "systemTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(SettingGroup group, Object settings) {
        String sql = "select cfg_value from es_settings where cfg_group = ?";
        SettingsDO settingsDO = this.systemDaoSupport.queryForObject(sql, SettingsDO.class, group.name());
        //将要保存的对象 转换成json
        String setting = JsonUtil.objectToJson(settings);
        if (settingsDO == null) {
            this.systemDaoSupport.execute("insert into es_settings set cfg_value = ?,cfg_group = ?", setting, group.name());
        } else {
            this.systemDaoSupport.execute("update es_settings set cfg_value = ? where cfg_group = ?", setting, group.name());
        }
        //清除缓存
        cache.remove(CachePrefix.SETTING.getPrefix() + group.name());
    }


    @Override
    public String get(SettingGroup group) {
        //从缓存中获取参数配置
        String setting = StringUtil.toString(cache.get(CachePrefix.SETTING.getPrefix() + group.name()), false);
        //如果没有获取到从数据库获取
        if (StringUtil.isEmpty(setting)) {
            String sql = "select * from es_settings where cfg_group = ?";
            SettingsDO settingsDO = this.systemDaoSupport.queryForObject(sql, SettingsDO.class, group.name());
            if (settingsDO == null) {
                return null;
            }
            setting = settingsDO.getCfgValue();

            this.cache.put(CachePrefix.SETTING.getPrefix() + group.name(), setting);
        }
        return setting;
    }
}
