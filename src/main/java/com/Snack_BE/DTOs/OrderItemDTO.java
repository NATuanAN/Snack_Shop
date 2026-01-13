package com.Snack_BE.DTOs;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private ProductDTO productDTO;
    private Integer Quantity;
    private BigDecimal UnitPrice;
}
