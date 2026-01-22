package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.Snack_BE.DTOs.OrderCreateRequest;
import com.Snack_BE.DTOs.OrderDTOtoKafka;
import com.Snack_BE.DTOs.OrderResponseDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderEntity.OrderStatus;
import com.Snack_BE.Model.OrderEntity.PaymentMethod;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.OrderItemRepo;
import com.Snack_BE.Repo.OrderRepo;
import com.Snack_BE.Repo.ProductRepo;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.OrderMapper;
import com.Snack_BE.config.RedisService;
import com.Snack_BE.config.TestDataHelper;

/**
 * Unit Tests for OrderService
 * Tests order creation with Redis, Kafka, validation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserRepo userRepo;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private OrderService orderService;

    private UserEntity testUser;
    private ProductEntity testProduct;
    private OrderEntity testOrder;
    private OrderResponseDTO testOrderResponseDTO;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new UserEntity();
        testUser.setUserID(TestDataHelper.TEST_USER_ID);
        testUser.setEmail(TestDataHelper.TEST_USER_EMAIL);

        // Setup test product
        testProduct = new ProductEntity();
        testProduct.setProductId(TestDataHelper.TEST_PRODUCT_ID);
        testProduct.setProductName(TestDataHelper.TEST_PRODUCT_NAME);
        testProduct.setStockQuantity(100);

        // Setup test order
        testOrder = new OrderEntity();
        testOrder.setOrderID(UUID.randomUUID());
        testOrder.setUserEntity(testUser);
        testOrder.setShippingAddress("123 Test Street");
        testOrder.setPaymentMethod(PaymentMethod.MOMO);
        testOrder.setStatus(OrderStatus.PAYMENT_PENDING);

        // Setup test order response DTO
        testOrderResponseDTO = OrderResponseDTO.builder()
                .orderID(testOrder.getOrderID())
                .shippingAddress("123 Test Street")
                .paymentMethod("MOMO")
                .status("PAYMENT_PENDING")
                .build();
    }

    // ==================== getAllOrder Tests ====================

    @Test
    @DisplayName("getAllOrder should return list of orders")
    void getAllOrder_ShouldReturnListOfOrders() {
        // Arrange
        List<OrderEntity> orders = Arrays.asList(testOrder);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderMapper.toDTO(any(OrderEntity.class))).thenReturn(testOrderResponseDTO);

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderService.getAllOrder();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(orderRepo, times(1)).findAll();
        verify(orderMapper, times(1)).toDTO(testOrder);
    }

    @Test
    @DisplayName("getAllOrder should return empty list when no orders")
    void getAllOrder_ShouldReturnEmptyList_WhenNoOrders() {
        // Arrange
        when(orderRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderService.getAllOrder();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(orderRepo, times(1)).findAll();
    }

    // ==================== createNewOrder Validation Tests ====================

    @Test
    @DisplayName("createNewOrder should throw exception when request is null")
    void createNewOrder_ShouldThrowException_WhenRequestIsNull() {
        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.createNewOrder(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Request cannot be empty"));
    }

    @Test
    @DisplayName("createNewOrder should throw exception when userId is null")
    void createNewOrder_ShouldThrowException_WhenUserIdIsNull() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(null);
        request.setShippingAddress("123 Test St");
        request.setItems(Arrays.asList(Map.of("productid", 1L, "qty", 2L)));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.createNewOrder(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("createNewOrder should throw exception when items list is empty")
    void createNewOrder_ShouldThrowException_WhenItemsListIsEmpty() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("123 Test St");
        request.setItems(Collections.emptyList());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.createNewOrder(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Items list cannot be empty"));
    }

    @Test
    @DisplayName("createNewOrder should throw exception when quantity is zero")
    void createNewOrder_ShouldThrowException_WhenQuantityIsZero() {
        // Arrange
        Map<String, Long> item = new HashMap<>();
        item.put("productid", 1L);
        item.put("qty", 0L);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(1L);
        request.setShippingAddress("123 Test St");
        request.setItems(Arrays.asList(item));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.createNewOrder(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Quantity must be greater than 0"));
    }

    // ==================== createNewOrder Success Tests ====================

    @Test
    @DisplayName("createNewOrder should create order when Redis allows reservation")
    void createNewOrder_ShouldCreateOrder_WhenRedisAllowsReservation() {
        // Arrange
        Map<String, Long> item = Map.of("productid", TestDataHelper.TEST_PRODUCT_ID, "qty", 2L);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(TestDataHelper.TEST_USER_ID);
        request.setShippingAddress("123 Test Street");
        request.setItems(Arrays.asList(item));

        when(redisService.tryReserve(anyLong(), anyLong(), anyInt())).thenReturn(true);
        when(userRepo.findById(TestDataHelper.TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(orderRepo.save(any(OrderEntity.class))).thenReturn(testOrder);
        when(kafkaTemplate.send(anyString(), any())).thenReturn(new CompletableFuture<>());

        // Act
        OrderEntity result = orderService.createNewOrder(request);

        // Assert
        assertNotNull(result);
        verify(redisService, times(1)).tryReserve(TestDataHelper.TEST_PRODUCT_ID, TestDataHelper.TEST_USER_ID, 2);
        verify(userRepo, times(1)).findById(TestDataHelper.TEST_USER_ID);
        verify(orderRepo, times(1)).save(any(OrderEntity.class));
        verify(kafkaTemplate, times(1)).send(eq("ORDER_CREATED"), any(OrderDTOtoKafka.class));
    }

    @Test
    @DisplayName("createNewOrder should fallback to DB when Redis denies reservation but stock available")
    void createNewOrder_ShouldFallbackToDB_WhenRedisDeniesButStockAvailable() {
        // Arrange
        Map<String, Long> item = Map.of("productid", TestDataHelper.TEST_PRODUCT_ID, "qty", 2L);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(TestDataHelper.TEST_USER_ID);
        request.setShippingAddress("123 Test Street");
        request.setItems(Arrays.asList(item));

        when(redisService.tryReserve(anyLong(), anyLong(), anyInt())).thenReturn(false);
        when(productRepo.findById(TestDataHelper.TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(productRepo.save(any(ProductEntity.class))).thenReturn(testProduct);
        when(userRepo.findById(TestDataHelper.TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(orderRepo.save(any(OrderEntity.class))).thenReturn(testOrder);
        when(kafkaTemplate.send(anyString(), any())).thenReturn(new CompletableFuture<>());

        // Act
        OrderEntity result = orderService.createNewOrder(request);

        // Assert
        assertNotNull(result);
        verify(productRepo, times(1)).findById(TestDataHelper.TEST_PRODUCT_ID);
        verify(productRepo, times(1)).save(testProduct);
        verify(redisService, times(1)).updateStock(TestDataHelper.TEST_PRODUCT_ID, 98); // 100 - 2
        verify(orderRepo, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("createNewOrder should throw exception when stock insufficient in DB")
    void createNewOrder_ShouldThrowException_WhenStockInsufficient() {
        // Arrange
        Map<String, Long> item = Map.of("productid", TestDataHelper.TEST_PRODUCT_ID, "qty", 200L);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(TestDataHelper.TEST_USER_ID);
        request.setShippingAddress("123 Test Street");
        request.setItems(Arrays.asList(item));

        testProduct.setStockQuantity(100); // Not enough stock

        when(redisService.tryReserve(anyLong(), anyLong(), anyInt())).thenReturn(false);
        when(productRepo.findById(TestDataHelper.TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            orderService.createNewOrder(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("stock of product is not enough"));
    }

    @Test
    @DisplayName("createNewOrder should throw exception when user not found")
    void createNewOrder_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Map<String, Long> item = Map.of("productid", TestDataHelper.TEST_PRODUCT_ID, "qty", 2L);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(999L); // Non-existent user
        request.setShippingAddress("123 Test Street");
        request.setItems(Arrays.asList(item));

        when(redisService.tryReserve(anyLong(), anyLong(), anyInt())).thenReturn(true);
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createNewOrder(request);
        });

        verify(userRepo, times(1)).findById(999L);
    }

    @Test
    @DisplayName("createNewOrder should send Kafka message with correct data")
    void createNewOrder_ShouldSendKafkaMessage_WithCorrectData() {
        // Arrange
        Map<String, Long> item = Map.of("productid", TestDataHelper.TEST_PRODUCT_ID, "qty", 2L);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUserId(TestDataHelper.TEST_USER_ID);
        request.setShippingAddress("123 Test Street");
        request.setItems(Arrays.asList(item));

        when(redisService.tryReserve(anyLong(), anyLong(), anyInt())).thenReturn(true);
        when(userRepo.findById(TestDataHelper.TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(orderRepo.save(any(OrderEntity.class))).thenReturn(testOrder);
        when(kafkaTemplate.send(anyString(), any())).thenReturn(new CompletableFuture<>());

        // Act
        orderService.createNewOrder(request);

        // Assert
        ArgumentCaptor<OrderDTOtoKafka> kafkaCaptor = ArgumentCaptor.forClass(OrderDTOtoKafka.class);
        verify(kafkaTemplate).send(eq("ORDER_CREATED"), kafkaCaptor.capture());

        OrderDTOtoKafka sentMessage = kafkaCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(1, sentMessage.getItems().size());
        
        // Verify OrderEntity fields (not object equality, since orderID is set after save)
        OrderEntity sentOrder = sentMessage.getOrderEntity();
        assertNotNull(sentOrder);
        assertEquals("123 Test Street", sentOrder.getShippingAddress());
        assertEquals(PaymentMethod.MOMO, sentOrder.getPaymentMethod());
        assertEquals(OrderStatus.PAYMENT_PENDING, sentOrder.getStatus());
        assertEquals(testUser, sentOrder.getUserEntity());
    }

    // ==================== getOrderbyUserId Tests ====================

    @Test
    @DisplayName("getOrderbyUserId should return orders for user")
    @SuppressWarnings("deprecation") 
    void getOrderbyUserId_ShouldReturnOrders_ForUser() {
        // Arrange
        List<OrderEntity> orders = Arrays.asList(testOrder);
        when(userRepo.getById(TestDataHelper.TEST_USER_ID)).thenReturn(testUser);
        when(orderRepo.getByUserEntity(testUser)).thenReturn(orders);
        when(orderMapper.toDTO(any(OrderEntity.class))).thenReturn(testOrderResponseDTO);

        // Act
        ResponseEntity<?> response = orderService.getOrderbyUserId(TestDataHelper.TEST_USER_ID);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        List<OrderResponseDTO> dtoList = (List<OrderResponseDTO>) response.getBody();
        assertEquals(1, dtoList.size());

        verify(userRepo, times(1)).getById(TestDataHelper.TEST_USER_ID);
        verify(orderRepo, times(1)).getByUserEntity(testUser);
    }
}
