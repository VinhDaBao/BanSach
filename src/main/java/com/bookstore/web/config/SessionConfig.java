package com.bookstore.web.config;

import com.bookstore.web.entity.NguoiDung;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionConfig {

    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("loggedUser");
        model.addAttribute("loggedUser", user);
    }

}
