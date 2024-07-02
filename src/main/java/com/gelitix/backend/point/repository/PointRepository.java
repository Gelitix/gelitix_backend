package com.gelitix.backend.point.repository;

import com.gelitix.backend.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findPointsByUsername(String username);
    Point findPointsByPointId(Long pointId);
}
