//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.ChiTietPN;
//import com.bookstore.web.entity.NhaCungCap;
//import com.bookstore.web.entity.PhieuNhap;
//import java.util.List;
//
//public interface PhieuNhapService {
//    List<PhieuNhap> findAll();
//    PhieuNhap findById(Integer MaPN);
//    PhieuNhap save(PhieuNhap phieuNhap);
//    void deleteById(Integer MaPN);
//    
//    List<PhieuNhap> findByNhaCungCap(NhaCungCap NhaCungCap);
//    
//    PhieuNhap saveWithDetails(PhieuNhap phieuNhap, List<ChiTietPN> chiTietList);
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.NhaCungCap;
import com.bookstore.web.entity.PhieuNhap;

import java.util.List;

public interface PhieuNhapService {
    
    // ==================== CRUD CƠ BẢN ====================
    List<PhieuNhap> findAll();
    PhieuNhap findById(Integer maPN);
    PhieuNhap save(PhieuNhap phieuNhap);
    void deleteById(Integer maPN);

    // ==================== LIÊN KẾT NHÀ CUNG CẤP ====================
    List<PhieuNhap> findByNhaCungCap(NhaCungCap nhaCungCap);

    // ==================== LƯU PHIẾU NHẬP KÈM CHI TIẾT ====================
    PhieuNhap saveWithDetails(PhieuNhap phieuNhap, List<ChiTietPN> chiTietList);
}
