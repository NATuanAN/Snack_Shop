package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Repo.ProductRepo;
import com.Snack_BE.config.TestDataHelper;

/**
 * Unit Tests for ProductService
 * Tests product listing functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private ProductDTO testProduct1;
    private ProductDTO testProduct2;

    @BeforeEach
    void setUp() {
        // Setup test products using record constructor
        testProduct1 = new ProductDTO(
            TestDataHelper.TEST_PRODUCT_ID,
            TestDataHelper.TEST_PRODUCT_NAME,
            BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE),
            "image1.jpg",
            "Test Shop"
        );

        testProduct2 = new ProductDTO(
            2L,
            "Snack 2",
            BigDecimal.valueOf(20000.0),
            "image2.jpg",
            "Shop 2"
        );
    }

    @Test
    @DisplayName("getallEntity should return ProductDTO list when products exist")
    void getallEntity_ShouldReturnProductDTOList_WhenProductsExist() {
        // Arrange
        List<ProductDTO> expectedProducts = Arrays.asList(testProduct1, testProduct2);
        when(productRepo.findAllProductDTO()).thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<ProductDTO>> response = productService.getAllEntity();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(TestDataHelper.TEST_PRODUCT_NAME, response.getBody().get(0).productName());
        assertEquals(BigDecimal.valueOf(TestDataHelper.TEST_PRODUCT_PRICE), response.getBody().get(0).price());
        assertEquals("Snack 2", response.getBody().get(1).productName());
        
        verify(productRepo, times(1)).findAllProductDTO();
    }

    @Test
    @DisplayName("getallEntity should return empty list when no products exist")
    void getallEntity_ShouldReturnEmptyList_WhenNoProducts() {
        // Arrange
        List<ProductDTO> emptyList = Collections.emptyList();
        when(productRepo.findAllProductDTO()).thenReturn(emptyList);

        // Act
        ResponseEntity<List<ProductDTO>> response = productService.getAllEntity();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
        
        verify(productRepo, times(1)).findAllProductDTO();
    }
}

