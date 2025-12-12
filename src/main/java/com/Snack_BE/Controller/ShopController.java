package com.Snack_BE.Controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.Model.ShopEntity;
import com.Snack_BE.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/shop")
public class ShopController {
    private final ShopService shopService;

    @GetMapping("/allshop")
    public ResponseEntity<List<ShopEntity>> getAllShop() {
        return shopService.getAllShop();
    }
}
