package com.bookstore.web.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.service.DonHangService;


@Controller
public class DonHangController {
    @Autowired
    private DonHangService donHangService;

    @GetMapping("/user/orders")  
    public String orderHistory(Model model, HttpSession session) { 
        NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";  
        }
        
        Integer currentUserId = loggedUser.getId();  

        List<DonHang> orderList = donHangService.getOrdersByUserId(currentUserId);

        model.addAttribute("orderList", orderList);
        model.addAttribute("pageTitle", "Lịch sử mua hàng");
        
        return "user/orderHistory"; 
    }
    @GetMapping("/order-detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId, Model model, HttpSession session) {
        NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
        System.out.println("DEBUG orderDetail: loggedUser ID=" + (loggedUser != null ? loggedUser.getId() : "NULL"));
        System.out.println("DEBUG Session ID=" + session.getId());  
        
        if (loggedUser == null) {
            System.out.println("DEBUG Redirect to login - session lost");  
            return "redirect:/login";  
        }
        
        Integer currentUserId = loggedUser.getId();
        try {
            DonHang order = donHangService.findById(orderId);
            if (order == null || !order.getNguoiDung().getId().equals(currentUserId)) {
                model.addAttribute("errorMessage", "Đơn hàng không tồn tại hoặc bạn không có quyền xem.");
                return "user/orderHistory";  
            }
            model.addAttribute("order", order);
            model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + orderId);
            
            return "user/orderDetail";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra khi xem chi tiết đơn hàng.");
            return "redirect:/user/orders";
        }
    }
    
    @PostMapping("/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Integer orderId, RedirectAttributes redirectAttributes, HttpSession session) {
        NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";  
        }
        
        Integer currentUserId = loggedUser.getId();
        try {
           
            DonHang order = donHangService.findById(orderId);  
            if (order == null || !order.getNguoiDung().getId().equals(currentUserId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền hủy đơn hàng này.");
                return "redirect:/user/orders";
            }
            
            donHangService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy đơn hàng #" + orderId + " thành công.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã có lỗi xảy ra khi hủy đơn hàng.");
        }
        return "redirect:/user/orders";
    }
}