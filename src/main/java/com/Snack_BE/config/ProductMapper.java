package com.Snack_BE.config;

import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(ProductEntity productEntity);
}
