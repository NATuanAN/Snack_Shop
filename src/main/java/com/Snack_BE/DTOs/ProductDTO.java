package com.Snack_BE.DTOs;

import java.math.BigDecimal;

public record ProductDTO(
                Long productId,
                String productName,
                BigDecimal price,
                String image_url,
                String shopName) {
}
