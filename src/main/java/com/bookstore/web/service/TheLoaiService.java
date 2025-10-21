//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.TheLoai;
//import java.util.List;
//
//public interface TheLoaiService {
//    List<TheLoai> findAll();
//    TheLoai findById(Integer id);
//	List<TheLoai> getAllTheLoai();
//}

//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.TheLoai;
//import java.util.List;
//
//public interface TheLoaiService {
//    List<TheLoai> findAll();
//    TheLoai findById(Integer MaTL);
//    TheLoai save(TheLoai theLoai);
//    void deleteById(Integer MaTL);
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.TheLoai;
import java.util.List;
import java.util.Optional;

public interface TheLoaiService {

    // ==================== CRUD CƠ BẢN ====================
    List<TheLoai> findAll();                   // Lấy tất cả thể loại
    Optional<TheLoai> findById(Integer maTL);  // Tìm thể loại theo ID
    TheLoai save(TheLoai theLoai);             // Thêm hoặc cập nhật thể loại
    void deleteById(Integer maTL);             // Xóa thể loại theo ID

    // ==================== HỖ TRỢ GIAO DIỆN / TRUY VẤN ====================
    List<TheLoai> getAllTheLoai();             // Lấy danh sách thể loại để hiển thị
    TheLoai findByTenTL(String tenTL);         // Tìm thể loại theo tên (nếu cần lọc hoặc tránh trùng)
    
    // ==================== TIỆN ÍCH ====================
    long countAll();                           // Đếm tổng số thể loại
}
