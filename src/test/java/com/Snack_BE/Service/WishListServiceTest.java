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

import com.Snack_BE.Model.WishlistEntity;
import com.Snack_BE.Repo.WishListRepo;

/**
 * Unit Tests for WishListService
 * Tests wishlist retrieval functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WishListService Unit Tests")
class WishListServiceTest {

    @Mock
    private WishListRepo wishListRepo;

    @InjectMocks
    private WishListService wishListService;

    private WishlistEntity testWishlist1;
    private WishlistEntity testWishlist2;

    @BeforeEach
    void setUp() {
        // Setup test wishlist items
        testWishlist1 = new WishlistEntity();
        testWishlist2 = new WishlistEntity();
    }

    @Test
    @DisplayName("getAllWishlist should return list when wishlists exist")
    void getAllWishlist_ShouldReturnList_WhenWishlistsExist() {
        // Arrange
        List<WishlistEntity> wishlists = Arrays.asList(testWishlist1, testWishlist2);
        when(wishListRepo.findAll()).thenReturn(wishlists);

        // Act
        ResponseEntity<List<WishlistEntity>> response = wishListService.getAllWishlist();

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(wishListRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllWishlist should return empty list when no wishlists")
    void getAllWishlist_ShouldReturnEmptyList_WhenNoWishlists() {
        // Arrange
        when(wishListRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<WishlistEntity>> response = wishListService.getAllWishlist();

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(wishListRepo, times(1)).findAll();
    }
}
