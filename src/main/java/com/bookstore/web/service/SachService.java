//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.KhuyenMai;
//import com.bookstore.web.entity.Sach;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.data.domain.*;
//
//import com.bookstore.web.entity.Sach;
//
//public interface SachService {
//    List<Sach> findAll();
//    Sach findById(Integer id);
//    void save(Sach sach);
//    void deleteById(Integer id);
//
//
//    Page<Sach> searchByKeyword(String keyword, int pageNo, int pageSize);
//    Page<Sach> getAllSach(int pageNo, int pageSize);
//    Page<Sach> findByTheLoaiId(Integer categoryId, int pageNo, int pageSize);
//	KhuyenMai getBestKhuyenMai(int maSach);
//
//    
//    
//    long countAll();
//    List<Map<String, Object>> getTopSellingBooks(int limit);
//    Map<String, Object> getTopSellingBook();
//    List<Map<String, Object>> getDetailedStockReport();
//    List<Map<String, Object>> getStockReport();
//    
//    Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize);
//}

//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.Sach;
//import com.bookstore.web.entity.KhuyenMai;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface SachService {
//
//    // ==================== CRUD CƠ BẢN ====================
//    List<Sach> findAll();
//    Page<Sach> findAll(Pageable pageable);
//    Optional<Sach> findById(Integer maSach);
//    Sach save(Sach sach);
//    void deleteById(Integer maSach);
//
//    // ==================== TÌM KIẾM & LỌC ====================
//    Sach findByTenSP(String tenSP);
//    Sach saveIfNotExists(String tenSP);
//    Page<Sach> searchByKeyword(String keyword, Pageable pageable);
//    Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable);
//    Optional<Sach> findSachById(Integer id);
//
//    // ==================== QUẢN LÝ SỐ LƯỢNG ====================
//    void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap);
//
//    // ==================== THỐNG KÊ BÁO CÁO ====================
//    List<Map<String, Object>> getTopSellingBooks(int limit);
//    Map<String, Object> getTopSellingBook();
//    List<Map<String, Object>> getDetailedStockReport();
//    List<Map<String, Object>> getStockReport();
//
//   
//    Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize); 
//
//    // ==================== KHUYẾN MÃI ====================
//    KhuyenMai getBestKhuyenMai(int maSach);
//
//    // ==================== ĐẾM SỐ LƯỢNG ====================
//    long countAll();
//    
//    List<Sach> findAllDangKinhDoanh();
//}

//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.KhuyenMai;
//import com.bookstore.web.entity.Sach;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//public interface SachService {
//
//    // ==================== CRUD CƠ BẢN ====================
//    List<Sach> findAll();
//    Page<Sach> findAll(Pageable pageable);
//    Optional<Sach> findById(Integer maSach);
//    Sach save(Sach sach);
//    void deleteById(Integer maSach);
//
//    // ==================== TÌM KIẾM & LỌC ====================
//    Sach findByTenSP(String tenSP);
//    Sach saveIfNotExists(String tenSP);
//    Page<Sach> searchByKeyword(String keyword, Pageable pageable);
//    Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable);
//    Optional<Sach> findSachById(Integer id);
//
//    // ==================== QUẢN LÝ SỐ LƯỢNG ====================
//    void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap);
//
//    // ==================== THỐNG KÊ BÁO CÁO ====================
//    List<Map<String, Object>> getTopSellingBooks(int limit);
//    Map<String, Object> getTopSellingBook();
//    List<Map<String, Object>> getDetailedStockReport();
//    List<Map<String, Object>> getStockReport();
//
//    // ==================== KHUYẾN MÃI ====================
//    KhuyenMai getBestKhuyenMai(int maSach);
//
//    // ==================== NGƯỜI DÙNG / YÊU THÍCH ====================
//    Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize);
//
//    // ==================== ĐẾM SỐ LƯỢNG ====================
//    long countAll();
//
//    // ==================== DANH SÁCH SÁCH ĐANG KINH DOANH ====================
//    List<Sach> findAllDangKinhDoanh();
//}

//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.KhuyenMai;
//import com.bookstore.web.entity.Sach;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//public interface SachService {
//	List<Sach> findAll();
//
//	Optional<Sach> findById(Integer maSach);
//
//	Sach save(Sach sach);
//
//	void deleteById(Integer maSach);
//
//	// ⚙️ Phân trang
//	Page<Sach> getAllSach(int pageNo, int pageSize);
//
//	Page<Sach> findByTheLoaiId(Integer categoryId, int pageNo, int pageSize);
//
//	Page<Sach> searchByKeyword(String keyword, int pageNo, int pageSize);
//
//	Sach findByTenSP(String tenSP);
//
//	Sach saveIfNotExists(String tenSP);
//
//	Optional<Sach> findSachById(Integer id);
//
//	void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap);
//
//	List<Map<String, Object>> getTopSellingBooks(int limit);
//
//	Map<String, Object> getTopSellingBook();
//
//
////	List<Map<String, Object>> getStockReport();
//
//	KhuyenMai getBestKhuyenMai(int maSach);
//
//	Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize);
//
////	List<Sach> findAllDangKinhDoanh();
//
//	Page<Sach> findAll(Pageable pageable);
//
//	Page<Sach> searchByKeyword(String keyword, Pageable pageable);
//
//	Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable);
//
//	long countAll();
//
//	Map<String, Object> getTopSellingBookInRange(LocalDateTime from, LocalDateTime to, String status);
//
//	List<Map<String, Object>> getTopSellingBooksInRange(int limit, LocalDateTime from, LocalDateTime to, String status);
//
//	List<Map<String, Object>> getRevenueByCategoryInRange(LocalDateTime from, LocalDateTime to, String status);
//
//	List<Map<String, Object>> getDetailedStockReport();
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SachService {
	// CRUD CƠ BẢN
	List<Sach> findAll();

	Page<Sach> findAll(Pageable pageable);

	Optional<Sach> findById(Integer maSach);

	Sach save(Sach sach);

	void deleteById(Integer maSach);

	// TÌM KIẾM & LỌC
	Sach findByTenSP(String tenSP);

	Sach saveIfNotExists(String tenSP);

	Page<Sach> searchByKeyword(String keyword, Pageable pageable);

	Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable);

	Optional<Sach> findSachById(Integer id);

	// QUẢN LÝ SỐ LƯỢNG
	void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap);

	// THỐNG KÊ BÁO CÁO
	Map<String, Object> getTopSellingBookInRange(LocalDateTime from, LocalDateTime to, String status);

	List<Map<String, Object>> getTopSellingBooksInRange(int limit, LocalDateTime from, LocalDateTime to, String status);

	List<Map<String, Object>> getRevenueByCategoryInRange(LocalDateTime from, LocalDateTime to, String status);

	List<Map<String, Object>> getDetailedStockReport();

	// KHUYẾN MÃI
	KhuyenMai getBestKhuyenMai(int maSach);

	// NGƯỜI DÙNG / YÊU THÍCH
	Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize);

	// ĐẾM SỐ LƯỢNG
	long countAll();

	// PHÂN TRANG
	Page<Sach> getAllSach(int pageNo, int pageSize);

	Page<Sach> findByTheLoaiId(Integer categoryId, int pageNo, int pageSize);

	Page<Sach> searchByKeyword(String keyword, int pageNo, int pageSize);
	
	
	List<Map<String, Object>> getTopSellingBooks(int limit);
	
	List<Map<String, Object>> getStockReport();

	List<Map<String, Object>> findTopSellingBooks();

}