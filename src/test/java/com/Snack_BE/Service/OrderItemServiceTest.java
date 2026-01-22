package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.Snack_BE.DTOs.OrderItemDTO;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Model.ShopEntity;
import com.Snack_BE.Repo.OrderItemRepo;
import com.Snack_BE.Repo.OrderRepo;
import com.Snack_BE.config.TestDataHelper;

/**
 * Unit Tests for OrderItemService
 * Tests order item retrieval and DTO mapping
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderItemService Unit Tests")
class OrderItemServiceTest {

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderEntity testOrder;
    private OrderItemEntity testOrderItem1;
    private OrderItemEntity testOrderItem2;
    private ProductEntity testProduct1;
    private ProductEntity testProduct2;
    private ShopEntity testShop;

    @BeforeEach
    void setUp() {
        // Setup test shop
        testShop = new ShopEntity();
        testShop.setShopId(TestDataHelper.TEST_SHOP_ID);
        testShop.setShopName("Test Shop");

        // Setup test products
        testProduct1 = new ProductEntity();
        testProduct1.setProductId(TestDataHelper.TEST_PRODUCT_ID);
        testProduct1.setProductName(TestDataHelper.TEST_PRODUCT_NAME);
        testProduct1.setPrice(BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE));
        testProduct1.setImage_url("image1.jpg");
        testProduct1.setShop(testShop);

        testProduct2 = new ProductEntity();
        testProduct2.setProductId(2L);
        testProduct2.setProductName("Test Snack 2");
        testProduct2.setPrice(BigDecimal.valueOf(20000.0));
        testProduct2.setImage_url("image2.jpg");
        testProduct2.setShop(testShop);

        // Setup test order
        testOrder = new OrderEntity();
        testOrder.setOrderID(UUID.randomUUID());

        // Setup test order items
        testOrderItem1 = new OrderItemEntity();
        testOrderItem1.setOrderEntity(testOrder);
        testOrderItem1.setProductEntity(testProduct1);
        testOrderItem1.setQuantity(2);
        testOrderItem1.setUnitPrice(BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE));

        testOrderItem2 = new OrderItemEntity();
        testOrderItem2.setOrderEntity(testOrder);
        testOrderItem2.setProductEntity(testProduct2);
        testOrderItem2.setQuantity(3);
        testOrderItem2.setUnitPrice(BigDecimal.valueOf(20000.0));
    }

    // ==================== getAllOrderItem Tests ====================

    @Test
    @DisplayName("getAllOrderItem should return list when items exist")
    void getAllOrderItem_ShouldReturnList_WhenItemsExist() {
        // Arrange
        List<OrderItemEntity> itemList = Arrays.asList(testOrderItem1, testOrderItem2);
        when(orderItemRepo.findAll()).thenReturn(itemList);

        // Act
        ResponseEntity<List<OrderItemEntity>> response = orderItemService.getAllOrderItem();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(2, response.getBody().get(0).getQuantity());
        assertEquals(3, response.getBody().get(1).getQuantity());

        verify(orderItemRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllOrderItem should return empty list when no items")
    void getAllOrderItem_ShouldReturnEmptyList_WhenNoItems() {
        // Arrange
        when(orderItemRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<OrderItemEntity>> response = orderItemService.getAllOrderItem();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(orderItemRepo, times(1)).findAll();
    }

    // ==================== getByOrderEntity Tests ====================

    @Test
    @DisplayName("getByOrderEntity should return OrderItemDTOs when order exists")
    void getByOrderEntity_ShouldReturnOrderItemDTOs_WhenOrderExists() {
        // Arrange
        UUID orderId = testOrder.getOrderID();
        List<OrderItemEntity> itemList = Arrays.asList(testOrderItem1, testOrderItem2);
        
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderItemRepo.findByOrderEntity(testOrder)).thenReturn(itemList);

        // Act
        ResponseEntity<?> response = orderItemService.getByOrderEntity(orderId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        List<OrderItemDTO> dtoList = (List<OrderItemDTO>) response.getBody();
        assertEquals(2, dtoList.size());
        
        // Verify first item mapping
        OrderItemDTO dto1 = dtoList.get(0);
        assertEquals(TestDataHelper.TEST_PRODUCT_NAME, dto1.getProductDTO().productName());
        assertEquals(2, dto1.getQuantity());
        assertEquals(BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE), dto1.getUnitPrice());

        verify(orderRepo, times(1)).findById(orderId);
        verify(orderItemRepo, times(1)).findByOrderEntity(testOrder);
    }

    @Test
    @DisplayName("getByOrderEntity should return empty list when order has no items")
    void getByOrderEntity_ShouldReturnEmptyList_WhenOrderHasNoItems() {
        // Arrange
        UUID orderId = testOrder.getOrderID();
        
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderItemRepo.findByOrderEntity(testOrder)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> response = orderItemService.getByOrderEntity(orderId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        @SuppressWarnings("unchecked")
        List<OrderItemDTO> dtoList = (List<OrderItemDTO>) response.getBody();
        assertTrue(dtoList.isEmpty());

        verify(orderRepo, times(1)).findById(orderId);
        verify(orderItemRepo, times(1)).findByOrderEntity(testOrder);
    }

    @Test
    @DisplayName("getByOrderEntity should throw exception when order not found")
    void getByOrderEntity_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        UUID nonExistentOrderId = UUID.randomUUID();
        when(orderRepo.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            orderItemService.getByOrderEntity(nonExistentOrderId);
        });

        verify(orderRepo, times(1)).findById(nonExistentOrderId);
        verify(orderItemRepo, never()).findByOrderEntity(any());
    }

    @Test
    @DisplayName("getByOrderEntity should map ProductDTO correctly with shop name")
    void getByOrderEntity_ShouldMapProductDTOCorrectly() {
        // Arrange
        UUID orderId = testOrder.getOrderID();
        List<OrderItemEntity> itemList = Arrays.asList(testOrderItem1);
        
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderItemRepo.findByOrderEntity(testOrder)).thenReturn(itemList);

        // Act
        ResponseEntity<?> response = orderItemService.getByOrderEntity(orderId);

        // Assert
        @SuppressWarnings("unchecked")
        List<OrderItemDTO> dtoList = (List<OrderItemDTO>) response.getBody();
        
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        
        // Verify ProductDTO fields
        OrderItemDTO dto = dtoList.get(0);
        assertEquals(TestDataHelper.TEST_PRODUCT_ID, dto.getProductDTO().productId());
        assertEquals(TestDataHelper.TEST_PRODUCT_NAME, dto.getProductDTO().productName());
        assertEquals(BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE), dto.getProductDTO().price());
        assertEquals("image1.jpg", dto.getProductDTO().image_url());
        assertEquals("Test Shop", dto.getProductDTO().shopName());
    }
}
