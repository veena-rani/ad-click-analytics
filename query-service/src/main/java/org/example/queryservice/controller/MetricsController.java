package org.example.queryservice.controller;

import org.example.queryservice.service.MetricsQueryService;
import org.example.sharedevents.MetricRow;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsQueryService metricsQueryService;

    public MetricsController(MetricsQueryService metricsQueryService) {
        this.metricsQueryService = metricsQueryService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> metrics(
            @RequestParam("adId") String adId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ) {
        try {
            List<MetricRow> rows = metricsQueryService.getMetrics(adId, start, end);
            return ResponseEntity.ok(Map.of(
                    "adId", adId,
                    "start", start,
                    "end", end,
                    "rows", rows
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "internal server error"));
        }
    }
}