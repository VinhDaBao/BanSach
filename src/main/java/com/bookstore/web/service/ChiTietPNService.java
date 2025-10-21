package com.bookstore.web.service;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.ChiTietPNKey;
import java.util.List;

public interface ChiTietPNService {
    List<ChiTietPN> findAll();
    ChiTietPN findById(ChiTietPNKey id);
    ChiTietPN save(ChiTietPN chiTietPN);
    void deleteById(ChiTietPNKey id);
    
 // ✅ Thêm mới: lấy danh sách chi tiết theo mã phiếu nhập
    List<ChiTietPN> findByPhieuNhap(Integer maPN);

    // ✅ Thêm mới: xóa toàn bộ chi tiết theo mã phiếu nhập
    void deleteByPhieuNhap(Integer maPN);
}
