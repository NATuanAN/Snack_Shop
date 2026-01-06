package com.Snack_BE.Controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Snack_BE.Service.PaymentService;
import com.Snack_BE.DTOs.PaymentRequest;

@RestController
@RequestMapping("/public/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * FE gọi API này để tạo link thanh toán MoMo
     */
    @PostMapping("/momo")
    public ResponseEntity<?> createMomoPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    /**
     * MoMo gọi API này sau khi user thanh toán xong
     * QUAN TRỌNG: chỉ tin cái này
     */
    @PostMapping("/momo/ipn")
    public ResponseEntity<?> momoIPN(@RequestBody Map<String, Object> payload) {
        paymentService.handleIPN(payload);
        return ResponseEntity.ok().build();
    }

    /**
     * Redirect URL (chỉ để UX)
     */
    @GetMapping("/momo/return")
    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok("Payment processing");
    }
}
