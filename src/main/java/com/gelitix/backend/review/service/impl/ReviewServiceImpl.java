package com.gelitix.backend.review.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.review.repository.ReviewRepository;
import com.gelitix.backend.review.service.ReviewService;
import com.gelitix.backend.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    @Override
    public ReviewDto addReview(String email, ReviewDto reviewDto, Order order, Event event) {
        Instant eventDate = event.getDate();
        String orderEmail = order.getUser().getEmail();
        Instant now = Instant.now();
        if(!email.equals(orderEmail) && now.isBefore(eventDate)) {
            throw new IllegalArgumentException("You have to order this event first before reviewing it.");
        }

        Review review = new Review();
        review.setName(reviewDto.getName());
        review.setFeedback(reviewDto.getFeedback());
        review.setRating(reviewDto.getRating());
        reviewRepository.save(review);

//        if(reviewRepository.findByEmail(email).equals(review)) {
//            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
//            }
//        if (reviewRepository.findByEmail(email).isEmpty()){
//            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
//        }

        ReviewDto response = new ReviewDto();
        response.setName(review.getName());
        response.setFeedback(review.getFeedback());
        response.setRating(review.getRating());
        return response;
    }

    @Override
    public Optional<Review> findReview(String email) {
        return reviewRepository.findByEmail(email);
    }
}
