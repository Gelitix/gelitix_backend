package com.gelitix.backend.dashboard.service;

import com.gelitix.backend.dashboard.dto.EventStatisticsDTO;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
EventStatisticsDTO getEventStatistics(String email);
}
