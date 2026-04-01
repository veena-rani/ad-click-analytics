package org.example.clickservice.service;

import org.example.clickservice.config.AppProperties;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ImpressionSignatureService {

    private final AppProperties appProperties;

    public ImpressionSignatureService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public boolean isValid(String adId, String impressionId, String signature) {
        return sign(adId, impressionId).equals(signature);
    }

    public String sign(String adId, String impressionId) {
        try {
            String payload = adId + "." + impressionId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appProperties.getHmacSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        } catch (Exception e) {
            throw new RuntimeException("failed to sign impression", e);
        }
    }
}