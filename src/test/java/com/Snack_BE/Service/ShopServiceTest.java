package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.Snack_BE.Model.ShopEntity;
import com.Snack_BE.Repo.ShopRepo;
import com.Snack_BE.config.TestDataHelper;

/**
 * Unit Tests for ShopService
 * Tests shop listing functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ShopService Unit Tests")
class ShopServiceTest {

    @Mock
    private ShopRepo shopRepo;

    @InjectMocks
    private ShopService shopService;

    private ShopEntity testShop1;
    private ShopEntity testShop2;

    @BeforeEach
    void setUp() {
        // Setup test shops
        testShop1 = new ShopEntity();
        testShop1.setShopId(TestDataHelper.TEST_SHOP_ID);
        testShop1.setShopName("Test Shop 1");
        testShop1.setAddress("123 Test Street");

        testShop2 = new ShopEntity();
        testShop2.setShopId(2L);
        testShop2.setShopName("Test Shop 2");
        testShop2.setAddress("456 Test Avenue");
    }

    @Test
    @DisplayName("getAllShop should return list of shops when shops exist")
    void getAllShop_ShouldReturnListOfShops_WhenShopsExist() {
        // Arrange
        List<ShopEntity> expectedShops = Arrays.asList(testShop1, testShop2);
        when(shopRepo.findAll()).thenReturn(expectedShops);

        // Act
        ResponseEntity<List<ShopEntity>> response = shopService.getAllShop();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Shop 1", response.getBody().get(0).getShopName());
        assertEquals("Test Shop 2", response.getBody().get(1).getShopName());
        assertEquals("123 Test Street", response.getBody().get(0).getAddress());
        
        verify(shopRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllShop should return empty list when no shops exist")
    void getAllShop_ShouldReturnEmptyList_WhenNoShops() {
        // Arrange
        List<ShopEntity> emptyList = Collections.emptyList();
        when(shopRepo.findAll()).thenReturn(emptyList);

        // Act
        ResponseEntity<List<ShopEntity>> response = shopService.getAllShop();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
        
        verify(shopRepo, times(1)).findAll();
    }
}
