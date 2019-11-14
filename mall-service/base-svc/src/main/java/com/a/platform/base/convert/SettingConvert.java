package com.a.platform.base.convert;

import com.a.platform.base.model.SettingsDO;
import com.a.platform.base.model.SiteSettingBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 描述
 *
 * @author weixing.yang
 * @version 1.1.0
 * @date 2019/11/14 13:58
 */
@Mapper
public interface SettingConvert {

    SettingConvert INSTANCE = Mappers.getMapper(SettingConvert.class);

    @Mappings({})
    SiteSettingBO convert(SettingsDO settingsDO);


}
