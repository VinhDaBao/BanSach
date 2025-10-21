package com.bookstore.web.service.impl;

import com.bookstore.web.config.MomoConfig;
import com.bookstore.web.service.MomoService;
import com.bookstore.web.service.Hmac;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MomoServiceImpl implements MomoService {

    @Autowired
    private MomoConfig momoConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String createPayment(long amount) {
        try {
            String orderId = UUID.randomUUID().toString();
            String requestId = UUID.randomUUID().toString();
            String orderInfo = "Thanh toan don hang " + orderId;

            String rawSignature = String.format(
                    "accessKey=%s&amount=%d&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    momoConfig.getAccessKey(), amount, momoConfig.getIpnUrl(),
                    orderId, orderInfo, momoConfig.getPartnerCode(),
                    momoConfig.getRedirectUrl(), requestId, "captureWallet"
            );

            String signature = Hmac.hmacSHA256(momoConfig.getSecretKey(), rawSignature);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("partnerCode", momoConfig.getPartnerCode());
            body.put("accessKey", momoConfig.getAccessKey());
            body.put("requestId", requestId);
            body.put("amount", amount);
            body.put("orderId", orderId);
            body.put("orderInfo", orderInfo);
            body.put("redirectUrl", momoConfig.getRedirectUrl());
            body.put("ipnUrl", momoConfig.getIpnUrl());
            body.put("requestType", "captureWallet");
            body.put("extraData", "");
            body.put("signature", signature);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(momoConfig.getEndpoint(), request, Map.class);
            Map<String, Object> result = response.getBody();

            if (result != null && result.containsKey("payUrl")) {
                return result.get("payUrl").toString();
            } else {
                throw new RuntimeException("Không nhận được payUrl từ MoMo: " + new ObjectMapper().writeValueAsString(result));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo thanh toán MoMo", e);
        }
    }

    @Override
    public boolean handlePaymentResult(String orderId, int resultCode) {
        // 0 = success, khác 0 = fail
        if (resultCode == 0) {
            System.out.println("Thanh toán thành công cho đơn hàng: " + orderId);
            // TODO: cập nhật trạng thái đơn hàng trong DB
            return true;
        } else {
            System.out.println("Thanh toán thất bại cho đơn hàng: " + orderId);
            return false;
        }
    }

    @Override
    public void handleNotify(String jsonData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(jsonData, Map.class);

            String orderId = (String) payload.get("orderId");
            Integer resultCode = (Integer) payload.get("resultCode");

            if (resultCode != null && resultCode == 0) {
                System.out.println("✅ [IPN] Thanh toán thành công cho đơn hàng: " + orderId);
                // TODO: cập nhật DB
            } else {
                System.out.println("❌ [IPN] Thanh toán thất bại cho đơn hàng: " + orderId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
