package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Snack_BE.DTOs.MomoConfig;
import com.Snack_BE.DTOs.PaymentRequest;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderEntity.OrderStatus;
import com.Snack_BE.Repo.OrderRepo;

/**
 * Unit Tests for MoMoService
 * Tests payment creation and IPN (Instant Payment Notification) handling
 * Note: createPayment tests focus on validation logic; actual HTTP calls require integration tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MoMoService Unit Tests")
class MoMoServiceTest {

    @Mock
    private OrderRepo orderRepo;

    private MoMoService moMoService;
    private MomoConfig momoConfig;

    @BeforeEach
    void setUp() {
        // Setup MomoConfig
        momoConfig = new MomoConfig();
        momoConfig.setEndpoint("https://test-payment.momo.vn/v2/gateway/api/create");
        momoConfig.setPartnerCode("TEST_PARTNER_CODE");
        momoConfig.setAccessKey("TEST_ACCESS_KEY");
        momoConfig.setSecretKey("TEST_SECRET_KEY");
        momoConfig.setRedirectUrl("http://localhost:8080/payment/return");
        momoConfig.setIpnUrl("http://localhost:8080/payment/ipn");

        // Create MoMoService
        moMoService = new MoMoService(momoConfig, orderRepo);
    }

    // ==================== createPayment Validation Tests ====================

    @Test
    @DisplayName("createPayment should throw exception when request is null")
    void createPayment_ShouldThrowException_WhenRequestNull() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moMoService.createPayment(null);
        });

        assertTrue(exception.getMessage().contains("Create MoMo payment failed"));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @DisplayName("createPayment should throw exception when orderId is null")
    void createPayment_ShouldThrowException_WhenOrderIdNull() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(null);
        request.setAmount(100000L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moMoService.createPayment(request);
        });

        assertTrue(exception.getMessage().contains("Create MoMo payment failed"));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("OrderId"));
    }

    @Test
    @DisplayName("createPayment should throw exception when amount is null")
    void createPayment_ShouldThrowException_WhenAmountNull() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(UUID.randomUUID());
        request.setAmount(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            moMoService.createPayment(request);
        });

        assertTrue(exception.getMessage().contains("Create MoMo payment failed"));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("Amount"));
    }

    // ==================== handleIPN Tests ====================

    @Test
    @DisplayName("handleIPN should update order to PAID when resultCode is zero")
    void handleIPN_ShouldUpdateOrderToPaid_WhenResultCodeZero() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrderEntity order = new OrderEntity();
        order.setOrderID(orderId);
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        Map<String, Object> ipnPayload = new HashMap<>();
        ipnPayload.put("resultCode", 0);
        ipnPayload.put("orderId", orderId.toString());
        ipnPayload.put("message", "Successful");

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        moMoService.handleIPN(ipnPayload);

        // Assert
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepo, times(1)).findById(orderId);
        verify(orderRepo, times(1)).save(orderCaptor.capture());

        OrderEntity savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.PAID, savedOrder.getStatus());
    }

    @Test
    @DisplayName("handleIPN should update order to FAILED when resultCode is non-zero")
    void handleIPN_ShouldUpdateOrderToFailed_WhenResultCodeNonZero() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrderEntity order = new OrderEntity();
        order.setOrderID(orderId);
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        Map<String, Object> ipnPayload = new HashMap<>();
        ipnPayload.put("resultCode", 1001); // Error code
        ipnPayload.put("orderId", orderId.toString());
        ipnPayload.put("message", "Transaction failed");

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        moMoService.handleIPN(ipnPayload);

        // Assert
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderRepo, times(1)).findById(orderId);
        verify(orderRepo, times(1)).save(orderCaptor.capture());

        OrderEntity savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.FAILED, savedOrder.getStatus());
    }

    @Test
    @DisplayName("handleIPN should throw exception when order is not found")
    void handleIPN_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        Map<String, Object> ipnPayload = new HashMap<>();
        ipnPayload.put("resultCode", 0);
        ipnPayload.put("orderId", orderId.toString());

        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalAccessError exception = assertThrows(IllegalAccessError.class, () -> {
            moMoService.handleIPN(ipnPayload);
        });

        assertTrue(exception.getMessage().contains("Order"));
        assertTrue(exception.getMessage().contains("invalid IPN"));
        
        verify(orderRepo, times(1)).findById(orderId);
        verify(orderRepo, never()).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("handleIPN should handle different resultCode values correctly")
    void handleIPN_ShouldHandleDifferentResultCodes() {
        // Test multiple error codes
        UUID orderId1 = UUID.randomUUID();
        OrderEntity order1 = new OrderEntity();
        order1.setOrderID(orderId1);
        order1.setStatus(OrderStatus.PAYMENT_PENDING);

        Map<String, Object> payload1 = new HashMap<>();
        payload1.put("resultCode", 9000); // Timeout
        payload1.put("orderId", orderId1.toString());

        when(orderRepo.findById(orderId1)).thenReturn(Optional.of(order1));

        // Act
        moMoService.handleIPN(payload1);

        // Assert
        verify(orderRepo, times(1)).save(argThat(order -> 
            order.getStatus() == OrderStatus.FAILED
        ));
    }
}
