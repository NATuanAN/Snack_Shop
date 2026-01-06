package com.Snack_BE.DTOs;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private Long amount;
}