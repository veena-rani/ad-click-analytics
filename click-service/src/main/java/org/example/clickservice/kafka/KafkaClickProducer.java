
package org.example.clickservice.kafka;
import org.example.clickservice.config.AppProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KafkaClickProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AppProperties appProperties;

    public KafkaClickProducer(KafkaTemplate<String, String> kafkaTemplate, AppProperties appProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.appProperties = appProperties;
    }

    public void send(String key, String payload) {
        try {
            kafkaTemplate.send(appProperties.getKafkaTopic(), key, payload).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("kafka send interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("failed to publish click event", e);
        }
    }
}