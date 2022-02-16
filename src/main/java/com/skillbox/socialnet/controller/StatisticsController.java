package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rs.StatisticsResponse;
import com.skillbox.socialnet.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatisticsForUser() {
        return ResponseEntity.ok(statisticsService.getStatisticsResponse());
    }
}