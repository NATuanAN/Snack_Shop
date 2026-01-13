package com.Snack_BE.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.DTOs.OrderItemDTO;
import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Repo.OrderItemRepo;
import com.Snack_BE.Repo.OrderRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    private final OrderRepo orderRepo;

    public ResponseEntity<List<OrderItemEntity>> getAllOrderItem() {
        return ResponseEntity.status(200).body(orderItemRepo.findAll());
    }

    public ResponseEntity<?> getByOrderEntity(UUID orderUuid) {
        OrderEntity orderEntity = orderRepo.findById(orderUuid).orElseThrow();
        List<OrderItemEntity> listOfOrderItem = orderItemRepo.findByOrderEntity(orderEntity);
        List<OrderItemDTO> items = listOfOrderItem.stream()
                .map(item -> new OrderItemDTO(
                        new ProductDTO(
                                item.getProductEntity().getProductId(),
                                item.getProductEntity().getProductName(),
                                item.getProductEntity().getPrice(),
                                item.getProductEntity().getImage_url(),
                                item.getProductEntity().getShop().getShopName()),
                        item.getQuantity(),
                        item.getUnitPrice()))
                .toList();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(items);
    }
}
