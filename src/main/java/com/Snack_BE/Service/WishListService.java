package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.Model.WishlistEntity;
import com.Snack_BE.Repo.WishListRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepo wishListRepo;

    public ResponseEntity<List<WishlistEntity>> getAllWishlist() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(wishListRepo.findAll());
    }
}
