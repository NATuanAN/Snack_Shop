package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.Model.ProductEntity;
import com.Snack_BE.Service.ProductService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductEntity>> getALLEntity() {
        return productService.getallEntity();
    }

}
