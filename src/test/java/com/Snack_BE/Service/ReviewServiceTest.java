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

import com.Snack_BE.Model.ReviewEnity;
import com.Snack_BE.Repo.ReviewRepo;

/**
 * Unit Tests for ReviewService
 * Tests review retrieval functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Unit Tests")
class ReviewServiceTest {

    @Mock
    private ReviewRepo reviewRepo;

    @InjectMocks
    private ReviewService reviewService;

    private ReviewEnity testReview1;
    private ReviewEnity testReview2;

    @BeforeEach
    void setUp() {
        // Setup test reviews
        testReview1 = new ReviewEnity();
        
        testReview2 = new ReviewEnity();
    }

    @Test
    @DisplayName("getAllReview should return list when reviews exist")
    void getAllReview_ShouldReturnList_WhenReviewsExist() {
        // Arrange
        List<ReviewEnity> reviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepo.findAll()).thenReturn(reviews);

        // Act
        ResponseEntity<List<ReviewEnity>> response = reviewService.getAllReview();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(reviewRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllReview should return empty list when no reviews")
    void getAllReview_ShouldReturnEmptyList_WhenNoReviews() {
        // Arrange
        when(reviewRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ReviewEnity>> response = reviewService.getAllReview();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(reviewRepo, times(1)).findAll();
    }
}
