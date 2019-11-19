package com.a.platform.goods.convert;

import com.a.platform.goods.model.CategoryBO;
import com.a.platform.goods.model.CategoryDO;
import com.a.platform.goods.model.CategoryDTO;
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
public interface CategoryConvert {

    CategoryConvert INSTANCE = Mappers.getMapper(CategoryConvert.class);

    @Mappings({})
    CategoryBO convert(CategoryDO categoryDO);

    @Mappings({})
    CategoryDO convert(CategoryDTO categoryDTO);

    @Mappings({})
    List<CategoryBO> convert(List<CategoryDO> categoryDOList);


}
