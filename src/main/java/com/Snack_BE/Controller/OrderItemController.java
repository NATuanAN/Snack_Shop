package com.Snack_BE.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Service.OrderItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderitem")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderItemEntity>> getAllOrderItem() {
        return orderItemService.getAllOrderItem();
    }
}
