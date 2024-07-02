package com.gelitix.backend.review.repository;

import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
Optional<Review> findByEmail (String email);
}
