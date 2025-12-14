package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.Model.ReviewEnity;
import com.Snack_BE.Service.ReviewService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/public/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/all")
    public ResponseEntity<List<ReviewEnity>> getAllReview() {
        return reviewService.getAllReview();
    }
}
