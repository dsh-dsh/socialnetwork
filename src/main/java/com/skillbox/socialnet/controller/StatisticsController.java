package com.skillbox.socialnet.controller;

import com.skillbox.socialnet.model.rs.StatisticsResponse;
import com.skillbox.socialnet.service.StatisticsService;
import com.skillbox.socialnet.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = Constants.ALLOW_ORIGIN)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatisticsForUser() {
        return ResponseEntity.ok(statisticsService.getStatisticsResponse());
    }
}
