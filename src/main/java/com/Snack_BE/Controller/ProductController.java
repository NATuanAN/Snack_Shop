package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Service.ProductService;
import com.Snack_BE.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<List<ProductEntity>> getALLEntity() {
        return productService.getallEntity();
    }

}
