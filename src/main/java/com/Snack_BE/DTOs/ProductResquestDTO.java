package com.Snack_BE.DTOs;

import com.Snack_BE.Model.ShopEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResquestDTO {
    private String productName;
    private BigDecimal price;
    private String image_url;
    private int stockQuantity;
    private String description;
}
