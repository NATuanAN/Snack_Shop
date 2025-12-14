package com.Snack_BE.Service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Repo.OrderRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;

    public ResponseEntity<List<OrderEntity>> getAllOrder() {
        return ResponseEntity.status(200).body(orderRepo.findAll());
    }
}
