package com.evgeny.innowisetasks.Mapper;

import com.evgeny.innowisetasks.DTO.CreateUserDTO;
import com.evgeny.innowisetasks.DTO.UserDTO;
import com.evgeny.innowisetasks.Entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(UserEntity user);
    UserDTO toDTO(CreateUserDTO user);

    UserEntity toEntity(UserDTO user);
    UserEntity toEntity(CreateUserDTO user);


    List<UserDTO> toDtoList(List<UserEntity> entities);
}
