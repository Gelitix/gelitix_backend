package com.gelitix.backend.review.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.service.ReviewService;
import jakarta.annotation.security.RolesAllowed;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/v1/review")

public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @RolesAllowed("ROLE_USER")
    @PostMapping("/{eventId}")
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto, @PathVariable Long eventId) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");

        return Response.success(200, "Review Added",  reviewService.addReview(email, reviewDto, eventId)
 );
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllReviews() {
        return Response.success(200, "Review has been fetched", reviewService.getAllReviews()
        );
    }
}
