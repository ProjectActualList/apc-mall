package com.a.platform.goods.convert;

import com.a.platform.goods.model.BrandBO;
import com.a.platform.goods.model.BrandDO;
import com.a.platform.goods.model.BrandDTO;
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
@Mapper(componentModel="spring")
public interface BrandConvert {

    BrandConvert INSTANCE = Mappers.getMapper(BrandConvert.class);

    @Mappings({})
    BrandBO convert(BrandDO brandDO);

    @Mappings({})
    BrandDO convert(BrandDTO brandDTO);

    @Mappings({})
    List<BrandBO> convert(List<BrandDO> brandDOList);


}
