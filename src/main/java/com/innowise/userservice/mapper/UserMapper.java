package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.CreateUserDTO;
import com.innowise.userservice.dto.UserDTO;
import com.innowise.userservice.entity.UserEntity;
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
