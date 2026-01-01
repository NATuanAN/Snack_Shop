package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.Model.WishlistEntity;
import com.Snack_BE.Service.WishListService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish-list")
public class WishListController {
    private final WishListService wishListService;

    @GetMapping("/all")
    public ResponseEntity<List<WishlistEntity>> getAllWishList() {
        return wishListService.getAllWishlist();
    }

}
