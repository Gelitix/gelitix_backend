package com.gelitix.backend.order.dao;

import com.gelitix.backend.event.entity.Event;
import lombok.Data;

public interface EventAttendeeCountDao {
    Event getEvent();
    Long getAttendeeCount();
}
