package com.innowise.innowisetasks.mapper;

import com.innowise.innowisetasks.dto.CreateUserDTO;
import com.innowise.innowisetasks.dto.UserDTO;
import com.innowise.innowisetasks.entity.UserEntity;
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
