package com.bookstore.web.controller;

import com.bookstore.web.service.MomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class MomoController {

    @Autowired
    private MomoService momoService;

    @GetMapping("/create")
    public String createPayment(@RequestParam(defaultValue = "50000") long amount) {
        String payUrl = momoService.createPayment(amount);
        return "redirect:" + payUrl;
    }

    @GetMapping("/result")
    @ResponseBody
    public String paymentResult(
            @RequestParam("orderId") String orderId,
            @RequestParam("resultCode") int resultCode
    ) {
        boolean success = momoService.handlePaymentResult(orderId, resultCode);
        return success ? "Thanh toán thành công!" : "Thanh toán thất bại!";
    }

    @PostMapping("/notify")
    @ResponseBody
    public String notifyPayment(@RequestBody String jsonData) {
        momoService.handleNotify(jsonData);
        return "{\"message\": \"IPN received\"}";
    }
}
