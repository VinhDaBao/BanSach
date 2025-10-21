////package com.bookstore.web.controller;
////
////
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.ModelAttribute;
////import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////
////import com.bookstore.web.entity.Sach;
////import com.bookstore.web.service.NhaCungCapService;
////import com.bookstore.web.service.SachService;
////import com.bookstore.web.service.TheLoaiService;
////
////
////
////@Controller
////@RequestMapping("/admin/sach")
////public class AdminSachController {
////
////    private final SachService sachService;
////    private final NhaCungCapService nccService;
////    private final TheLoaiService theLoaiService; 
////
////    public AdminSachController(SachService sachService,
////                               NhaCungCapService nccService,
////                               TheLoaiService theLoaiService) {
////        this.sachService = sachService;
////        this.nccService = nccService;
////        this.theLoaiService = theLoaiService;
////    }
////
////    @GetMapping
////    public String list(Model model) {
////        model.addAttribute("list", sachService.findAll());
////        return "admin/sach/list";
////    }
////
////    @GetMapping("/create")
////    public String createForm(Model model) {
////        model.addAttribute("sach", new Sach());
////        model.addAttribute("listNCC", nccService.findAll());
////        model.addAttribute("listTL", theLoaiService.findAll());
////        return "admin/sach/form";
////    }
////
////    @PostMapping("/save")
////    public String save(@ModelAttribute Sach sach) {
////        sachService.save(sach);
////        return "redirect:/admin/sach";
////    }
////
////    @GetMapping("/edit/{id}")
////    public String editForm(@PathVariable Integer id, Model model) {
////        model.addAttribute("sach", sachService.findById(id));
////        model.addAttribute("listNCC", nccService.findAll());
////        model.addAttribute("listTL", theLoaiService.findAll());
////        return "admin/sach/form";
////    }
////
////    @GetMapping("/delete/{id}")
////    public String delete(@PathVariable Integer id) {
////        sachService.deleteById(id);
////        return "redirect:/admin/sach";
////    }
////
////    @GetMapping("/detail/{id}")
////    public String detail(@PathVariable Integer id, Model model) {
////        model.addAttribute("sach", sachService.findById(id));
////        return "admin/sach/detail";
////    }
////}
//
//package com.bookstore.web.controller;
//
//import com.bookstore.web.entity.NhaXB;
//import com.bookstore.web.entity.Sach;
//import com.bookstore.web.service.*;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//@RequestMapping("/admin/sach")
//public class AdminSachController {
//
//    private final SachService sachService;
//    private final NhaCungCapService nccService;
//    private final NhaXBService nxbService;
//    private final HinhThucService htService;
//    private final TheLoaiService theLoaiService;
//
//    public AdminSachController(
//            SachService sachService,
//            NhaCungCapService nccService,
//            NhaXBService nxbService,
//            HinhThucService htService,
//            TheLoaiService theLoaiService) {
//
//        this.sachService = sachService;
//        this.nccService = nccService;
//        this.nxbService = nxbService;
//        this.htService = htService;
//        this.theLoaiService = theLoaiService;
//    }
//
//    // 📘 Danh sách sách
//    @GetMapping
//    public String listSach(
//            Model model,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(required = false) String keyword) {
//
//        int pageSize = 8; // số sách mỗi trang
//        var pageable = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);
//
//        Page<Sach> sachPage;
//
//        // Nếu có keyword thì tìm kiếm, ngược lại hiển thị toàn bộ
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            sachPage = sachService.searchByKeyword(keyword.trim(), pageable);
//            model.addAttribute("keyword", keyword);
//        } else {
//            sachPage = sachService.findAll(pageable);
//        }
//
//        model.addAttribute("list", sachPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", sachPage.getTotalPages());
//        model.addAttribute("totalItems", sachPage.getTotalElements());
//
//        return "admin/sach/list";
//    }
//
//
//    // ➕ Form thêm mới
//    @GetMapping("/create")
//    public String createForm(Model model) {
//        // Luôn add object "sach" để tránh lỗi "Neither BindingResult..."
//        model.addAttribute("sach", new Sach());
//        addCommonData(model);
//        return "admin/sach/form";
//    }
//
//    // ✏️ Form chỉnh sửa
//    @GetMapping("/edit/{id}")
//    public String editForm(@PathVariable Integer id, Model model) {
//    	Sach sach = sachService.findById(id).orElse(null);
//        if (sach == null) {
//            // nếu id không tồn tại => mở form thêm mới thay vì lỗi 500
//            sach = new Sach();
//        }
//        model.addAttribute("sach", sach);
//        addCommonData(model);
//        return "admin/sach/form";
//    }
//
//    @PostMapping("/save")
//    public String save(@ModelAttribute("sach") Sach sach,
//                       @RequestParam(value="tenNXB", required=false) String tenNXB,
//                       @RequestParam(value="diaChiNXB", required=false) String diaChiNXB,
//                       @RequestParam(value="namThanhLapNXB", required=false) Integer namThanhLapNXB,
//                       @RequestParam("anhFile") MultipartFile anhFile,
//                       Model model) {
//        try {
//            // --- Xử lý Nhà Xuất Bản ---
//            if (tenNXB != null && !tenNXB.isBlank()) {
//                NhaXB nhaXB = nxbService.findByTenNXB(tenNXB.trim());
//                if (nhaXB == null) {
//                    // Tạo mới với đầy đủ thông tin
//                    nhaXB = new NhaXB();
//                    nhaXB.setTenNXB(tenNXB.trim());
//                    nhaXB.setDiaChi(diaChiNXB != null ? diaChiNXB.trim() : "Chưa cập nhật");
//                    nhaXB.setNamThanhLap(namThanhLapNXB != null ? namThanhLapNXB : 2000);
//                    nhaXB = nxbService.save(nhaXB);
//                }
//                sach.setNhaXB(nhaXB);
//            } else if (sach.getNhaXB() != null) {
//                // giữ nguyên NXB đã chọn
//            }
//
//            // --- Xử lý ảnh ---
//            String uploadDir = "uploads/";
//            File uploadFolder = new File(uploadDir);
//            if (!uploadFolder.exists()) uploadFolder.mkdirs();
//
//            if (!anhFile.isEmpty()) {
//                String fileName = System.currentTimeMillis() + "_" + anhFile.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir + fileName);
//                Files.write(filePath, anhFile.getBytes());
//                sach.setAnh("/uploads/" + fileName);
//            } else if (sach.getId() != null) {
//                Sach existing = sachService.findById(sach.getId()).orElse(null);
//                if (existing != null) sach.setAnh(existing.getAnh());
//            }
//
//            sachService.save(sach);
//            return "redirect:/admin/sach";
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Lưu thất bại: " + e.getMessage());
//            model.addAttribute("sach", sach);
//            addCommonData(model);
//            return "admin/sach/form";
//        }
//    }
//
//
//
//    // ❌ Xóa sách
//    @GetMapping("/delete/{id}")
//    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttrs) {
//    	 Sach sach = sachService.findById(id).orElse(null);
//        if (sach != null) {
//            sach.setTrangThai(false); // ẩn sách thay vì xóa
//            sachService.save(sach);
//            redirectAttrs.addFlashAttribute("success", "Sách đã được ẩn khỏi hệ thống.");
//        } else {
//            redirectAttrs.addFlashAttribute("error", "Không tìm thấy sách để xóa!");
//        }
//        return "redirect:/admin/sach";
//    }
//
// // 🔍 Xem chi tiết
//    @GetMapping("/detail/{id}")
//    public String detail(@PathVariable Integer id, Model model) {
//    	Sach sach = sachService.findById(id).orElse(null);
//
//        // Nếu sách không tồn tại trong DB => trả về template chi tiết với thông báo
//        if (sach == null) {
//            model.addAttribute("errorMessage", "Không tìm thấy sách có mã: " + id);
//            model.addAttribute("sach", new Sach()); // tránh null pointer trong Thymeleaf
//            return "admin/sach/detail";
//        }
//
//        model.addAttribute("sach", sach);
//        return "admin/sach/detail";
//    }
//
//
//    // ✅ Hàm dùng chung để load danh sách NXB, NCC, Hình thức, Thể loại
//    private void addCommonData(Model model) {
//        model.addAttribute("dsNCC", nccService.findAll());
//        model.addAttribute("dsNXB", nxbService.findAll());
//        model.addAttribute("dsHT", htService.findAll());
//        model.addAttribute("dsTheLoai", theLoaiService.findAll());
//    }
//}





