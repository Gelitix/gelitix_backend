package com.gelitix.backend.point.scheduler;

import com.gelitix.backend.point.service.PointService;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;

public class UpdatePointScheduler {
    private final PointService pointService;

    public UpdatePointScheduler(PointService pointService) {
        this.pointService = pointService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAllUserPointBalances() {
        pointService.updateAllUserPointBalances();
    }
}
