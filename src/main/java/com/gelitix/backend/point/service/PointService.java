package com.gelitix.backend.point.service;

import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.users.entity.Users;

import java.math.BigDecimal;
import java.util.List;

public interface PointService {
    List<Point> getPoint(String email);
    Point recordPointHistory (Users uplineUser , Users savedUser);
    Point deductPointHistory(Users users, BigDecimal pointUsed);
    List<Point> findPointsByInviterId(Long inviterId);
    void updateUserPointBalance(Long userId , BigDecimal pointUsed);
}
