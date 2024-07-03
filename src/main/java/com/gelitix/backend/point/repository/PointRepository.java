package com.gelitix.backend.point.repository;

import com.gelitix.backend.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
//    Point findPointsByUsername(String username);
//    Point findPointsByPointId(Long pointId);
    List<Point> findPointsByInviterId(Long inviterId);
}
