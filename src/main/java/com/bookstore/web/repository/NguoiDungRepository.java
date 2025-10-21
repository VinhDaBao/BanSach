//package com.bookstore.web.repository;
//
//import com.bookstore.web.entity.NguoiDung;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Repository
//public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
//
//    @Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.vaiTro = :vaiTro")
//    long countByVaiTro(@Param("vaiTro") String vaiTro);
//
//    @Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.createdAt > :date AND n.vaiTro = 'USER'")
//    long countByCreatedAtAfter(@Param("date") LocalDateTime date);
//
//    Optional<NguoiDung> findByTaiKhoan(String taiKhoan);
//    
//    NguoiDung findByTaiKhoanAndMatKhau(String taiKhoan, String matKhau);
//    
//    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM NguoiDung n WHERE n.taiKhoan = :taiKhoan")
//    boolean existsByTaiKhoan(@Param("taiKhoan") String taiKhoan);
//}

package com.bookstore.web.repository;

import com.bookstore.web.entity.NguoiDung;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

	// 🔹 Đếm số người dùng theo vai trò (USER, ADMIN, ...)
	@Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.vaiTro = :vaiTro")
	long countByVaiTro(@Param("vaiTro") String vaiTro);

	// 🔹 Đếm số người dùng đăng ký sau một thời điểm nhất định (chỉ tính USER)
	@Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.ngayTao > :date AND n.vaiTro = 'USER'")
	long countByCreatedAtAfter(@Param("date") LocalDateTime date);

	// 🔹 Tìm người dùng theo tài khoản
	@Query("SELECT n FROM NguoiDung n WHERE n.taiKhoan = :taiKhoan")
	Optional<NguoiDung> findByTaiKhoan(@Param("taiKhoan") String taiKhoan);

	// 🔹 Đăng nhập: tìm người dùng theo tài khoản và mật khẩu
	NguoiDung findByTaiKhoanAndMatKhau(String taiKhoan, String matKhau);

	// 🔹 Kiểm tra tài khoản đã tồn tại chưa (dùng cho đăng ký)
	@Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM NguoiDung n WHERE n.taiKhoan = :taiKhoan")
	boolean existsByTaiKhoan(@Param("taiKhoan") String taiKhoan);

	// 🔹 Lấy danh sách người dùng theo vai trò
	@Query("SELECT n FROM NguoiDung n WHERE n.vaiTro = :vaiTro")
	List<NguoiDung> findByVaiTro(@Param("vaiTro") String vaiTro);

	// 🔹 Lấy tất cả khách hàng (vai trò USER)
	@Query("SELECT n FROM NguoiDung n WHERE n.vaiTro = 'USER'")
	List<NguoiDung> findAllCustomers();

	// 🔹 Tìm kiếm khách hàng theo tên, tài khoản hoặc số điện thoại
	@Query("""
			    SELECT n FROM NguoiDung n
			    WHERE n.vaiTro = 'USER'
			    AND (
			        LOWER(n.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(n.taiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(n.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    )
			""")
	List<NguoiDung> searchCustomers(@Param("keyword") String keyword);

//	// Đếm số người dùng theo vai trò
//	long countByVaiTro(String vaiTro);

	// Đếm số người dùng tạo mới trong khoảng thời gian
	@Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.ngayTao BETWEEN :from AND :to")
	long countByNgayTaoBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

	// ✅ Tổng số khách hàng đăng ký (vai trò USER)
	@Query("SELECT COUNT(n) FROM NguoiDung n WHERE n.vaiTro = 'USER'")
	long countAllUsers();

	// ✅ Tổng khách hàng đăng ký trong khoảng thời gian
	@Query("SELECT COUNT(n) FROM NguoiDung n "
			+ "WHERE n.vaiTro = 'USER' AND n.ngayTao BETWEEN :startDate AND :endDate")
	long countRegisteredBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
