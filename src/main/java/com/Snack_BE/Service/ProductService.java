package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Repo.ProductRepo;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ResponseEntity<List<ProductEntity>> getallEntity() {
        return ResponseEntity.ok(productRepo.findAll());
    }
}
