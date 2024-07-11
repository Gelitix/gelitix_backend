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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
   public List<Point> findPointsByInviterId(Long inviterId){
        return pointRepository.findPointsByInviterId(inviterId);
    }

    @Override
    public Point recordPointHistory(Users uplineUser , Users savedUser) {

        int pointAwarded =10000;
        Instant threeMonthsFromNow = Instant.now().plus(3, ChronoUnit.MONTHS);


        BigDecimal currentBalance = uplineUser.getPointBalance();
        Point addedPoint = new Point();
        addedPoint.setInviter(uplineUser);
        addedPoint.setInvitee(savedUser);
        addedPoint.setPointsHistory(BigDecimal.valueOf(pointAwarded));
        addedPoint.setExpiredAt(Instant.from(threeMonthsFromNow));
        addedPoint.setRemainingPoint(BigDecimal.valueOf(10000));
        pointRepository.save(addedPoint);
//        if (addedPoint.getCreatedAt().isAfter(Instant.from(threeMonthsFromNow)) ){
//            uplineUser.setPointBalance(uplineUser.getPointBalance().subtract(BigDecimal.valueOf(10000)));
//        }if(currentBalance.compareTo(BigDecimal.valueOf(pointAwarded))<=0){
//            uplineUser.setPointBalance(BigDecimal.ZERO);
//        }
        return addedPoint;


    }
    @Override
    public Point deductPointHistory(Users users, BigDecimal pointUsed){
        Point deductedPoint = new Point();
        deductedPoint.setPointsHistory(pointUsed.negate());
        return pointRepository.save(deductedPoint);
    }
    @Override
    public void updateUserPointBalance(Long userId , BigDecimal pointUsed) {
        List<Point> sortedHistory = pointRepository.findAllByUserIdOrderByDateAsc(userId);

        for (Point point : sortedHistory) {
            while (pointUsed.compareTo(BigDecimal.ZERO) > 0) {
                if (point.getRemainingPoint().compareTo(pointUsed)>0) {
                point.setRemainingPoint(point.getRemainingPoint().subtract(pointUsed));
                pointUsed = BigDecimal.valueOf(0);
            } else if (point.getRemainingPoint().compareTo(pointUsed)<0) {
                pointUsed= pointUsed.subtract(point.getRemainingPoint());
                point.setRemainingPoint(BigDecimal.valueOf(0));
            }}

        }
        BigDecimal pointBalance = BigDecimal.ZERO;
        for (Point point : sortedHistory) {
            pointBalance.add(point.getRemainingPoint());
        }
        userService.findById(userId).setPointBalance(pointBalance);

   }
}
