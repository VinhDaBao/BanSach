//package com.bookstore.web.controller;
//
//import com.bookstore.web.entity.*;
//import com.bookstore.web.service.*;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/admin/phieunhap")
//public class AdminPhieuNhapController {
//
//    private final PhieuNhapService phieuNhapService;
//    private final NhaCungCapService nhaCungCapService;
//    private final ChiTietPNService chiTietPNService;
//    private final SachService sachService;
//
//    public AdminPhieuNhapController(
//            PhieuNhapService phieuNhapService,
//            NhaCungCapService nhaCungCapService,
//            ChiTietPNService chiTietPNService,
//            SachService sachService
//    ) {
//        this.phieuNhapService = phieuNhapService;
//        this.nhaCungCapService = nhaCungCapService;
//        this.chiTietPNService = chiTietPNService;
//        this.sachService = sachService;
//    }
//
//    /** ---------------- DANH SÁCH ---------------- */
//    @GetMapping
//    public String list(Model model, @RequestParam(value = "message", required = false) String message) {
//        List<PhieuNhap> list = phieuNhapService.findAll();
//
//        // 🔹 Tính tổng tiền cho từng phiếu nhập
//        Map<Integer, BigDecimal> tongTienMap = new HashMap<>();
//        for (PhieuNhap pn : list) {
//            BigDecimal tong = BigDecimal.ZERO;
//            if (pn.getChiTietPNList() != null) {
//                for (ChiTietPN ct : pn.getChiTietPNList()) {
//                    if (ct.getGiaNhap() != null && ct.getSoLuong() != null)
//                        tong = tong.add(ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())));
//                }
//            }
//            tongTienMap.put(pn.getMaPN(), tong);
//        }
//
//        model.addAttribute("list", list);
//        model.addAttribute("tongTienMap", tongTienMap);
//        if (message != null) model.addAttribute("message", message);
//        return "admin/phieunhap/list";
//    }
//
//    /** ---------------- FORM TẠO ---------------- */
//    @GetMapping("/create")
//    public String createForm(Model model) {
//        model.addAttribute("phieuNhap", new PhieuNhap());
//        model.addAttribute("nccList", nhaCungCapService.findAll());
//        List<Sach> sachList = sachService.findAll(); // hoặc findAllDangKinhDoanh()
//        model.addAttribute("sachList", sachService.findAll());
//        return "admin/phieunhap/form";
//    }
//
//    /** ---------------- FORM SỬA ---------------- */
//    @GetMapping("/edit/{maPN}")
//    public String editForm(@PathVariable("maPN") Integer maPN, Model model, RedirectAttributes redirectAttrs) {
//        PhieuNhap pn = phieuNhapService.findById(maPN);
//        if (pn == null) {
//            redirectAttrs.addFlashAttribute("message", "⚠️ Phiếu nhập không tồn tại!");
//            return "redirect:/admin/phieunhap";
//        }
//        model.addAttribute("phieuNhap", pn);
//        model.addAttribute("nccList", nhaCungCapService.findAll());
//     // ✅ load lại danh sách sách để hiển thị trong select
//        List<Sach> sachList = sachService.findAll();
//        model.addAttribute("sachList", sachService.findAll());
//        return "admin/phieunhap/form";
//    }
//
//    /** ---------------- LƯU (TẠO HOẶC CẬP NHẬT) ---------------- */
//    @PostMapping("/save")
//    public String save(
//            @RequestParam(value = "maPN", required = false) Integer maPN,
//            @RequestParam(value = "maNCC") String maNCCStr, // 👈 đổi từ Integer sang String
//            @RequestParam(value = "tenNccMoi", required = false) String tenNccMoi, // 👈 nhận tên NCC mới
//            @RequestParam(value = "ngayNhap") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayNhap,
//            @RequestParam(value = "sachIds[]", required = false) List<String> sachIds,
//            @RequestParam(value = "tenSachMoi[]", required = false) List<String> tenSachMoiList,
//            @RequestParam(value = "soLuongs[]", required = false) List<Integer> soLuongs,
//            @RequestParam(value = "giaNhaps[]", required = false) List<BigDecimal> giaNhaps,
//            RedirectAttributes redirectAttrs
//    ) {
//        try {
//            NhaCungCap ncc = null;
//
//            // 🔹 Nếu người dùng chọn “+ Nhập mới...”
//            if ("__other__".equals(maNCCStr)) {
//                if (tenNccMoi == null || tenNccMoi.isBlank()) {
//                    redirectAttrs.addFlashAttribute("message", "⚠️ Vui lòng nhập tên Nhà cung cấp mới!");
//                    return "redirect:/admin/phieunhap/create";
//                }
//
//                // ✅ Kiểm tra trùng tên NCC
//                NhaCungCap existing = nhaCungCapService.findByTenNCC(tenNccMoi.trim());
//                if (existing != null) {
//                    ncc = existing;
//                } else {
//                    ncc = new NhaCungCap();
//                    ncc.setTenNCC(tenNccMoi.trim());
//                    ncc = nhaCungCapService.save(ncc);
//                }
//            } 
//            // 🔹 Nếu người dùng chọn NCC có sẵn
//            else {
//                Integer maNCC = Integer.parseInt(maNCCStr);
//                ncc = nhaCungCapService.findById(maNCC);
//            }
//
//            if (ncc == null) {
//                redirectAttrs.addFlashAttribute("message", "❌ Không tìm thấy Nhà cung cấp!");
//                return "redirect:/admin/phieunhap";
//            }
//
//            // 🔹 2. Phiếu nhập
//            PhieuNhap phieuNhap = (maPN != null)
//                    ? phieuNhapService.findById(maPN)
//                    : new PhieuNhap();
//            phieuNhap.setNhaCungCap(ncc);
//            phieuNhap.setNgayNhap(ngayNhap != null ? ngayNhap.atStartOfDay() : LocalDateTime.now());
//
//            // 🔹 3. Chi tiết phiếu nhập
//            List<ChiTietPN> chiTietList = new ArrayList<>();
//
//            if (sachIds != null) {
//                for (int i = 0; i < sachIds.size(); i++) {
//                    String sachIdStr = sachIds.get(i);
//                    Sach sach = null;
//
//                    // 3.1 Nếu người dùng chọn “sách có sẵn”
//                    if (sachIdStr != null && !sachIdStr.isBlank() && !"__other__".equals(sachIdStr)) {
//                        try {
//                            Integer sachId = Integer.parseInt(sachIdStr);
//                            sach = sachService.findById(sachId).orElse(null);
//                        } catch (Exception ignored) {}
//                    }
//
//                    // 3.2 Nếu người dùng nhập “tên sách mới”
//                    if ("__other__".equals(sachIdStr)) {
//                        String tenMoi = (tenSachMoiList != null && tenSachMoiList.size() > i)
//                                ? tenSachMoiList.get(i) : null;
//                        if (tenMoi != null && !tenMoi.isBlank()) {
//                            Sach exist = sachService.findByTenSP(tenMoi.trim());
//                            if (exist != null) {
//                                sach = exist;
//                            } else {
//                                Sach sNew = new Sach();
//                                sNew.setTenSP(tenMoi.trim());
//                                // ⚙️ Gán giá trị mặc định tránh lỗi SQL
//                                sNew.setAnh("default.png");
//                                sNew.setGiaBan(BigDecimal.ZERO);
//                                sNew.setSoLuongTon(0);
//                                sNew.setSoTrang(0);
//                                sNew.setTacGia("Chưa cập nhật");
//                                sNew.setMoTa("");
//                                sNew.setNgayCoHang(LocalDateTime.now());
//                                sNew.setNhaCungCap(ncc);
//                                sNew.setHinhThuc(null);
//                                sNew.setNhaXB(null);
//                                sNew.setNamXB(null);
//
//                                sach = sachService.save(sNew);
//                            }
//                        }
//                    }
//
//                    if (sach == null) continue;
//
//                    ChiTietPN ct = new ChiTietPN();
//                    ct.setSach(sach);
//                    ct.setSoLuong((soLuongs != null && soLuongs.size() > i) ? soLuongs.get(i) : 0);
//                    ct.setGiaNhap((giaNhaps != null && giaNhaps.size() > i) ? giaNhaps.get(i) : BigDecimal.ZERO);
//                    chiTietList.add(ct);
//                }
//            }
//
//            // 🔹 4. Lưu phiếu nhập
//            phieuNhapService.saveWithDetails(phieuNhap, chiTietList);
//
//            redirectAttrs.addFlashAttribute("message", "✅ Lưu phiếu nhập thành công!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttrs.addFlashAttribute("message", "❌ Lỗi khi lưu phiếu nhập: " + e.getMessage());
//        }
//
//        return "redirect:/admin/phieunhap";
//    }
//
//    /** ---------------- XÓA ---------------- */
//    @GetMapping("/delete/{maPN}")
//    public String delete(@PathVariable("maPN") Integer maPN, RedirectAttributes redirectAttrs) {
//        try {
//            phieuNhapService.deleteById(maPN);
//            redirectAttrs.addFlashAttribute("message", "🗑️ Đã xóa phiếu nhập thành công!");
//        } catch (Exception e) {
//            redirectAttrs.addFlashAttribute("message", "⚠️ Không thể xóa phiếu nhập (có chi tiết liên quan)!");
//        }
//        return "redirect:/admin/phieunhap";
//    }
//
//    /** ---------------- CHI TIẾT ---------------- */
//    @GetMapping("/detail/{maPN}")
//    public String detail(@PathVariable("maPN") Integer maPN, Model model, RedirectAttributes redirectAttrs) {
//        PhieuNhap pn = phieuNhapService.findById(maPN);
//        if (pn == null) {
//            redirectAttrs.addFlashAttribute("message", "⚠️ Phiếu nhập không tồn tại!");
//            return "redirect:/admin/phieunhap";
//        }
//
//        // ✅ Chủ động load để tránh LazyInitializationException
//        List<ChiTietPN> chiTietList = pn.getChiTietPNList() != null ? pn.getChiTietPNList() : new ArrayList<>();
//        for (ChiTietPN ct : chiTietList) {
//            if (ct.getSach() != null) ct.getSach().getTenSP(); // ép Hibernate load dữ liệu
//        }
//
//        model.addAttribute("phieuNhap", pn);
//        model.addAttribute("chiTietList", chiTietList);
//        return "admin/phieunhap/detail";
//    }
//}





