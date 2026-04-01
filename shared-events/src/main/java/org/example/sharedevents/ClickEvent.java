package org.example.sharedevents;

import java.io.Serializable;

public class ClickEvent implements Serializable {
    private String eventId;
    private String adId;
    private String campaignId;
    private String impressionId;
    private long eventTimeMillis;
    private String targetUrl;
    private String signature;

    public ClickEvent() {
    }

    public ClickEvent(String eventId, String adId, String campaignId, String impressionId,
                      long eventTimeMillis, String targetUrl, String signature) {
        this.eventId = eventId;
        this.adId = adId;
        this.campaignId = campaignId;
        this.impressionId = impressionId;
        this.eventTimeMillis = eventTimeMillis;
        this.targetUrl = targetUrl;
        this.signature = signature;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public void setImpressionId(String impressionId) {
        this.impressionId = impressionId;
    }

    public long getEventTimeMillis() {
        return eventTimeMillis;
    }

    public void setEventTimeMillis(long eventTimeMillis) {
        this.eventTimeMillis = eventTimeMillis;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "ClickEvent{" +
                "eventId='" + eventId + '\'' +
                ", adId='" + adId + '\'' +
                ", campaignId='" + campaignId + '\'' +
                ", impressionId='" + impressionId + '\'' +
                ", eventTimeMillis=" + eventTimeMillis +
                ", targetUrl='" + targetUrl + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}