package com.gelitix.backend.review.service.impl;

import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.review.repository.ReviewRepository;
import com.gelitix.backend.review.service.ReviewService;
import com.gelitix.backend.users.repository.UserRepository;
import org.springframework.stereotype.Service;

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
    public ReviewDto addReview(String username, ReviewDto reviewDto) {
        Review review = new Review();
        review.setName(reviewDto.getName());
        review.setFeedback(reviewDto.getFeedback());
        review.setRating(reviewDto.getRating());
        reviewRepository.save(review);

        if(reviewRepository.findReviewByUserNameContains(username).equals(review)) {
            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
            }
        if (reviewRepository.findReviewByUserNameContains(username).isEmpty()){
            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
        }

        ReviewDto response = new ReviewDto();
        response.setName(review.getName());
        response.setFeedback(review.getFeedback());
        response.setRating(review.getRating());
        return response;
    }

    @Override
    public Optional<Review> findReview(String username) {
        return reviewRepository.findReviewByUserNameContains(username);
    }
}
