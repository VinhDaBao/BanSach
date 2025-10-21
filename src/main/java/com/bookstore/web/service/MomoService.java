package com.bookstore.web.service;

public interface MomoService {
    String createPayment(long amount);
    boolean handlePaymentResult(String orderId, int resultCode);
    void handleNotify(String jsonData);
}
