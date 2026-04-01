package org.example.sharedevents;

import java.io.Serializable;
public class MetricRow implements Serializable {
    private String adId;
    private long minuteBucketEpoch;
    private long clickCount;

    public MetricRow() {
    }

    public MetricRow(String adId, long minuteBucketEpoch, long clickCount) {
        this.adId = adId;
        this.minuteBucketEpoch = minuteBucketEpoch;
        this.clickCount = clickCount;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public long getMinuteBucketEpoch() {
        return minuteBucketEpoch;
    }

    public void setMinuteBucketEpoch(long minuteBucketEpoch) {
        this.minuteBucketEpoch = minuteBucketEpoch;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public String toString() {
        return "MetricRow{" +
                "adId='" + adId + '\'' +
                ", minuteBucketEpoch=" + minuteBucketEpoch +
                ", clickCount=" + clickCount +
                '}';
    }
}