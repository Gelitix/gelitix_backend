package com.gelitix.backend.point.service;

import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.users.entity.Users;

public interface PointService {
    Point getPoint(String username);
    Point recordPointHistory (Users inviter , Users invitee);
}
