package org.example.queryservice.repository;

import org.example.sharedevents.MetricRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class ClickHouseMetricsRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClickHouseMetricsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MetricRow> findMetrics(String adId, Instant start, Instant end) {
        String sql = """
    SELECT ad_id,
           toUnixTimestamp(minute_bucket) AS minute_bucket_epoch,
           sum(click_count) AS click_count
    FROM ad_click_metrics_minute
    WHERE ad_id = ?
      AND minute_bucket >= toDateTime(?)
      AND minute_bucket < toDateTime(?)
    GROUP BY ad_id, minute_bucket
    ORDER BY minute_bucket
    """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new MetricRow(
                        rs.getString("ad_id"),
                        rs.getLong("minute_bucket_epoch"),
                        rs.getLong("click_count")
                ),
                adId,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC).format(start),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC).format(end)
        );
    }
}