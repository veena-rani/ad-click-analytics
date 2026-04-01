package org.example.clickservice.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedirectService {

    private final Map<String, String> adTargets = Map.of(
            "ad_1", "https://example.com/product/1",
            "ad_2", "https://example.com/product/2"
    );

    public String resolveTargetUrl(String adId) {
        String url = adTargets.get(adId);
        if (url == null) {
            throw new IllegalArgumentException("unknown adId");
        }
        return url;
    }
}