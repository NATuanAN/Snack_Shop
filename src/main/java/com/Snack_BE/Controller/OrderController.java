 package com.Snack_BE.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.DTOs.OrderCreateRequest;
import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        return orderService.getAllOrder();
    }

    @PostMapping("/create-new-order")
    public ResponseEntity<?> createNewOrder(Authentication auth, @RequestBody OrderCreateRequest request) {
        Long userId = (Long) auth.getDetails();
        request.setUserId(userId);

        OrderEntity orderEntity = orderService.createNewOrder(request);
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderEntity.getOrderID());
        response.put("message", "Order created successfully");
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/order-by-user")
    public ResponseEntity<?> getOrderbyUserId(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        return orderService.getOrderbyUserId(userId);
    }
}
