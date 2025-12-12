package com.Snack_BE.config;

import org.mapstruct.Mapper;
import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserResponseDTO dto);

    UserResponseDTO toDTO(UserEntity entity);
}