package com.gelitix.backend.review.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.review.dto.ReviewDto;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.review.repository.ReviewRepository;
import com.gelitix.backend.review.service.ReviewService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import com.gelitix.backend.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final EventService eventService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, OrderService orderService, UserService userService, EventService eventService) {
        this.reviewRepository = reviewRepository;
        this.orderService = orderService;
        this.userService = userService;
        this.eventService = eventService;
    }

//    @Override
//    public ReviewDto addReview(String email, ReviewDto reviewDto, Long eventId) {
//        Event event = eventService.getEventEntityById(eventId);
//        Instant eventDate = event.getDate();
//        Users currentuser = userService.getUserByEmail(email).orElseThrow(()-> new IllegalArgumentException("user not found"));
//        Long userId = currentuser.getId();
//        List<Order> checkOrderList = orderService.findOrdersByUserId(userId);
//
//        boolean hasBoughtEvent = checkOrderList.stream()
//                .anyMatch(order -> {
//                    boolean matches = order.getEvent().getId().equals(eventId);
//                    log.debug("Comparing order event {} with given event {}. Matches: {}",
//                            order.getEvent().getId(), eventId, matches);
//                    return matches;
//                });
//        ;
//        Instant now = Instant.now();
//
//        if (!hasBoughtEvent){
//            throw new IllegalArgumentException("You have to order this event first before reviewing it.");
//        }else if (now.isBefore(eventDate)) {
//            throw new IllegalArgumentException("Please wait until the event is finished.");
//        }
//
//
//        Review review = new Review();
//        review.setName(reviewDto.getName());
//        review.setFeedback(reviewDto.getFeedback());
//        review.setRating(reviewDto.getRating());
//        reviewRepository.save(review);
//
////        if(reviewRepository.findByEmail(email).equals(review)) {
////            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
////            }
////        if (reviewRepository.findByEmail(email).isEmpty()){
////            throw new IllegalArgumentException("Something went wrong. Please enter your review again.");
////        }
//
//        ReviewDto response = new ReviewDto();
//        response.setName(review.getName());
//        response.setFeedback(review.getFeedback());
//        response.setRating(review.getRating());
//        return response;
//    }

    @Override
    public ReviewDto addReview(String email, ReviewDto reviewDto, Long eventId) {
        Event event = eventService.getEventEntityById(eventId);
        Instant eventDate = event.getDate();
        Users currentUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long userId = currentUser.getId();
        List<Order> checkOrderList = orderService.findOrdersByUserId(userId);

        Order userOrder = checkOrderList.stream()
                .filter(order -> order.getEvent().getId().equals(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("You have to order this event first before reviewing it."));

        Instant now = Instant.now();

        if (now.isBefore(eventDate)) {
            throw new IllegalArgumentException("Please wait until the event is finished.");
        }

        Review review = new Review();
        review.setName(reviewDto.getName());
        review.setFeedback(reviewDto.getFeedback());
        review.setRating(reviewDto.getRating());
        review.setUser(currentUser);
        review.setOrder(userOrder);

        Review savedReview = reviewRepository.save(review);

        ReviewDto response = new ReviewDto();
        response.setName(savedReview.getName());
        response.setFeedback(savedReview.getFeedback());
        response.setRating(savedReview.getRating());
        return response;
    }



    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
