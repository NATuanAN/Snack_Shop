package com.Snack_BE.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Snack_BE.Model.ReviewEnity;
import com.Snack_BE.Repo.ReviewRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepo reviewRepo;

    public ResponseEntity<List<ReviewEnity>> getAllReview() {
        return ResponseEntity.status(200).body(reviewRepo.findAll());
    }
}
