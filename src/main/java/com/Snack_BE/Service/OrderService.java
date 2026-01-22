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
import com.Snack_BE.DTOs.OrderDTOtoKafka;
import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Model.OrderEntity.OrderStatus;
import com.Snack_BE.Model.OrderEntity.PaymentMethod;
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

        public OrderEntity createNewOrder(OrderCreateRequest request) {
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
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + userId));
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserEntity(userEntity);
        orderEntity.setPaymentMethod(PaymentMethod.MOMO);
        orderEntity.setShippingAddress(shippingAddress);
        orderEntity.setStatus(OrderStatus.PAYMENT_PENDING);
        orderRepo.save(orderEntity);

        OrderDTOtoKafka orderCreateTemp = new OrderDTOtoKafka(items, orderEntity);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("ORDER_CREATED", orderCreateTemp);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Failed to send message: " + ex.getMessage());
            } else {
                System.out.println("Sent message successfully: " + orderCreateTemp);
            }
        });

        return orderEntity;
    }

    @Transactional
    @KafkaListener(topics = "ORDER_CREATED", groupId = "snack-order-group")
    public void handleCreateInDB(OrderDTOtoKafka event) {
        try {
            List<OrderItemEntity> listofOrderItem = new ArrayList<>();
            List<Map<String, Long>> items = event.getItems();
            for (Map<String, Long> item : items) {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                ProductEntity productEntity = productRepo.findById(item.get("productid"))
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Product not found with ID: " + item.get("productid")));

                orderItemEntity.setOrderItemID(
                        new OrderItemID(event.getOrderEntity().getOrderID(), productEntity.getProductId()));
                orderItemEntity.setOrderEntity(event.getOrderEntity());
                orderItemEntity.setProductEntity(productEntity);
                orderItemEntity.setQuantity(Integer.valueOf(item.get("qty").toString()));
                listofOrderItem.add(orderItemEntity);
            }
            orderItemRepo.saveAll(listofOrderItem);
            System.out.println("Order created successfully with ID: " + event.getOrderEntity().getOrderID());
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to process order: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<?> getOrderbyUserId(Long userId) {
        UserEntity userEntity = userRepo.getById(userId);
        List<OrderEntity> listOfOrder = orderRepo.getByUserEntity(userEntity);
        List<OrderResponseDTO> listOfDtos = listOfOrder.stream().map(orderMapper::toDTO).toList();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(listOfDtos);
    }
}
