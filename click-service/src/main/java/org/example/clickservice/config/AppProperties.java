package org.example.clickservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String kafkaTopic = "ad-clicks";
    private String hmacSecret = "super-secret-key";
    private long dedupTtlSeconds = 86400;

    public String getKafkaTopic() { return kafkaTopic; }
    public void setKafkaTopic(String kafkaTopic) { this.kafkaTopic = kafkaTopic; }

    public String getHmacSecret() { return hmacSecret; }
    public void setHmacSecret(String hmacSecret) { this.hmacSecret = hmacSecret; }

    public long getDedupTtlSeconds() { return dedupTtlSeconds; }
    public void setDedupTtlSeconds(long dedupTtlSeconds) { this.dedupTtlSeconds = dedupTtlSeconds; }
}