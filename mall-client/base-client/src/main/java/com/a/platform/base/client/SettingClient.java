package com.a.platform.base.client;


import com.a.platform.base.model.SettingGroup;

/**
 * 系统设置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:23
 */
public interface SettingClient {
    /**
     * 系统参数配置
     *
     * @param group    系统设置的分组
     * @param settings 要保存的设置对象
     */
    void save(SettingGroup group, Object settings);

    /**
     * 获取配置
     *
     * @param group 分组名称
     * @return 存储对象
     */
    String get(SettingGroup group);


}
