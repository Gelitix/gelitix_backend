package com.gelitix.backend.review.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.review.service.ReviewService;
import com.gelitix.backend.users.entity.Users;
import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed("Role_user")
    public ResponseEntity<?> addReview(ReviewDto reviewDto, Order order, Event event) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");

        return Response.success(200, "Review Added",  reviewService.addReview(email, reviewDto, order, event)
 );
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllReviews() {
        return Response.success(200, "Review has been fetched", reviewService.getAllReviews()
        );
    }
}
