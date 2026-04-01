CREATE TABLE IF NOT EXISTS ad_click_metrics_minute
(
    ad_id String,
    minute_bucket DateTime,
    click_count UInt64
)
ENGINE = SummingMergeTree(click_count)
ORDER BY (ad_id, minute_bucket);