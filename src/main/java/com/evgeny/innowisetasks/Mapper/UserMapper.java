package com.evgeny.innowisetasks.Mapper;

import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);

    UserEntity toEntity(UserDTO user);
}
