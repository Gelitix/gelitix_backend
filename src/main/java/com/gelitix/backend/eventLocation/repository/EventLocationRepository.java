package com.gelitix.backend.eventLocation.repository;

import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;


public interface EventLocationRepository extends JpaRepository<EventLocation, Long> {
}
