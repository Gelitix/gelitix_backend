package com.gelitix.backend.point.service.impl;

import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.point.repository.PointRepository;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final UserService userService;

    public PointServiceImpl(PointRepository pointRepository, @Lazy UserService userService) {
        this.pointRepository = pointRepository;
        this.userService = userService;
    }

    @Override
    public List<Point> getPoint(String email) {
        Optional<Users> currentUser= userService.getUserByEmail(email);
        if (currentUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid email");
        }
        return pointRepository.findPointsByInviterId(currentUser.get().getId());
    }

    @Override
    public Point recordPointHistory(Users uplineUser, Users savedUser) {
        int pointAwarded = 10000;
        BigDecimal currentBalance = uplineUser.getPointBalance();
        Point addedPoint = new Point();
        addedPoint.setInviter(uplineUser);
        addedPoint.setInvitee(savedUser);
        addedPoint.setPointsHistory(BigDecimal.valueOf(pointAwarded));
        addedPoint = pointRepository.save(addedPoint);

        // Convert LocalDateTime to Instant
        Instant threeMonthsFromNow = LocalDateTime.now().plusMonths(3)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        if (addedPoint.getCreatedAt().isBefore(threeMonthsFromNow)) {
            uplineUser.setPointBalance(uplineUser.getPointBalance().subtract(BigDecimal.valueOf(10000)));
        }
        if (currentBalance.compareTo(BigDecimal.valueOf(pointAwarded)) <= 0) {
            uplineUser.setPointBalance(BigDecimal.ZERO);
        }
        return addedPoint;
    }


    @Override
    public Point deductPointHistory(Users users, BigDecimal pointUsed){
        Point deductedPoint = new Point();
        deductedPoint.setPointsHistory(pointUsed.negate());
        return pointRepository.save(deductedPoint);
    }

}
