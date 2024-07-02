package com.gelitix.backend.review.service;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;

import java.util.Optional;

public interface ReviewService {
    ReviewDto addReview(String email, ReviewDto reviewDto, Order order, Event event);
    Optional<Review> findReview(String email);
}
