package com.Snack_BE.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Service.OrderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        return orderService.getAllOrder();
    }

    @PostMapping("/create-new-order")
    public void createNewOrder(@RequestBody Map<String, Object> request) {
        return;
    }

    @GetMapping("/testkafka")
    public void testkafka() {
        orderService.proKafka();
        return;
    }
}
