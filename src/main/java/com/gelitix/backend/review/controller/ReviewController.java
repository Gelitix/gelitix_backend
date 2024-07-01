package com.gelitix.backend.review.controller;

import com.gelitix.backend.response.Response;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.review.service.ReviewService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @PostMapping("/")
    public ResponseEntity<?> addReview(ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        String username = authentication.getName();
        reviewService.addReview(username, reviewDto);
        return Response.success(200, "Review Added", reviewService.findReview(username)
 );
    }

    @GetMapping("/")
    public ResponseEntity<?> findReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        String username = authentication.getName();
        return Response.success(200, "Review has been fetched", reviewService.findReview(username));
    }
}
