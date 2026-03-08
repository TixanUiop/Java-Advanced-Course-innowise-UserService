package com.evgeny.innowisetasks.Mapper;

import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper<I, K> {

    I toDTO(K entity);

    K toEntity(I entity);
}
