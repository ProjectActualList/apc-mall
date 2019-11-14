package com.a.platform.system.convert;

import com.a.platform.buyer.model.SiteNavigationBO;
import com.a.platform.buyer.model.SiteNavigationDTO;
import com.a.platform.system.model.SiteNavigationDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 描述
 *
 * @author weixing.yang
 * @version 1.1.0
 * @date 2019/11/14 13:58
 */
@Mapper
public interface SiteNavigationConvert {

    SiteNavigationConvert INSTANCE = Mappers.getMapper(SiteNavigationConvert.class);

    @Mappings({})
    SiteNavigationBO convert(SiteNavigationDO siteNavigationDO);

    @Mappings({})
    SiteNavigationDO convert(SiteNavigationDTO siteNavigationDTO);

    @Mappings({})
    List<SiteNavigationBO> convert(List<SiteNavigationDO> siteNavigationDOList);


}
