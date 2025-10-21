//package com.bookstore.web.service;
//
//
//import com.bookstore.web.entity.NguoiDung;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface NguoiDungService {
//    List<NguoiDung> findAll();
//    Optional<NguoiDung> findById(Integer id);
//    NguoiDung save(NguoiDung nguoiDung);
//    void deleteById(Integer id);
//    long countByVaiTro(String vaiTro);
//    long countByCreatedAtAfter(LocalDateTime date);
//    Optional<NguoiDung> findByTaiKhoan(String taiKhoan);
//    
//    NguoiDung login(String taikhoan, String matkhau);
//	Optional<NguoiDung> findById(int id);
//	boolean existsByTaiKhoan(String taiKhoan);
//	
//	void updateUser(NguoiDung nguoiDung);
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.NguoiDung;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NguoiDungService {

	// Lấy toàn bộ người dùng
	List<NguoiDung> findAll();

	// Tìm người dùng theo ID
	Optional<NguoiDung> findById(Integer id);

	// Lưu hoặc cập nhật người dùng
	NguoiDung save(NguoiDung nguoiDung);

	// Xóa người dùng theo ID
	void deleteById(Integer id);

	// Tìm người dùng theo tài khoản
	Optional<NguoiDung> findByTaiKhoan(String taiKhoan);

	// Kiểm tra tài khoản đã tồn tại
	boolean existsByTaiKhoan(String taiKhoan);

	// Đăng nhập
	NguoiDung login(String taiKhoan, String matKhau);

	// Đếm người dùng theo vai trò
	long countByVaiTro(String vaiTro);

	// Đếm người dùng đăng ký sau thời điểm nhất định
	long countByCreatedAtAfter(LocalDateTime date);

	// Cập nhật thông tin người dùng
	void updateUser(NguoiDung nguoiDung);

	// Tìm danh sách người dùng theo vai trò
	List<NguoiDung> findByVaiTro(String vaiTro);

//	long countByVaiTro(String vaiTro);
    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    
    long countAllUsers();

    long countRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate);
}