package com.Snack_BE.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * Unit Tests for RedisService
 * Tests stock reservation with Lua script and stock updates
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RedisService Unit Tests")
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private RedisService redisService;

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_USER_ID = 1L;
    private static final Integer TEST_QTY = 2;

    @BeforeEach
    void setUp() {
        redisService = new RedisService(redisTemplate);
    }

    // ==================== tryReserve Tests ====================

    @Test
    @DisplayName("tryReserve should return true when stock is available")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldReturnTrue_WhenStockAvailable() {
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(1L);

        // Act
        boolean result = redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QTY);

        // Assert
        assertTrue(result);
        
        // Verify script was executed with correct parameters
        ArgumentCaptor<RedisScript<Long>> scriptCaptor = ArgumentCaptor.forClass(RedisScript.class);
        ArgumentCaptor<List<String>> keysCaptor = ArgumentCaptor.forClass(List.class);
        
        verify(redisTemplate).execute(
            scriptCaptor.capture(),
            keysCaptor.capture(),
            eq(TEST_QTY),
            eq(3)
        );

        // Verify keys are correct
        List<String> keys = keysCaptor.getValue();
        assertEquals(2, keys.size());
        assertTrue(keys.get(0).contains("product:" + TEST_PRODUCT_ID + ":stock"));
        assertTrue(keys.get(1).contains("user:" + TEST_USER_ID + ":product:" + TEST_PRODUCT_ID));
    }

    @Test
    @DisplayName("tryReserve should return false when stock is insufficient")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldReturnFalse_WhenStockInsufficient() {
        // Arrange - Mock Lua script execution returning 0 (failure)
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(0L);

        // Act
        boolean result = redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QTY);

        // Assert
        assertFalse(result);
        
        verify(redisTemplate).execute(
            any(RedisScript.class),
            anyList(),
            eq(TEST_QTY),
            eq(3)
        );
    }

    @Test
    @DisplayName("tryReserve should check user limit when reserving stock")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldCheckUserLimit_WhenReservingStock() {
        // Arrange
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(1L);

        // Act
        redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QTY);

        // Assert - Verify user limit (max 3) is passed to Lua script
        verify(redisTemplate).execute(
            any(RedisScript.class),
            anyList(),
            eq(TEST_QTY),
            eq(3) // Max user limit
        );
    }

    @Test
    @DisplayName("tryReserve should handle null result from Redis")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldHandleNullResult_WhenRedisReturnsNull() {
        // Arrange - Simulate Redis connection issue
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(null);

        // Act
        boolean result = redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, TEST_QTY);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("tryReserve should use correct Redis keys")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldUseCorrectRedisKeys() {
        // Arrange
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(1L);

        Long productId = 123L;
        Long userId = 456L;

        // Act
        redisService.tryReserve(productId, userId, TEST_QTY);

        // Assert
        ArgumentCaptor<List<String>> keysCaptor = ArgumentCaptor.forClass(List.class);
        verify(redisTemplate).execute(
            any(RedisScript.class),
            keysCaptor.capture(),
            any(),
            any()
        );

        List<String> keys = keysCaptor.getValue();
        assertEquals("product:123:stock", keys.get(0));
        assertTrue(keys.get(1).contains("user:456:product:123"));
    }

    @Test
    @DisplayName("tryReserve should handle concurrent requests atomically")
    @SuppressWarnings("unchecked")
    void tryReserve_ShouldHandleConcurrentRequests() {
        // Arrange - Simulate concurrent scenario where first succeeds, second fails
        when(redisTemplate.execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        )).thenReturn(1L, 0L);

        // Act - Simulate two concurrent reservation attempts
        boolean firstResult = redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, 3);
        boolean secondResult = redisService.tryReserve(TEST_PRODUCT_ID, TEST_USER_ID, 3);

        // Assert - First should succeed, second should fail due to insufficient stock
        assertTrue(firstResult);
        assertFalse(secondResult);
        
        // Verify Lua script was called twice
        verify(redisTemplate, times(2)).execute(
            any(RedisScript.class),
            anyList(),
            any(),
            any()
        );
    }

    // ==================== updateStock Tests ====================

    @Test
    @DisplayName("updateStock should set value in Redis")
    void updateStock_ShouldSetValueInRedis() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Long productId = TEST_PRODUCT_ID;
        int stock = 100;

        // Act
        redisService.updateStock(productId, stock);

        // Assert
        String expectedKey = "product:stock:" + productId;
        verify(valueOperations).set(expectedKey, "100");
    }

    @Test
    @DisplayName("updateStock should handle negative stock")
    void updateStock_ShouldHandleNegativeStock() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Long productId = TEST_PRODUCT_ID;
        int negativeStock = -5;

        // Act
        redisService.updateStock(productId, negativeStock);

        // Assert - Should still set the value (business logic will handle validation)
        String expectedKey = "product:stock:" + productId;
        verify(valueOperations).set(expectedKey, "-5");
    }

    @Test
    @DisplayName("updateStock should handle zero stock")
    void updateStock_ShouldHandleZeroStock() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Long productId = TEST_PRODUCT_ID;
        int zeroStock = 0;

        // Act
        redisService.updateStock(productId, zeroStock);

        // Assert
        String expectedKey = "product:stock:" + productId;
        verify(valueOperations).set(expectedKey, "0");
    }
}