package com.bookstore.web.controller;

import com.bookstore.web.entity.HinhThuc;
import com.bookstore.web.entity.NhaXB;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.entity.TheLoai;
import com.bookstore.web.service.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sach")
public class AdminSachController {

    private final SachService sachService;
    private final NhaCungCapService nccService;
    private final NhaXBService nxbService;
    private final HinhThucService htService;
    private final TheLoaiService theLoaiService;

    public AdminSachController(
            SachService sachService,
            NhaCungCapService nccService,
            NhaXBService nxbService,
            HinhThucService htService,
            TheLoaiService theLoaiService) {

        this.sachService = sachService;
        this.nccService = nccService;
        this.nxbService = nxbService;
        this.htService = htService;
        this.theLoaiService = theLoaiService;
    }

    // 📘 Danh sách sách
    @GetMapping
    public String listSach(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword) {

        int pageSize = 8; // số sách mỗi trang
        var pageable = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);

        Page<Sach> sachPage;

        // Nếu có keyword thì tìm kiếm, ngược lại hiển thị toàn bộ
        if (keyword != null && !keyword.trim().isEmpty()) {
            sachPage = sachService.searchByKeyword(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword);
        } else {
            sachPage = sachService.findAll(pageable);
        }

        model.addAttribute("list", sachPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sachPage.getTotalPages());
        model.addAttribute("totalItems", sachPage.getTotalElements());

