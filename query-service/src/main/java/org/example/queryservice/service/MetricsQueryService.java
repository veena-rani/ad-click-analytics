package org.example.queryservice.service;


import org.example.queryservice.repository.ClickHouseMetricsRepository;
import org.example.sharedevents.MetricRow;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MetricsQueryService {

    private final ClickHouseMetricsRepository repository;

    public MetricsQueryService(ClickHouseMetricsRepository repository) {
        this.repository = repository;
    }

    public List<MetricRow> getMetrics(String adId, Instant start, Instant end) {
        if (adId == null || adId.isBlank()) {
            throw new IllegalArgumentException("adId is required");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end are required");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("invalid time range");
        }
        return repository.findMetrics(adId, start, end);
    }
}
