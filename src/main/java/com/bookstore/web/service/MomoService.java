package com.bookstore.web.service;

import java.math.BigDecimal;

public interface MomoService {
    String createPayment(long orderId,long amount);
    boolean handlePaymentResult(String orderId, int resultCode);
    void handleNotify(String jsonData);
}