package com.bookstore.web.controller;

import com.bookstore.web.entity.*;
import com.bookstore.web.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/phieunhap")
public class AdminPhieuNhapController {

    private final PhieuNhapService phieuNhapService;
    private final NhaCungCapService nhaCungCapService;
    private final ChiTietPNService chiTietPNService;
    private final SachService sachService;

    public AdminPhieuNhapController(
            PhieuNhapService phieuNhapService,
            NhaCungCapService nhaCungCapService,
            ChiTietPNService chiTietPNService,
            SachService sachService
    ) {
        this.phieuNhapService = phieuNhapService;
        this.nhaCungCapService = nhaCungCapService;
        this.chiTietPNService = chiTietPNService;
        this.sachService = sachService;
    }

    /** ---------------- DANH SÁCH ---------------- */
    @GetMapping
    public String list(Model model, @RequestParam(value = "message", required = false) String message) {
        List<PhieuNhap> list = phieuNhapService.findAll();

        // 🔹 Tính tổng tiền cho từng phiếu nhập
        Map<Integer, BigDecimal> tongTienMap = new HashMap<>();
        for (PhieuNhap pn : list) {
            BigDecimal tong = BigDecimal.ZERO;
            if (pn.getChiTietPNList() != null) {
                for (ChiTietPN ct : pn.getChiTietPNList()) {
                    if (ct.getGiaNhap() != null && ct.getSoLuong() != null)
                        tong = tong.add(ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())));
                }
            }
            tongTienMap.put(pn.getMaPN(), tong);
        }

        model.addAttribute("list", list);
        model.addAttribute("tongTienMap", tongTienMap);
        if (message != null) model.addAttribute("message", message);
        return "admin/phieunhap/list";
    }

    /** ---------------- FORM TẠO ---------------- */
    @GetMapping("/create")
    public String createForm(Model model) {
        // Tạo phiếu nhập mới
        PhieuNhap phieuNhap = new PhieuNhap();
        
        // Gắn vào model
        model.addAttribute("phieuNhap", phieuNhap);

        // Load danh sách nhà cung cấp
        model.addAttribute("nccList", nhaCungCapService.findAll());

        // Load danh sách sách để dropdown chọn
        List<Sach> sachList = sachService.findAll(); // hoặc findAllDangKinhDoanh()
        model.addAttribute("sachList", sachList);

        // Nếu muốn tạo 1 dòng chi tiết mặc định để form không trống
        phieuNhap.setChiTietPNList(new ArrayList<>()); // trống ban đầu

        return "admin/phieunhap/form";
    }

    /** ---------------- FORM CHỈNH SỬA PHIẾU NHẬP ---------------- */
    @GetMapping("/edit/{maPN}")
    public String editForm(@PathVariable("maPN") Integer maPN, Model model, RedirectAttributes redirectAttrs) {
        PhieuNhap pn = phieuNhapService.findById(maPN);
        if (pn == null) {
            redirectAttrs.addFlashAttribute("message", "⚠️ Phiếu nhập không tồn tại!");
            return "redirect:/admin/phieunhap";
        }

        // Load chi tiết để tránh LazyInitializationException
        List<ChiTietPN> chiTietList = pn.getChiTietPNList() != null ? pn.getChiTietPNList() : new ArrayList<>();
        for (ChiTietPN ct : chiTietList) {
            if (ct.getSach() != null) ct.getSach().getTenSP();
        }

        model.addAttribute("phieuNhap", pn);
        model.addAttribute("chiTietList", chiTietList);
        model.addAttribute("nccList", nhaCungCapService.findAll());
        model.addAttribute("sachList", sachService.findAll());
        return "admin/phieunhap/form";
    }

    /** ---------------- LƯU (TẠO HOẶC CẬP NHẬT) ---------------- */
    @PostMapping("/save")
    public String save(
            @RequestParam(value = "maPN", required = false) Integer maPN,
            @RequestParam(value = "maNCC") String maNCCStr, // 👈 đổi từ Integer sang String
            @RequestParam(value = "tenNccMoi", required = false) String tenNccMoi, // 👈 nhận tên NCC mới
            @RequestParam(value = "ngayNhap") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayNhap,
            @RequestParam(value = "sachIds[]", required = false) List<String> sachIds,
            @RequestParam(value = "tenSachMoi[]", required = false) List<String> tenSachMoiList,
            @RequestParam(value = "soLuongs[]", required = false) List<Integer> soLuongs,
            @RequestParam(value = "giaNhaps[]", required = false) List<BigDecimal> giaNhaps,
            RedirectAttributes redirectAttrs
    ) {
        try {
            NhaCungCap ncc = null;

            // 🔹 Nếu người dùng chọn “+ Nhập mới...”
            if ("__other__".equals(maNCCStr)) {
                if (tenNccMoi == null || tenNccMoi.isBlank()) {
                    redirectAttrs.addFlashAttribute("message", "⚠️ Vui lòng nhập tên Nhà cung cấp mới!");
                    return "redirect:/admin/phieunhap/create";
                }

                // ✅ Kiểm tra trùng tên NCC
                NhaCungCap existing = nhaCungCapService.findByTenNCC(tenNccMoi.trim());
                if (existing != null) {
                    ncc = existing;
                } else {
                    ncc = new NhaCungCap();
                    ncc.setTenNCC(tenNccMoi.trim());
                    ncc = nhaCungCapService.save(ncc);
                }
            } 
            // 🔹 Nếu người dùng chọn NCC có sẵn
            else {
                Integer maNCC = Integer.parseInt(maNCCStr);
                ncc = nhaCungCapService.findById(maNCC);
            }

            if (ncc == null) {
                redirectAttrs.addFlashAttribute("message", "❌ Không tìm thấy Nhà cung cấp!");
                return "redirect:/admin/phieunhap";
            }

            // 🔹 2. Phiếu nhập
            PhieuNhap phieuNhap = (maPN != null)
                    ? phieuNhapService.findById(maPN)
                    : new PhieuNhap();
            phieuNhap.setNhaCungCap(ncc);
            phieuNhap.setNgayNhap(ngayNhap != null ? ngayNhap.atStartOfDay() : LocalDateTime.now());

            // 🔹 3. Chi tiết phiếu nhập
            List<ChiTietPN> chiTietList = new ArrayList<>();

            if (sachIds != null) {
                for (int i = 0; i < sachIds.size(); i++) {
                    String sachIdStr = sachIds.get(i);
                    Sach sach = null;

                    // 3.1 Nếu người dùng chọn “sách có sẵn”
                    if (sachIdStr != null && !sachIdStr.isBlank() && !"__other__".equals(sachIdStr)) {
                        try {
                            Integer sachId = Integer.parseInt(sachIdStr);
                            sach = sachService.findById(sachId).orElse(null);
                        } catch (Exception ignored) {}
                    }

                    // 3.2 Nếu người dùng nhập “tên sách mới”
                    if ("__other__".equals(sachIdStr)) {
                        String tenMoi = (tenSachMoiList != null && tenSachMoiList.size() > i)
                                ? tenSachMoiList.get(i) : null;
                        if (tenMoi != null && !tenMoi.isBlank()) {
                            Sach exist = sachService.findByTenSP(tenMoi.trim());
                            if (exist != null) {
                                sach = exist;
                            } else {
                                Sach sNew = new Sach();
                                sNew.setTenSP(tenMoi.trim());
                                // ⚙️ Gán giá trị mặc định tránh lỗi SQL
                                sNew.setAnh("default.png");
                                sNew.setGiaBan(BigDecimal.ZERO);
                                sNew.setSoLuongTon(0);
                                sNew.setSoTrang(0);
                                sNew.setTacGia("Chưa cập nhật");
                                sNew.setMoTa("");
                                sNew.setNgayCoHang(LocalDateTime.now());
                                sNew.setHinhThuc(null);
                                sNew.setNhaXB(null);
                                sNew.setNamXB(null);

                                sach = sachService.save(sNew);
                            }
                        }
                    }

                    if (sach == null) continue;

                    ChiTietPN ct = new ChiTietPN();
                    ct.setSach(sach);
                    ct.setSoLuong((soLuongs != null && soLuongs.size() > i) ? soLuongs.get(i) : 0);
                    ct.setGiaNhap((giaNhaps != null && giaNhaps.size() > i) ? giaNhaps.get(i) : BigDecimal.ZERO);
                    chiTietList.add(ct);
                }
            }

            // 🔹 4. Lưu phiếu nhập
            phieuNhapService.saveWithDetails(phieuNhap, chiTietList);

            redirectAttrs.addFlashAttribute("message", "✅ Lưu phiếu nhập thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("message", "❌ Lỗi khi lưu phiếu nhập: " + e.getMessage());
        }

        return "redirect:/admin/phieunhap";
    }

    /** ---------------- XÓA ---------------- */
    @GetMapping("/delete/{maPN}")
    public String delete(@PathVariable("maPN") Integer maPN, RedirectAttributes redirectAttrs) {
        try {
            phieuNhapService.deleteById(maPN);
            redirectAttrs.addFlashAttribute("message", "🗑️ Đã xóa phiếu nhập thành công!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("message", "⚠️ Không thể xóa phiếu nhập (có chi tiết liên quan)!");
        }
        return "redirect:/admin/phieunhap";
    }

    /** ---------------- CHI TIẾT ---------------- */
    @GetMapping("/detail/{maPN}")
    public String detail(@PathVariable("maPN") Integer maPN, Model model, RedirectAttributes redirectAttrs) {
        PhieuNhap pn = phieuNhapService.findById(maPN);
        if (pn == null) {
            redirectAttrs.addFlashAttribute("message", "⚠️ Phiếu nhập không tồn tại!");
            return "redirect:/admin/phieunhap";
        }

        // ✅ Chủ động load để tránh LazyInitializationException
        List<ChiTietPN> chiTietList = pn.getChiTietPNList() != null ? pn.getChiTietPNList() : new ArrayList<>();
        for (ChiTietPN ct : chiTietList) {
            if (ct.getSach() != null) ct.getSach().getTenSP(); // ép Hibernate load dữ liệu
        }

        model.addAttribute("phieuNhap", pn);
        model.addAttribute("chiTietList", chiTietList);
        return "admin/phieunhap/detail";
    }
}
