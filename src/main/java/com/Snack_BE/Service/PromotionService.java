package com.Snack_BE.Service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Snack_BE.Model.PromotionEntity;
import com.Snack_BE.Repo.PromotionRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepo promotionRepo;

    public ResponseEntity<List<PromotionEntity>> getAllPromotion() {
        return ResponseEntity.status(201).body(promotionRepo.findAll());
    }
}
