package com.bookstore.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bookstore.web.service.CartService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addCartItemCount(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        int cartItemCount = cartService.countCartItems(userId);
        model.addAttribute("cartItemCount", cartItemCount);
    }
}