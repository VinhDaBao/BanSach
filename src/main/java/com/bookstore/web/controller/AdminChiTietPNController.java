package com.bookstore.web.controller;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.ChiTietPNKey;
import com.bookstore.web.entity.PhieuNhap;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.service.ChiTietPNService;
import com.bookstore.web.service.PhieuNhapService;
import com.bookstore.web.service.SachService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/chitietpn")
public class AdminChiTietPNController {

    private final ChiTietPNService chiTietPNService;
    private final PhieuNhapService phieuNhapService;
    private final SachService sachService;

    public AdminChiTietPNController(ChiTietPNService chiTietPNService,
                                    PhieuNhapService phieuNhapService,
                                    SachService sachService) {
        this.chiTietPNService = chiTietPNService;
        this.phieuNhapService = phieuNhapService;
        this.sachService = sachService;
    }

    // Danh sách chi tiết theo mã phiếu nhập
    @GetMapping("/phieu/{maPN}")
    public String listByPhieu(@PathVariable("maPN") Integer maPN, Model model) {
        PhieuNhap pn = phieuNhapService.findById(maPN);
        model.addAttribute("phieuNhap", pn);
        model.addAttribute("chiTietList", pn.getChiTietPNList());
        return "admin/chitietpn/list";
    }

    // Form thêm chi tiết
    @GetMapping("/create/{maPN}")
    public String createForm(@PathVariable("maPN") Integer maPN, Model model) {
        PhieuNhap pn = phieuNhapService.findById(maPN);
        model.addAttribute("phieuNhap", pn);
        model.addAttribute("sachList", sachService.findAll());
        model.addAttribute("chiTietPN", new ChiTietPN());
        return "admin/chitietpn/form";
    }

    // Lưu (thêm mới hoặc cập nhật)
    @PostMapping("/save")
    public String save(@RequestParam("maPN") Integer maPN,
                       @RequestParam("maSach") Integer maSach,
                       @RequestParam("soLuong") Integer soLuong,
                       @RequestParam("giaNhap") BigDecimal giaNhap) {

        PhieuNhap phieuNhap = phieuNhapService.findById(maPN);
        Sach sach = sachService.findById(maSach).orElse(null);

        ChiTietPNKey key = new ChiTietPNKey(maPN, maSach);
        ChiTietPN chiTietPN = chiTietPNService.findById(key);

        if (chiTietPN == null) {
            chiTietPN = new ChiTietPN();
            chiTietPN.setId(key);
            chiTietPN.setPhieuNhap(phieuNhap);
            chiTietPN.setSach(sach);
        }

        chiTietPN.setSoLuong(soLuong);
        chiTietPN.setGiaNhap(giaNhap);

        chiTietPNService.save(chiTietPN);
        return "redirect:/admin/chitietpn/phieu/" + maPN;
    }

    // Xóa một dòng chi tiết
    @GetMapping("/delete/{maPN}/{maSach}")
    public String delete(@PathVariable("maPN") Integer maPN,
                         @PathVariable("maSach") Integer maSach) {
        ChiTietPNKey key = new ChiTietPNKey(maPN, maSach);
        chiTietPNService.deleteById(key);
        return "redirect:/admin/chitietpn/phieu/" + maPN;
    }
}
