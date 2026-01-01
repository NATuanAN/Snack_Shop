package com.Snack_BE.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long orderID;
    private String shippingAddress;
    private String paymentMethod;
    private String status;
    private UserResponseDTO buyer;
}
