package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.Model.PromotionEntity;
import com.Snack_BE.Service.PromotionService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("all")
    public ResponseEntity<List<PromotionEntity>> getAllPromotion() {
        return promotionService.getAllPromotion();
    }

}
