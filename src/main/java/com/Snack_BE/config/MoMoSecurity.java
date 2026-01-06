package com.Snack_BE.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class MoMoSecurity {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String createSignature(String rawData, String secretKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(UTF_8), HMAC_SHA256);
            mac.init(key);
            byte[] rawHmac = mac.doFinal(rawData.getBytes(UTF_8));
            return Hex.encodeHexString(rawHmac);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create MoMo signature", e);
        }
    }
}
