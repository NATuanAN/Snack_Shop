package com.Snack_BE.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Snack_BE.config.MoMoSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;

// SỬA LẠI IMPORT Ở ĐÂY
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@RequestMapping("/public/api/payment")
public class MoMoController {

    @GetMapping("/momo")
    public ResponseEntity<?> createMoMoPayment() {
        try {
            // 1. Thông số cơ bản (Môi trường Test)
            String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
            String partnerCode = "MOMOBKUN20180830";
            String accessKey = "8sqDz7ICSTnu28sq";
            String secretKey = "9z2Xm9P9S9N9S9X9S9N9S9X9S9N9S9X9";

            String orderInfo = "Thanh toán đơn hàng #12345";
            String redirectUrl = "http://localhost:8080/api/payment/return";
            String ipnUrl = "https://webhook.site/test";
            String amount = "50000";
            String orderId = String.valueOf(System.currentTimeMillis());
            String requestId = String.valueOf(System.currentTimeMillis());
            String requestType = "captureWallet";
            String extraData = "";

            // 2. Tạo chuỗi ký tự (Raw Hash)
            String rawHash = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            // 3. Ký tên (Signature)
            String signature = MoMoSecurity.createSignature(rawHash, secretKey);

            // 4. Tạo JSON request body
            Map<String, Object> message = new HashMap<>();
            message.put("partnerCode", partnerCode);
            message.put("partnerName", "Test");
            message.put("storeId", "MomoTestStore");
            message.put("requestId", requestId);
            message.put("amount", Long.parseLong(amount)); // MoMo nhận amount kiểu Long
            message.put("orderId", orderId);
            message.put("orderInfo", orderInfo);
            message.put("redirectUrl", redirectUrl);
            message.put("ipnUrl", ipnUrl);
            message.put("lang", "vi");
            message.put("extraData", extraData);
            message.put("requestType", requestType);
            message.put("signature", signature);

            // 5. Gửi POST Request
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(endpoint)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseData = response.body().string();
                return ResponseEntity.ok(responseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}