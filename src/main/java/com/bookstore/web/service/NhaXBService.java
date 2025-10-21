package com.bookstore.web.service;

import com.bookstore.web.entity.NhaXB;
import java.util.List;

public interface NhaXBService {

    // ==================== CRUD CƠ BẢN ====================
    List<NhaXB> findAll();
    NhaXB findById(Integer maNXB);
    NhaXB findByTenNXB(String tenNXB);
    NhaXB save(NhaXB nhaXB);
    void deleteById(Integer maNXB);
    
    
    NhaXB saveIfNotExists(String tenNXB);
}
