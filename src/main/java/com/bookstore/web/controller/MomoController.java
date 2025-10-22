package com.bookstore.web.controller;

import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.repository.DonHangRepository;
import com.bookstore.web.service.MomoService;
import com.bookstore.web.service.impl.DonHangServiceImpl;

import jakarta.servlet.http.HttpSession;

import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class MomoController {
	
    private final DonHangServiceImpl donHangServiceImpl;

    @Autowired
    private MomoService momoService;

    @Autowired
    private DonHangRepository donHangRepository;

    MomoController(DonHangServiceImpl donHangServiceImpl) {
        this.donHangServiceImpl = donHangServiceImpl;
    }
    @GetMapping("/create")

    public String createPayment(@RequestParam("orderId") long orderId) {
        DonHang order = donHangRepository.findById((int) orderId).orElse(null);
        if(order == null)
            return "redirect:/";
        long amountLong = order.getTongTien().setScale(0, RoundingMode.HALF_UP).longValue();

        String payUrl = momoService.createPayment(orderId, amountLong );
        return "redirect:" + payUrl;
    }

    @GetMapping("/result")
    public String paymentResult(
            @RequestParam("orderId") String momoOrderId,
            @RequestParam("resultCode") int resultCode,
            HttpSession session
    ) {
        try {
            String rawId = momoOrderId.split("_")[0];
            DonHang order = donHangRepository.findById((Integer.parseInt(rawId))).orElse(null);
            NguoiDung user = order.getNguoiDung();
            session.setAttribute("loggedUser", user);
            boolean success = momoService.handlePaymentResult(rawId, resultCode);

            if (success) {
                return "redirect:/user/orders?success=true";
            } else {
                return "redirect:/user/orders?error=payment_failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/orders?error=invalid_order";
        }
    }

    @PostMapping("/notify")
    @ResponseBody
    public String notifyPayment(@RequestBody String jsonData) {
        momoService.handleNotify(jsonData);
        return "{\"message\": \"IPN received\"}";
    }

}
