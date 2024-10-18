package com.demo.springboot.animal;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnimalMapper {

    AnimalMapper INSTANCE = Mappers.getMapper(AnimalMapper.class);

    AnimalEntity fromDto(final AnimalDto animal);
    AnimalDto toDto(final AnimalEntity animal);

}
