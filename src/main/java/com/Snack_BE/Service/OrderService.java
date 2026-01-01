package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Repo.OrderRepo;
import com.Snack_BE.config.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;

    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<OrderResponseDTO> listOfOrder = orderRepo.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();

        return ResponseEntity.ok(listOfOrder);
    }

    // public void createNewOrder() {
    // orderRepo.save()
    // }
}
