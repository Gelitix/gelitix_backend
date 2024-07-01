package com.gelitix.backend.review.service;

import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;

import java.util.Optional;

public interface ReviewService {
    ReviewDto addReview(String username, ReviewDto reviewDto);
    Optional<Review> findReview(String username);
}
