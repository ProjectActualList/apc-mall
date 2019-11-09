package com.a.platform.member.convert;


import com.a.platform.member.model.MemberDO;
import com.a.platform.member.model.bo.MemberBO;
import com.a.platform.member.model.dto.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberConvert {

    MemberConvert INSTANCE = Mappers.getMapper(MemberConvert.class);

    @Mappings({})
    MemberBO convert(MemberDO memberDO);

    @Mappings({})
    MemberDO convert(MemberDTO memberDTO);

    @Mappings({})
    MemberDTO convert(MemberBO memberBO);

    @Mappings({})
    List<MemberBO> convert(List<MemberDO> memberDOList);
}
