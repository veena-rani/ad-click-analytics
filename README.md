# Ad Click Aggregation System

## Overview

This project implements a **real-time ad click aggregation pipeline** using Kafka, Flink, ClickHouse, and Redis.

The system ingests ad click events, deduplicates them, aggregates clicks per minute, stores aggregated metrics in ClickHouse, and exposes an API to query metrics.

---

## Architecture

```
Click Service → Kafka → Flink → ClickHouse → Query Service
                      ↓
                    Redis (Deduplication)
```

Redis is used for click deduplication.

---

## Services

### 1. Click Service

* Accepts ad click requests
* Generates impression signature
* Deduplicates clicks using Redis
* Publishes click event to Kafka topic `ad-clicks`
* Redirects user to landing page

### 2. Kafka

* Message broker for click events
* Topic: `ad-clicks`

### 3. Flink Job

* Consumes click events from Kafka
* Performs 1-minute tumbling window aggregation
* Computes click count per ad per minute
* Writes aggregated metrics to ClickHouse

### 4. ClickHouse

* Stores aggregated metrics
* Table: `ad_click_metrics_minute`

### 5. Query Service

* REST API to query metrics by `ad_id` and time range

### 6. Redis

* Used for click deduplication

---

## How to Run

### 1. Build JARs

```bash
mvn clean package
```

### 2. Start Infrastructure

```bash
docker-compose down -v
docker-compose up --build
```

### 3. Submit Flink Job

Job is automatically submitted by the `job-submitter` container.

### 4. Test Click API

```
http://localhost:<click-service-port>/click?adId=ad_1&impressionId=imp_1
```

### 5. Query Metrics

```
http://localhost:<query-service-port>/metrics?adId=ad_1&start=2024-01-01T00:00:00Z&end=2024-01-01T01:00:00Z
```

---

## Kafka Topic

```
ad-clicks
```

---

## ClickHouse Table

```
analytics.ad_click_metrics_minute
```

---

## Tech Stack

* Java
* Spring Boot
* Apache Kafka
* Apache Flink
* ClickHouse
* Redis
* Docker

