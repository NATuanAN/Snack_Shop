package com.Snack_BE.DTOs;

import java.util.UUID;
import lombok.Data;

@Data
public class PaymentRequest {
    private UUID orderId;
    private Long amount;
}