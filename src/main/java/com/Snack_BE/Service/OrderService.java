package com.Snack_BE.Service;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.OrderRepo;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.OrderMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final UserRepo userRepo;
    private final KafkaTemplate kafkaTemplate;

    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<OrderResponseDTO> listOfOrder = orderRepo.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();

        return ResponseEntity.ok(listOfOrder);
    }

    @Transactional
    public void createNewOrder(Map<String, Object> request) {
        if (request == null || request.isEmpty() || !request.containsKey("id")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Long userId = Long.valueOf(request.get("id").toString());

        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "This user is not found"));

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setShippingAddress(request.get("address").toString());
        orderEntity.setPaymentMethod(request.get("payment").toString());
        orderEntity.setStatus(request.get("status").toString());
        orderEntity.setUserEntity(userEntity);
        orderRepo.save(orderEntity);

    }

    public void proKafka() {
        kafkaTemplate.send("test-topic", "hello from pro");
    }

    @KafkaListener(topics = "test-topic", groupId = "group-1")
    public void comKafka(String message) {
        System.out.println("Nhận được message: " + message);
    }

}
