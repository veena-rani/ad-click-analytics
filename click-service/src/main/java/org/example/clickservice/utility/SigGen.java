package org.example.clickservice.utility;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SigGen {
    public static void main(String[] args) throws Exception {
        String adId = "ad_1";
        String impressionId = "test-imp-123";
        String secret = "super-secret-key";

        String payload = adId + "." + impressionId;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String sig = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);

        System.out.println(sig);
    }
}
