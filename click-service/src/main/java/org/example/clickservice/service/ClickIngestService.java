package org.example.clickservice.service;

import org.example.clickservice.config.AppProperties;
import org.example.clickservice.kafka.KafkaClickProducer;
import org.example.sharedevents.ClickEvent;
import org.example.sharedevents.EventSerde;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClickIngestService {

    private final ImpressionSignatureService signatureService;
    private final RedisDedupService redisDedupService;
    private final RedirectService redirectService;
    private final KafkaClickProducer kafkaClickProducer;
    private final AppProperties appProperties;

    public ClickIngestService(
            ImpressionSignatureService signatureService,
            RedisDedupService redisDedupService,
            RedirectService redirectService,
            KafkaClickProducer kafkaClickProducer,
            AppProperties appProperties
    ) {
        this.signatureService = signatureService;
        this.redisDedupService = redisDedupService;
        this.redirectService = redirectService;
        this.kafkaClickProducer = kafkaClickProducer;
        this.appProperties = appProperties;
    }

    public String process(String adId, String campaignId, String impressionId, String signature) {
        if (!signatureService.isValid(adId, impressionId, signature)) {
            throw new IllegalArgumentException("invalid signature");
        }

        String targetUrl = redirectService.resolveTargetUrl(adId);

        if (redisDedupService.alreadySeen(impressionId)) {
            return targetUrl;
        }

        ClickEvent event = new ClickEvent(
                UUID.randomUUID().toString(),
                adId,
                campaignId,
                impressionId,
                System.currentTimeMillis(),
                targetUrl,
                signature
        );

        kafkaClickProducer.send(adId, EventSerde.toJson(event));
        redisDedupService.markSeen(impressionId, appProperties.getDedupTtlSeconds());

        return targetUrl;
    }
}