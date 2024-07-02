package com.gelitix.backend.point.service.impl;

import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.point.repository.PointRepository;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointServiceImpl(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Point getPoint(String username) {
        return pointRepository.findPointsByUsername(username);
    }

    @Override
    public Point recordPointHistory(Users inviter , Users invitee) {

        int pointAwarded =10000;
        Point addedPoint = new Point();
        addedPoint.setInviter(inviter);
        addedPoint.setInvitee(invitee);
        addedPoint.setPointsHistory(pointAwarded);
        return pointRepository.save(addedPoint);
    }
}
