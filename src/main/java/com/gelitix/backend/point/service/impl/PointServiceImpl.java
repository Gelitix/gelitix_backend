package com.gelitix.backend.point.service.impl;

import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.point.repository.PointRepository;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
        return pointRepository.findPointsByInviterId(Long.valueOf(currentUser.get().getId()));
    }

    @Override
    public Point recordPointHistory(Users uplineUser , Users savedUser) {

        int pointAwarded =10000;
        Point addedPoint = new Point();
        addedPoint.setInviter(uplineUser);
        addedPoint.setInvitee(savedUser);
        addedPoint.setPointsHistory(pointAwarded);
        return pointRepository.save(addedPoint);
    }

}
