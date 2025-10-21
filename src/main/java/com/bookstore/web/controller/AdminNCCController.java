//package com.bookstore.web.controller;
//
//import com.bookstore.web.entity.NhaCungCap;
//import com.bookstore.web.service.NhaCungCapService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//
//@Controller
//@RequestMapping("/admin/ncc")
//public class AdminNCCController {
//
//	private final NhaCungCapService nccService;
//
//	public AdminNCCController(NhaCungCapService nccService) {
//		this.nccService = nccService;
//	}
//
//	@GetMapping
//	public String list(Model model) {
//		model.addAttribute("list", nccService.findAll());
//		return "admin/ncc/list";
//	}
//
//	@GetMapping("/create")
//	public String createForm(Model model) {
//		model.addAttribute("ncc", new NhaCungCap());
//		return "admin/ncc/form";
//	}
//
//	@PostMapping("/save")
//	public String save(@ModelAttribute NhaCungCap ncc) {
//		nccService.save(ncc);
//		return "redirect:/admin/ncc";
//	}
//
//	@GetMapping("/edit/{maNCC}")
//	public String editForm(@PathVariable("maNCC") Integer maNCC, Model model) {
//		model.addAttribute("ncc", nccService.findById(maNCC));
//		return "admin/ncc/form";
//	}
//
//	@GetMapping("/delete/{maNCC}")
//	public String delete(@PathVariable("maNCC") Integer maNCC) {
//		nccService.deleteById(maNCC);
//		return "redirect:/admin/ncc";
//	}
//}

package com.bookstore.web.controller;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.NhaCungCap;
import com.bookstore.web.entity.PhieuNhap;
import com.bookstore.web.service.NhaCungCapService;
import com.bookstore.web.service.PhieuNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/ncc")
public class AdminNCCController {

    private final NhaCungCapService nccService;
    private final PhieuNhapService phieuNhapService;

    @Autowired
    public AdminNCCController(NhaCungCapService nccService, PhieuNhapService phieuNhapService) {
        this.nccService = nccService;
        this.phieuNhapService = phieuNhapService;
    }

    // ✅ Danh sách nhà cung cấp
    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "message", required = false) String message) {
        model.addAttribute("list", nccService.findAll());
        if (message != null) model.addAttribute("message", message);
        return "admin/ncc/list";
    }

    // ✅ Form thêm mới
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("title", "➕ Thêm Nhà Cung Cấp");
        model.addAttribute("ncc", new NhaCungCap());
        return "admin/ncc/form";
    }

    // ✅ Form chỉnh sửa
    @GetMapping("/edit/{maNCC}")
    public String editForm(@PathVariable("maNCC") Integer maNCC, Model model, RedirectAttributes redirectAttrs) {
        NhaCungCap ncc = nccService.findById(maNCC);
        if (ncc == null) {
            redirectAttrs.addFlashAttribute("message", "❌ Nhà cung cấp không tồn tại!");
            return "redirect:/admin/ncc";
        }
        model.addAttribute("title", "✏️ Chỉnh Sửa Nhà Cung Cấp");
        model.addAttribute("ncc", ncc);
        return "admin/ncc/form";
    }

    // ✅ Lưu (thêm hoặc cập nhật)
    @PostMapping("/save")
    public String save(@ModelAttribute("ncc") NhaCungCap ncc, RedirectAttributes redirectAttrs) {
        try {
            nccService.save(ncc);
            redirectAttrs.addFlashAttribute("message", "✅ Lưu nhà cung cấp thành công!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("message", "❌ Lỗi khi lưu: " + e.getMessage());
        }
        return "redirect:/admin/ncc";
    }

    // ✅ Xóa NCC
    @GetMapping("/delete/{maNCC}")
    public String delete(@PathVariable("maNCC") Integer maNCC, RedirectAttributes redirectAttrs) {
        try {
            nccService.deleteById(maNCC);
            redirectAttrs.addFlashAttribute("message", "🗑️ Xóa thành công!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("message",
                    "⚠️ Không thể xóa vì nhà cung cấp đang được sử dụng trong phiếu nhập!");
        }
        return "redirect:/admin/ncc";
    }

    // ✅ Xem chi tiết nhà cung cấp + các phiếu nhập liên quan
    @GetMapping("/detail/{maNCC}")
    public String detail(@PathVariable("maNCC") Integer maNCC, Model model, RedirectAttributes redirectAttrs) {
        NhaCungCap ncc = nccService.findById(maNCC);
        if (ncc == null) {
            redirectAttrs.addFlashAttribute("message", "❌ Nhà cung cấp không tồn tại!");
            return "redirect:/admin/ncc";
        }

        // 🔹 Lấy danh sách phiếu nhập của NCC
        List<PhieuNhap> phieuNhapList = phieuNhapService.findByNhaCungCap(ncc);
        if (phieuNhapList == null) phieuNhapList = new ArrayList<>();

        // 🔹 Tính tổng tiền từng phiếu & tổng toàn bộ NCC
        BigDecimal tongGiaTri = BigDecimal.ZERO;
        for (PhieuNhap pn : phieuNhapList) {
            BigDecimal tongPN = BigDecimal.ZERO;
            List<ChiTietPN> chiTietList = pn.getChiTietPNList();

            if (chiTietList != null) {
                for (ChiTietPN ct : chiTietList) {
                    BigDecimal thanhTien = ct.getGiaNhap()
                            .multiply(BigDecimal.valueOf(ct.getSoLuong()));
                    tongPN = tongPN.add(thanhTien);
                }
            }
            pn.setTongTien(tongPN); // 💾 gán để Thymeleaf hiển thị
            tongGiaTri = tongGiaTri.add(tongPN);
        }

        model.addAttribute("ncc", ncc);
        model.addAttribute("phieuNhapList", phieuNhapList);
        model.addAttribute("tongGiaTri", tongGiaTri);

        return "admin/ncc/detail";
    }
}

