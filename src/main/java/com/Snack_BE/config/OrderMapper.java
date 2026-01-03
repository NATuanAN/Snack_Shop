package com.Snack_BE.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "userEntity", target = "buyer")
    OrderResponseDTO toDTO(OrderEntity entity);
}
