package com.Snack_BE.Controller;

import com.Snack_BE.DTOs.ProductResquestDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Service.ProductService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("public/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getALLEntity() {
        return productService.getAllEntity();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('Seller') or hasRole('Admin')")
    public ResponseEntity<?> addNewProduct(@RequestBody ProductResquestDTO body, Authentication authentication) {
        Long userId=(Long) authentication.getDetails();
        return productService.addNewProduct(body,userId);
    }

    @GetMapping("/getByUser")
    @PreAuthorize("hasRole('Seller') or hasRole('Admin')")
    public ResponseEntity<?> getByUser(Authentication authentication){
        return productService.getByUser((Long)authentication.getDetails());
    }
}
