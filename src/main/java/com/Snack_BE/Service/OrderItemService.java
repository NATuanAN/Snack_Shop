package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Repo.OrderItemRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;

    public ResponseEntity<List<OrderItemEntity>> getAllOrderItem() {
        return ResponseEntity.status(200).body(orderItemRepo.findAll());
    }
}