        return "admin/sach/list";
    }


    // ➕ Form thêm mới
    @GetMapping("/create")
    public String createForm(Model model) {
        // Luôn add object "sach" để tránh lỗi "Neither BindingResult..."
        model.addAttribute("sach", new Sach());
        addCommonData(model);
        return "admin/sach/form";
    }

    // ✏️ Form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
    	Sach sach = sachService.findById(id).orElse(null);
        if (sach == null) {
            // nếu id không tồn tại => mở form thêm mới thay vì lỗi 500
            sach = new Sach();
        }
        model.addAttribute("sach", sach);
        addCommonData(model);
        return "admin/sach/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("sach") Sach sach,
    				   @RequestParam(required = false) String hinhThucId,
                       @RequestParam(required = false) String tenHTMoi,
                       @RequestParam(required = false) List<Integer> theLoaiIds,
                       @RequestParam(required = false) String tenTheLoaiMoi,
                       @RequestParam(required = false) String nxbId,
                       @RequestParam(value="tenNXB", required=false) String tenNXB,
                       @RequestParam(value="diaChiNXB", required=false) String diaChiNXB,
                       @RequestParam(value="namThanhLapNXB", required=false) Integer namThanhLapNXB,
                       @RequestParam("anhFile") MultipartFile anhFile,
                       Model model) {
        try {
        	 if (sach.getId() != null) {
                 Sach existing = sachService.findById(sach.getId()).orElse(null);
                 if (existing != null) {
                     sach.setSoLuongTon(existing.getSoLuongTon());
                 }
             }
        	// --- Xử lý Nhà Xuất Bản ---
        	 if (nxbId != null && nxbId.equals("__other__")) {
        	     // Trường hợp người dùng chọn "Nhập mới..."
        	     if (tenNXB != null && !tenNXB.isBlank()) {
        	         NhaXB nhaXB = new NhaXB();
        	         nhaXB.setTenNXB(tenNXB.trim());
        	         nhaXB.setDiaChi(diaChiNXB != null && !diaChiNXB.isBlank() ? diaChiNXB.trim() : "Chưa cập nhật");
        	         nhaXB.setNamThanhLap(namThanhLapNXB != null ? namThanhLapNXB : 2000);
        	         nhaXB = nxbService.save(nhaXB);
        	         sach.setNhaXB(nhaXB);
        	     }
        	 } else if (nxbId != null && !nxbId.isBlank()) {
        	     // Chọn từ danh sách có sẵn
        	     NhaXB nhaXB = nxbService.findById(Integer.parseInt(nxbId));
        	     sach.setNhaXB(nhaXB);
        	 } else {
        	     // Không chọn gì
        	     sach.setNhaXB(null);
        	 }
            
            /** ================== XỬ LÝ HÌNH THỨC ================== */
            if ("__other__".equals(hinhThucId) && tenHTMoi != null && !tenHTMoi.isBlank()) {
                HinhThuc htMoi = new HinhThuc();
                htMoi.setTenHT(tenHTMoi.trim());
                htMoi = htService.save(htMoi);
                sach.setHinhThuc(htMoi);
            } else if (hinhThucId != null && !hinhThucId.isBlank()) {
                HinhThuc ht = htService.findById(Integer.parseInt(hinhThucId));
                if (ht != null) sach.setHinhThuc(ht);
            } else {
                sach.setHinhThuc(null);
            }

            /** ================== XỬ LÝ THỂ LOẠI ================== */
            List<TheLoai> danhSachTL = new ArrayList<>();

            // Thêm các thể loại chọn sẵn
            if (theLoaiIds != null && !theLoaiIds.isEmpty()) {
                for (Integer id : theLoaiIds) {
                    theLoaiService.findById(id).ifPresent(danhSachTL::add);
                }
            }

            // Thêm thể loại mới (nếu có nhập)
            if (tenTheLoaiMoi != null && !tenTheLoaiMoi.isBlank()) {
                TheLoai tlMoi = new TheLoai();
                tlMoi.setTenTL(tenTheLoaiMoi.trim());
                tlMoi = theLoaiService.save(tlMoi);
                danhSachTL.add(tlMoi);
            }

            sach.setDanhSachTheLoai(new HashSet<>(danhSachTL));

            // --- Xử lý ảnh ---
            String uploadDir = "uploads/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();

            if (!anhFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + anhFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, anhFile.getBytes());
                sach.setAnh("/uploads/" + fileName);
            } else if (sach.getId() != null) {
                Sach existing = sachService.findById(sach.getId()).orElse(null);
                if (existing != null) sach.setAnh(existing.getAnh());
            }

            sachService.save(sach);
            return "redirect:/admin/sach";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Lưu thất bại: " + e.getMessage());
            model.addAttribute("sach", sach);
            addCommonData(model);
            return "admin/sach/form";
        }
    }
    
    @GetMapping("/save")
    public String redirectSaveToCreate() {
        return "redirect:/admin/sach/create";
    }



    // ❌ Xóa sách
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttrs) {
    	 Sach sach = sachService.findById(id).orElse(null);
        if (sach != null) {
            sach.setTrangThai(false); // ẩn sách thay vì xóa
            sachService.save(sach);
            redirectAttrs.addFlashAttribute("success", "Sách đã được ẩn khỏi hệ thống.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Không tìm thấy sách để xóa!");
        }
        return "redirect:/admin/sach";
    }

 // 🔍 Xem chi tiết
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
    	Sach sach = sachService.findById(id).orElse(null);

        // Nếu sách không tồn tại trong DB => trả về template chi tiết với thông báo
        if (sach == null) {
            model.addAttribute("errorMessage", "Không tìm thấy sách có mã: " + id);
            model.addAttribute("sach", new Sach()); // tránh null pointer trong Thymeleaf
            return "admin/sach/detail";
        }

        model.addAttribute("sach", sach);
        return "admin/sach/detail";
    }


    // ✅ Hàm dùng chung để load danh sách NXB, NCC, Hình thức, Thể loại
    private void addCommonData(Model model) {
        model.addAttribute("dsNCC", nccService.findAll());
        model.addAttribute("dsNXB", nxbService.findAll());
        model.addAttribute("dsHT", htService.findAll());
        model.addAttribute("dsTheLoai", theLoaiService.findAll());
    }
    
    /** 🔄 Bật/Tắt trạng thái kinh doanh thủ công */
    @GetMapping("/toggle/{id}")
    public String toggleTrangThai(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        Sach sach = sachService.findById(id).orElse(null);
        if (sach != null) {
            sach.setTrangThai(!sach.getTrangThai());
            sachService.save(sach);
            redirectAttrs.addFlashAttribute("message", "✅ Đã thay đổi trạng thái kinh doanh!");
        } else {
            redirectAttrs.addFlashAttribute("error", "❌ Không tìm thấy sách!");
        }
        return "redirect:/admin/sach";
    }
}