package com.gelitix.backend.review.repository;

import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
