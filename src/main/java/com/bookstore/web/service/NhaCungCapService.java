//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.NhaCungCap;
//import java.util.List;
//
//public interface NhaCungCapService {
//    List<NhaCungCap> findAll();
//    NhaCungCap findById(Integer id);
//    NhaCungCap save(NhaCungCap ncc);
//    void deleteById(Integer id);
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.NhaCungCap;
import java.util.List;

public interface NhaCungCapService {

    // ==================== CRUD CƠ BẢN ====================
    List<NhaCungCap> findAll();
    NhaCungCap findById(Integer maNCC);
    NhaCungCap findByTenNCC(String tenNCC);
    NhaCungCap save(NhaCungCap ncc);
    void deleteById(Integer maNCC);
}