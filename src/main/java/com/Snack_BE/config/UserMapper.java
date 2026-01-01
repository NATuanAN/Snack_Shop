package com.Snack_BE.config;

import org.mapstruct.Mapper;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.DTOs.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDTO(UserEntity entity);
}