package org.example.clickservice.controller;

import org.example.clickservice.service.ClickIngestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
public class ClickController {

    private final ClickIngestService clickIngestService;

    public ClickController(ClickIngestService clickIngestService) {
        this.clickIngestService = clickIngestService;
    }

    @GetMapping("/click")
    public ResponseEntity<?> click(
            @RequestParam("adId") String adId,
            @RequestParam("campaignId") String campaignId,
            @RequestParam("impressionId") String impressionId,
            @RequestParam("sig") String sig
    )  {
        try {
            String targetUrl = clickIngestService.process(adId, campaignId, impressionId, sig);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(targetUrl))
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "internal server error"));
        }
    }
}