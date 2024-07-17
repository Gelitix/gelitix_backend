package com.gelitix.backend.point.repository;

import com.gelitix.backend.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
//    Point findPointsByUsername(String username);
//    Point findPointsByPointId(Long pointId);
    List<Point> findPointsByInviterId(Long inviterId);

    @Query("SELECT p FROM Point p WHERE p.inviter.id = :userId AND p.expiredAt <= CURRENT_DATE  ORDER BY p.expiredAt ASC")
    List<Point> findAllByUserIdOrderByDateAsc(@Param("userId") Long userId);

}

