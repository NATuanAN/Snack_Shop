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

import com.Snack_BE.Model.PromotionEntity;
import com.Snack_BE.Repo.PromotionRepo;

/**
 * Unit Tests for PromotionService
 * Tests promotion retrieval functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PromotionService Unit Tests")
class PromotionServiceTest {

    @Mock
    private PromotionRepo promotionRepo;

    @InjectMocks
    private PromotionService promotionService;

    private PromotionEntity testPromotion1;
    private PromotionEntity testPromotion2;

    @BeforeEach
    void setUp() {
        // Setup test promotions
        testPromotion1 = new PromotionEntity();
        
        testPromotion2 = new PromotionEntity();
    }

    @Test
    @DisplayName("getAllPromotion should return list when promotions exist")
    void getAllPromotion_ShouldReturnList_WhenPromotionsExist() {
        // Arrange
        List<PromotionEntity> promotions = Arrays.asList(testPromotion1, testPromotion2);
        when(promotionRepo.findAll()).thenReturn(promotions);

        // Act
        ResponseEntity<List<PromotionEntity>> response = promotionService.getAllPromotion();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(promotionRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllPromotion should return empty list when no promotions")
    void getAllPromotion_ShouldReturnEmptyList_WhenNoPromotions() {
        // Arrange
        when(promotionRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<PromotionEntity>> response = promotionService.getAllPromotion();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(promotionRepo, times(1)).findAll();
    }
}
