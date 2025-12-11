package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Service.ProductService;
import com.Snack_BE.util.JwtUtil;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("login")
    public ResponseEntity<String> postMethodName(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "123".equals(password)) {
            return ResponseEntity.ok(JwtUtil.generateToken(username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("null");
        }
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<ProductEntity>> getALLEntity() {
        return productService.getallEntity();
    }

}
