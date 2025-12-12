package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.Model.ShopEntity;
import com.Snack_BE.Repo.ShopRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepo shopRepo;

    public ResponseEntity<List<ShopEntity>> getAllShop() {
        return ResponseEntity.status(201).body(shopRepo.findAll());
    }
}
