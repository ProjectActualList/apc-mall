package com.a.platform.member.convert;


import com.a.platform.member.model.ClerkDO;
import com.a.platform.member.model.bo.ClerkBO;
import com.a.platform.member.model.dto.ClerkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClerkConvert {

    ClerkConvert INSTANCE = Mappers.getMapper(ClerkConvert.class);

    @Mappings({})
    ClerkBO convert(ClerkDO clerkDO);

    @Mappings({})
    ClerkDO convert(ClerkDTO clerkDTO);
}
