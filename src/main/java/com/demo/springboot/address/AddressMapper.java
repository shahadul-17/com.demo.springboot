package com.demo.springboot.address;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressEntity fromDto(final AddressDto address);
    Set<AddressEntity> fromDto(final Set<AddressDto> addresses);
    AddressDto toDto(final AddressEntity addressEntity);
    Set<AddressDto> toDto(final Set<AddressEntity> addressEntities);

}
