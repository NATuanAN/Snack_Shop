package com.Snack_BE.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.server.ResponseStatusException;
import com.Snack_BE.DTOs.OrderCreateRequest;
import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.OrderItemRepo;
import com.Snack_BE.Repo.OrderRepo;
import com.Snack_BE.Repo.ProductRepo;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.OrderItemID;
import com.Snack_BE.config.OrderMapper;
import com.Snack_BE.config.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderMapper orderMapper;
    private final UserRepo userRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductRepo productRepo;
    private final RedisService redisService;

    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<OrderResponseDTO> listOfOrder = orderRepo.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();

        return ResponseEntity.ok(listOfOrder);
    }

    public void createNewOrder(OrderCreateRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be empty");
        }

        Long userId = request.getUserId();
        String shippingAddress = request.getShippingAddress();
        List<Map<String, Long>> items = request.getItems();

        if (userId == null || shippingAddress == null || items == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User ID, shipping address, and items must not be null");
        }

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Items list cannot be empty");
        }
        for (Map<String, Long> item : items) {
            if (item.get("productid") == null || item.get("qty") == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each item must have productid and qty");
            }

            Long productId = item.get("productid");
            Integer qty = Integer.valueOf(item.get("qty").toString());

            if (qty <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
            }

            Boolean allowed = redisService.tryReserve(productId, userId, qty);

            if (!allowed) {
                ProductEntity productEntity = productRepo.findById(productId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "product with id = " + productId + " is not found"));

                if (productEntity.getStockQuantity() - qty < 0)
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "The stock of product is not enough or empty");
                productEntity.setStockQuantity(productEntity.getStockQuantity() - qty);
                productRepo.save(productEntity);
                redisService.updateStock(productId, productEntity.getStockQuantity());
            }

        }

        OrderCreateRequest orderCreateTemp = new OrderCreateRequest(items, userId, shippingAddress);
        // kafkaTemplate.send("ORDER_CREATED", orderCreateTemp);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("ORDER_CREATED", orderCreateTemp);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Failed to send message: " + ex.getMessage());
            } else {
                System.out.println("Sent message successfully: " + orderCreateTemp);
            }
        });

        System.out.println("send to kafka");
    }

    @Transactional
    @KafkaListener(topics = "ORDER_CREATED", groupId = "snack-order-group")
    public void handleCreateInDB(OrderCreateRequest event) {
        try {
            System.out.println("Received kafka message");
            System.out.println("User Id = " + event.getUserId());
            System.out.println("Address = " + event.getShippingAddress());
            System.out.println("Items = " + event.getItems());

            UserEntity userEntity = userRepo.findById(event.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found with ID: " + event.getUserId()));

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserEntity(userEntity);
            orderEntity.setPaymentMethod("Cod");
            orderEntity.setShippingAddress(event.getShippingAddress());
            orderEntity.setStatus("Ordered");
            orderRepo.save(orderEntity);

            List<OrderItemEntity> listofOrderItem = new ArrayList<>();
            List<Map<String, Long>> items = event.getItems();
            for (Map<String, Long> item : items) {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                ProductEntity productEntity = productRepo.findById(item.get("productid"))
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Product not found with ID: " + item.get("productid")));

                orderItemEntity.setOrderItemID(new OrderItemID(orderEntity.getOderID(), productEntity.getProductID()));
                orderItemEntity.setOrder(orderEntity);
                orderItemEntity.setProduct(productEntity);
                orderItemEntity.setQuantity(Integer.valueOf(item.get("qty").toString()));
                listofOrderItem.add(orderItemEntity);
            }
            orderItemRepo.saveAll(listofOrderItem);
            System.out.println("Order created successfully with ID: " + orderEntity.getOderID());
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to process order: " + e.getMessage(), e);
        }
    }

}
