package com.bookstore.web.controller;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.entity.ChiTietDonHang;
import com.bookstore.web.entity.DiaChi;
import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.GioHang;
import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.repository.ChiTietDonHangRepository;
import com.bookstore.web.repository.DonHangRepository;
import com.bookstore.web.repository.GioHangRepository;
import com.bookstore.web.repository.GioHangSachRepository;
import com.bookstore.web.repository.SachRepository;
import com.bookstore.web.service.DiaChiService;
import com.bookstore.web.service.SachService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class CheckoutController {

    @Autowired
    private DonHangRepository donHangRepository;
    
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    
    @Autowired
    private GioHangRepository gioHangRepository;
    
    @Autowired
    private GioHangSachRepository gioHangSachRepository;
    
    @Autowired
    private SachRepository sachRepository;
    
    @Autowired
    private SachService sachService;
    
    @Autowired 
    private DiaChiService diaChiService;

    @PostMapping("/checkout")
    @Transactional
    public String checkout(
            @RequestParam(value = "selectedItems", required = false) List<Integer> selectedItems,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            @RequestParam(value = "addressId", required = false) Integer addressId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        System.out.println("=== CHECKOUT STARTED ===");
        System.out.println("Selected items: " + selectedItems);
        System.out.println("Quantities: " + quantities);
        
        // Kiểm tra đăng nhập
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thanh toán");
            return "redirect:/login";
        }
        
        // Kiểm tra có chọn sản phẩm không
        if (selectedItems == null || selectedItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán");
            return "redirect:/cart";
        }
        
        // Kiểm tra quantities
        if (quantities == null || quantities.size() != selectedItems.size()) {
            redirectAttributes.addFlashAttribute("error", "Dữ liệu số lượng không hợp lệ");
            return "redirect:/cart";
        }
        
        try {
            // Lấy thông tin user
            NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng");
                return "redirect:/login";
            }
            DiaChi diaChi = null;
            if (addressId != null) {
                diaChi = diaChiService.findById(addressId);
            }
            
            if (diaChi == null) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn địa chỉ giao hàng");
                return "redirect:/cart";
            }
            
            // ⭐ TẠO ĐƠN HÀNG MỚI
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(loggedUser);
            donHang.setNgayDat(LocalDateTime.now());
            donHang.setTrangThai("Chờ xác nhận"); // ← TRẠNG THÁI MẶC ĐỊNH
            
            // Lấy địa chỉ và SĐT
            donHang.setDiaChi(loggedUser.getDiaChi() != null && !loggedUser.getDiaChi().isEmpty() 
                ? loggedUser.getDiaChi() : "Chưa cập nhật địa chỉ");
            donHang.setSdt(loggedUser.getSdt() != null && !loggedUser.getSdt().isEmpty() 
                ? loggedUser.getSdt() : "Chưa cập nhật SĐT");
            
            BigDecimal tongTien = BigDecimal.ZERO;
            List<ChiTietDonHang> chiTietList = new ArrayList<>();
            
            // Lấy giỏ hàng
            GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
            
            // Duyệt qua các sản phẩm được chọn
            for (int i = 0; i < selectedItems.size(); i++) {
                Integer maSach = selectedItems.get(i);
                Integer soLuong = quantities.get(i);
                
                System.out.println("Processing item: maSach=" + maSach + ", soLuong=" + soLuong);
                
                Sach sach = sachRepository.findById(maSach).orElse(null);
                if (sach == null) {
                    System.out.println("Sach not found: " + maSach);
                    continue;
                }
                
                // Kiểm tra tồn kho
                if (sach.getSoLuongTon() < soLuong) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Sản phẩm '" + sach.getTenSP() + "' không đủ số lượng trong kho");
                    return "redirect:/cart";
                }
                
                // ⭐ ÁP DỤNG GIÁ GIẢM NẾU CÓ
                BigDecimal giaBan = sach.getGiaBan();
                KhuyenMai km = sachService.getBestKhuyenMai(sach.getId());
                if (km != null && isActivePromotion(km)) {
                    double gt = km.getGiaTri();
                    if (gt < 1.0) {
                        giaBan = giaBan.multiply(BigDecimal.valueOf(1 - gt));
                    } else {
                        giaBan = giaBan.subtract(BigDecimal.valueOf(gt));
                    }
                    System.out.println("Applied discount: " + sach.getGiaBan() + " → " + giaBan);
                }
                
                // Tạo chi tiết đơn hàng
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setSach(sach);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setGia(giaBan);
                
                chiTietList.add(chiTiet);
                
                // Tính tổng tiền
                BigDecimal itemTotal = giaBan.multiply(BigDecimal.valueOf(soLuong));
                tongTien = tongTien.add(itemTotal);
                
                // Giảm số lượng tồn kho
                sach.setSoLuongTon(sach.getSoLuongTon() - soLuong);
                sachRepository.save(sach);
                System.out.println("Updated stock for " + sach.getTenSP() + ": " + sach.getSoLuongTon());
                
                // Xóa khỏi giỏ hàng (nếu có)
            }
            
            // Thêm phí vận chuyển
            BigDecimal phiVanChuyen = BigDecimal.ZERO;
            tongTien = tongTien.add(phiVanChuyen);
            
            donHang.setTongTien(tongTien);
            donHang.setChiTietDonHangs(chiTietList);
            
            // Lưu đơn hàng
            DonHang savedOrder = donHangRepository.save(donHang);
            System.out.println("✓ Order saved: ID=" + savedOrder.getId());
            
            // Lưu chi tiết đơn hàng
            for (ChiTietDonHang chiTiet : chiTietList) {
                chiTiet.setDonHang(savedOrder); // Set lại reference
                chiTietDonHangRepository.save(chiTiet);
            }
            
            System.out.println("✓ Order details saved");
            System.out.println("=== CHECKOUT COMPLETED ===");
            System.out.println("OrderId: " + savedOrder.getId());
            System.out.println("Status: " + savedOrder.getTrangThai());
            System.out.println("Total: " + tongTien);
            
            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: #" + savedOrder.getId());
            
            return "redirect:/products";
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("✗ Checkout error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", 
                "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }
    
    /**
     * Kiểm tra khuyến mãi còn hiệu lực không
     */
    private boolean isActivePromotion(KhuyenMai km) {
        java.time.LocalDate today = java.time.LocalDate.now();
        return (today.isAfter(km.getNgayBD()) || today.isEqual(km.getNgayBD())) 
               && (today.isBefore(km.getNgayKT()) || today.isEqual(km.getNgayKT()));
    }
}