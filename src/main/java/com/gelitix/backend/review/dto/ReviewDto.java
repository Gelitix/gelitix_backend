package com.gelitix.backend.review.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDto {
    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    @Column(name = "rating")
    private Integer rating;
}
