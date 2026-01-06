package com.Snack_BE.Service;

import java.util.Map;
import com.Snack_BE.DTOs.PaymentRequest;

public interface PaymentService {
    Object createPayment(PaymentRequest request);

    void handleIPN(Map<String, Object> payload);
}
