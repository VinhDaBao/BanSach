package com.bookstore.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.entity.DiaChi;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.service.DiaChiService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/addresses")
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    @GetMapping
    public String viewAddresses(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<DiaChi> addresses = diaChiService.getByUserId(userId);
        model.addAttribute("addresses", addresses);
        model.addAttribute("pageTitle", "Địa chỉ giao hàng");

        return "user/addresses";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("diaChi", new DiaChi());
        model.addAttribute("pageTitle", "Thêm địa chỉ mới");
        return "user/address-form";
    }

    @PostMapping("/add")
    public String addAddress(@ModelAttribute DiaChi diaChi,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setId(userId);
            diaChi.setNguoiDung(nguoiDung);

            // Nếu là địa chỉ đầu tiên, tự động set mặc định
            List<DiaChi> existing = diaChiService.getByUserId(userId);
            if (existing.isEmpty()) {
                diaChi.setMacDinh(true);
            }

            diaChiService.save(diaChi);
            redirectAttributes.addFlashAttribute("success", "Thêm địa chỉ thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/user/addresses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, 
                              Model model, 
                              HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        DiaChi diaChi = diaChiService.findById(id);
        if (diaChi == null || !diaChi.getNguoiDung().getId().equals(userId)) {
            return "redirect:/user/addresses";
        }

        model.addAttribute("diaChi", diaChi);
        model.addAttribute("pageTitle", "Sửa địa chỉ");
        return "user/address-form";
    }

    @PostMapping("/edit/{id}")
    public String editAddress(@PathVariable("id") Integer id,
                             @ModelAttribute DiaChi diaChi,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            DiaChi existing = diaChiService.findById(id);
            if (existing == null || !existing.getNguoiDung().getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy địa chỉ");
                return "redirect:/user/addresses";
            }

            diaChi.setId(id);
            diaChi.setNguoiDung(existing.getNguoiDung());
            diaChiService.save(diaChi);
            redirectAttributes.addFlashAttribute("success", "Cập nhật địa chỉ thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/user/addresses";
    }

    @PostMapping("/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            DiaChi diaChi = diaChiService.findById(id);
            if (diaChi != null && diaChi.getNguoiDung().getId().equals(userId)) {
                diaChiService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa địa chỉ thành công");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/user/addresses";
    }

    @PostMapping("/set-default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            diaChiService.setDefaultAddress(userId, id);
            redirectAttributes.addFlashAttribute("success", "Đã đặt làm địa chỉ mặc định");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/user/addresses";
    }
}