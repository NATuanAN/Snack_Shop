package com.Snack_BE.Service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Repo.ProductRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public ResponseEntity<List<ProductDTO>> getallEntity() {
        return ResponseEntity.ok(productRepo.findAllProductDTO());
    }
}
