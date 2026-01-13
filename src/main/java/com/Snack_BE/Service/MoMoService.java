package com.Snack_BE.Service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.Snack_BE.config.MoMoSecurity;
import com.Snack_BE.DTOs.MomoConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Snack_BE.DTOs.PaymentRequest;
import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderEntity.OrderStatus;
import com.Snack_BE.Repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class MoMoService implements PaymentService {
    private final MomoConfig momoConfig;
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final OrderRepo orderRepo;

    @Override
    public Object createPayment(PaymentRequest req) {
        try {
            if (req == null || req.getOrderId() == null || req.getAmount() == null) {
                throw new IllegalArgumentException("PaymentRequest, OrderId, and Amount cannot be null");
            }

            String requestId = String.valueOf(System.currentTimeMillis());
            String orderId = req.getOrderId().toString();
            String amount = req.getAmount().toString();

            String rawHash = "accessKey=" + momoConfig.getAccessKey()
                    + "&amount=" + amount
                    + "&extraData="
                    + "&ipnUrl=" + momoConfig.getIpnUrl()
                    + "&orderId=" + orderId
                    + "&orderInfo=Thanh toán đơn hàng #" + orderId
                    + "&partnerCode=" + momoConfig.getPartnerCode()
                    + "&redirectUrl=" + momoConfig.getRedirectUrl()
                    + "&requestId=" + requestId
                    + "&requestType=captureWallet";

            String signature = MoMoSecurity.createSignature(rawHash, momoConfig.getSecretKey());

            Map<String, Object> body = new HashMap<>();
            body.put("partnerCode", momoConfig.getPartnerCode());
            body.put("requestId", requestId);
            body.put("orderId", orderId);
            body.put("amount", req.getAmount());
            body.put("orderInfo", "Thanh toán đơn hàng #" + orderId);
            body.put("redirectUrl", momoConfig.getRedirectUrl());
            body.put("ipnUrl", momoConfig.getIpnUrl());
            body.put("requestType", "captureWallet");
            body.put("autoCapture", true);
            body.put("lang", "vi");
            body.put("signature", signature);
            body.put("extraData", "");

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String json = mapper.writeValueAsString(body);

            RequestBody requestBody = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(momoConfig.getEndpoint())
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return mapper.readValue(response.body().string(), Map.class);
            }

        } catch (Exception e) {
            throw new RuntimeException("Create MoMo payment failed", e);
        }
    }

    @Override
    public void handleIPN(Map<String, Object> payload) {
        Integer resultCode = (Integer) payload.get("resultCode");
        String orderId = payload.get("orderId").toString();
        OrderEntity orderEntity = orderRepo.findById(java.util.UUID.fromString(orderId))
                .orElseThrow(() -> new IllegalAccessError("Order " + orderId + " invalid IPN"));

        if (resultCode == 0) {
            orderEntity.setStatus(OrderStatus.PAID);
            System.out.println("Order " + orderId + " is PAIDED");

        } else {
            orderEntity.setStatus(OrderStatus.FAILED);
            System.out.println("Order " + orderId + " is FAILED");
        }
        orderRepo.save(orderEntity);
    }
}
