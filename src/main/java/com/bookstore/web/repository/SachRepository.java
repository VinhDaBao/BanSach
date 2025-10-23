//package com.bookstore.web.repository;
//
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.bookstore.web.entity.Sach;
//
//public interface SachRepository extends JpaRepository<Sach, Integer> {
//	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc", countQuery = "SELECT count(DISTINCT s) FROM Sach s")
//	Page<Sach> findAllDistinct(Pageable pageable);
//	
//	@Query("SELECT DISTINCT s FROM Sach s JOIN s.danhSachYeuThich yt WHERE yt.maTK = :userId")
//	Page<Sach> findFavoritesByUserId(@Param("userId") Integer userId, Pageable pageable);
//	
//	@Query(value = "SELECT DISTINCT s FROM Sach s JOIN s.danhSachTheLoai tl LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE tl.id = :categoryId", countQuery = "SELECT count(DISTINCT s) FROM Sach s JOIN s.danhSachTheLoai tl WHERE tl.id = :categoryId")
//	Page<Sach> findByTheLoaiId(@Param("categoryId") Integer categoryId, Pageable pageable);
//
//	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%", countQuery = "SELECT count(DISTINCT s) FROM Sach s WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%")
//	Page<Sach> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
//
//	@Query("SELECT s FROM Sach s " + "LEFT JOIN FETCH s.danhSachTheLoai " + "LEFT JOIN FETCH s.nhaXB "
//			+ "LEFT JOIN FETCH s.hinhThuc " + "WHERE s.id = :id")
//	Optional<Sach> findSachById(@Param("id") Integer id);
//
//	@Query(value = """
//		    SELECT s.tenSP, s.tacGia, COALESCE(SUM(ct.soLuong), 0) as soLuongBan
//		    FROM Sach s
//		    LEFT JOIN ChiTietDonHang ct ON s.maSach = ct.maSach
//		    GROUP BY s.maSach, s.tacGia, s.maSach
//		    ORDER BY soLuongBan DESC
//		    """, nativeQuery = true)
//		List<Map<String, Object>> getTopSellingBooks(Pageable pageable);
//
//
//	@Query(value = "SELECT s.tenSP, COALESCE(SUM(ctpn.soLuong), 0) as soLuongNhap, "
//			+ "COALESCE(SUM(ctdh.soLuong), 0) as soLuongBan, s.soLuongTon as conLai, "
//			+ "(s.soLuongTon * s.giaBan) as giaTriTonKho " + "FROM Sach s "
//			+ "LEFT JOIN ChiTietPN ctpn ON s.maSach = ctpn.maSach "
//			+ "LEFT JOIN ChiTietDonHang ctdh ON s.maSach = ctdh.maSach "
//			+ "GROUP BY s.maSach, s.tenSP, s.soLuongTon, s.giaBan", nativeQuery = true)
//	List<Map<String, Object>> getDetailedStockReport();
//
//	@Query(value = "SELECT s.tenSP, s.soLuongTon, COALESCE(SUM(ct.soLuong), 0) as daBan " + "FROM Sach s "
//			+ "LEFT JOIN ChiTietDonHang ct ON s.maSach = ct.maSach "
//			+ "GROUP BY s.maSach, s.tenSP, s.soLuongTon", nativeQuery = true)
//	List<Map<String, Object>> getStockReport();
//}

//package com.bookstore.web.repository;
//
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.bookstore.web.entity.Sach;
//
//public interface SachRepository extends JpaRepository<Sach, Integer> {
//	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc", countQuery = "SELECT count(DISTINCT s) FROM Sach s")
//	Page<Sach> findAllDistinct(Pageable pageable);
//	
//	@Query("SELECT DISTINCT s FROM Sach s JOIN s.danhSachYeuThich yt WHERE yt.maTK = :userId")
//	Page<Sach> findFavoritesByUserId(@Param("userId") Integer userId, Pageable pageable);
//	
//	@Query(value = "SELECT DISTINCT s FROM Sach s JOIN s.danhSachTheLoai tl LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE tl.id = :categoryId", countQuery = "SELECT count(DISTINCT s) FROM Sach s JOIN s.danhSachTheLoai tl WHERE tl.id = :categoryId")
//	Page<Sach> findByTheLoaiId(@Param("categoryId") Integer categoryId, Pageable pageable);
//
//	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%", countQuery = "SELECT count(DISTINCT s) FROM Sach s WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%")
//	Page<Sach> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
//
//	@Query("SELECT s FROM Sach s " + "LEFT JOIN FETCH s.danhSachTheLoai " + "LEFT JOIN FETCH s.nhaXB "
//			+ "LEFT JOIN FETCH s.hinhThuc " + "WHERE s.id = :id")
//	Optional<Sach> findSachById(@Param("id") Integer id);
//
//	@Query(value = """
//		    SELECT s.tenSP, s.tacGia, COALESCE(SUM(ct.soLuong), 0) as soLuongBan
//		    FROM Sach s
//		    LEFT JOIN ChiTietDonHang ct ON s.maSach = ct.maSach
//		    GROUP BY s.maSach, s.tacGia, s.maSach
//		    ORDER BY soLuongBan DESC
//		    """, nativeQuery = true)
//		List<Map<String, Object>> getTopSellingBooks(Pageable pageable);
//
//
//	@Query(value = "SELECT s.tenSP, COALESCE(SUM(ctpn.soLuong), 0) as soLuongNhap, "
//			+ "COALESCE(SUM(ctdh.soLuong), 0) as soLuongBan, s.soLuongTon as conLai, "
//			+ "(s.soLuongTon * s.giaBan) as giaTriTonKho " + "FROM Sach s "
//			+ "LEFT JOIN ChiTietPN ctpn ON s.maSach = ctpn.maSach "
//			+ "LEFT JOIN ChiTietDonHang ctdh ON s.maSach = ctdh.maSach "
//			+ "GROUP BY s.maSach, s.tenSP, s.soLuongTon, s.giaBan", nativeQuery = true)
//	List<Map<String, Object>> getDetailedStockReport();
//
//	@Query(value = "SELECT s.tenSP, s.soLuongTon, COALESCE(SUM(ct.soLuong), 0) as daBan " + "FROM Sach s "
//			+ "LEFT JOIN ChiTietDonHang ct ON s.maSach = ct.maSach "
//			+ "GROUP BY s.maSach, s.tenSP, s.soLuongTon", nativeQuery = true)
//	List<Map<String, Object>> getStockReport();
//	
//	 List<Sach> findByTrangThaiTrue();
//	 
//	 @Query("SELECT s FROM Sach s WHERE s.trangThai = true")
//	 List<Sach> findAllDangKinhDoanh();
//}

package com.bookstore.web.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bookstore.web.entity.Sach;

public interface SachRepository extends JpaRepository<Sach, Integer> {

	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc", countQuery = "SELECT count(DISTINCT s) FROM Sach s")
	Page<Sach> findAllDistinct(Pageable pageable);

	@Query("SELECT DISTINCT s FROM Sach s JOIN s.danhSachYeuThich yt WHERE yt.maTK = :userId")
	Page<Sach> findFavoritesByUserId(@Param("userId") Integer userId, Pageable pageable);

	@Query(value = "SELECT DISTINCT s FROM Sach s JOIN s.danhSachTheLoai tl LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE tl.id = :categoryId", countQuery = "SELECT count(DISTINCT s) FROM Sach s JOIN s.danhSachTheLoai tl WHERE tl.id = :categoryId")
	Page<Sach> findByTheLoaiId(@Param("categoryId") Integer categoryId, Pageable pageable);

	@Query(value = "SELECT DISTINCT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%", countQuery = "SELECT count(DISTINCT s) FROM Sach s WHERE s.tenSP LIKE %:keyword% OR s.tacGia LIKE %:keyword%")
	Page<Sach> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT s FROM Sach s LEFT JOIN FETCH s.danhSachTheLoai LEFT JOIN FETCH s.nhaXB LEFT JOIN FETCH s.hinhThuc WHERE s.id = :id")
	Optional<Sach> findSachById(@Param("id") Integer id);

	@Query("""
			    SELECT s.tenSP, s.tacGia, SUM(ct.soLuong) AS total
			    FROM ChiTietDonHang ct
			    JOIN ct.sach s
			    JOIN ct.donHang d
			    WHERE d.ngayDat BETWEEN :from AND :to
			      AND d.trangThai = :status
			    GROUP BY s.tenSP, s.tacGia
			    ORDER BY total DESC
			""")
	List<Object[]> getTopSellingBooksInRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
			@Param("status") String status);

	@Query("SELECT s.tenSP, SUM(ct.soLuong) AS total FROM ChiTietDonHang ct " + "JOIN ct.sach s JOIN ct.donHang d "
			+ "WHERE d.ngayDat BETWEEN :from AND :to AND d.trangThai = :status "
			+ "GROUP BY s.tenSP ORDER BY total DESC")
	List<Object[]> getTopSellingBookInRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
			@Param("status") String status);



	@Query("SELECT s.tenSP, s.soLuongTon, s.giaBan FROM Sach s")
	List<Object[]> getStockReport();

	// 🔹 Thống kê doanh thu theo thể loại trong khoảng thời gian
	@Query("SELECT tl.tenTL, SUM(ct.soLuong * ct.gia) " + "FROM ChiTietDonHang ct " + "JOIN ct.sach s "
			+ "JOIN s.danhSachTheLoai tl " + "JOIN ct.donHang dh " + "WHERE dh.trangThai = :status "
			+ "AND dh.ngayDat BETWEEN :from AND :to " + "GROUP BY tl.tenTL")
	List<Object[]> getRevenueByCategoryInRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
			@Param("status") String status);

//	@Query(value = """
//	        SELECT 
//	            s.MaSach,
//	            s.tenSP,
//	            s.tacGia,
//	            GROUP_CONCAT(DISTINCT tl.TenTL SEPARATOR ', ') AS theLoai,
//	            nxb.TenNXB,
//	            s.giaBan,
//	            (COALESCE(SUM(ctdh.soLuong), 0) + s.soLuongTon) AS soLuongNhap,
//	            COALESCE(SUM(ctdh.soLuong), 0) AS soLuongBan,
//	            s.soLuongTon AS conLai,
//	            s.NgayCoHang,
//	            CASE
//	                WHEN s.soLuongTon <= 10 THEN 'Thấp'
//	                WHEN s.soLuongTon <= 30 THEN 'Sắp hết'
//	                ELSE 'Ổn định'
//	            END AS trangThai
//	        FROM sach s
//	        LEFT JOIN ChiTietDonHang ctdh ON ctdh.MaSach = s.MaSach
//	        LEFT JOIN Sach_TheLoai stl ON s.MaSach = stl.MaSach
//	         LEFT JOIN TheLoai tl ON stl.MaTL = tl.MaTL
//	        LEFT JOIN NhaXB nxb ON s.MaNXB = nxb.MaNXB
//	        GROUP BY s.MaSach, s.tenSP, s.tacGia, nxb.TenNXB, s.giaBan, s.soLuongTon, s.NgayCoHang
//	    """, nativeQuery = true)
//	List<Object[]> getDetailedStockReport();
//
	@Query(value = """
		    SELECT
		        s.MaSach,
		        s.tenSP,
		        s.tacGia,
		        GROUP_CONCAT(DISTINCT tl.TenTL SEPARATOR ', ') AS theLoai,
		        nxb.TenNXB,
		        s.giaBan,

		        -- tổng nhập từ chitietpn
		        COALESCE(tpn.soLuongNhap, 0) AS soLuongNhap,

		        -- tổng bán từ chitietdonhang (chỉ đơn đã xác nhận)
		        COALESCE(tbh.soLuongBan, 0) AS soLuongBan,

		        -- còn lại = nhập - bán (không âm)
		        GREATEST(COALESCE(tpn.soLuongNhap, 0) - COALESCE(tbh.soLuongBan, 0), 0) AS conLai,

		        s.NgayCoHang,

		        -- trạng thái dựa trên conLai vừa tính
		        CASE
		            WHEN GREATEST(COALESCE(tpn.soLuongNhap, 0) - COALESCE(tbh.soLuongBan, 0), 0) <= 10 THEN 'Thấp'
		            WHEN GREATEST(COALESCE(tpn.soLuongNhap, 0) - COALESCE(tbh.soLuongBan, 0), 0) <= 30 THEN 'Sắp hết'
		            ELSE 'Ổn định'
		        END AS trangThai

		    FROM sach s

		    -- derived table tổng nhập
		    LEFT JOIN (
		        SELECT MaSach, SUM(SoLuong) AS soLuongNhap
		        FROM chitietpn
		        GROUP BY MaSach
		    ) tpn ON tpn.MaSach = s.MaSach

		    -- derived table tổng bán (chỉ đơn đã xác nhận)
		    LEFT JOIN (
		        SELECT ctdh.MaSach, SUM(ctdh.SoLuong) AS soLuongBan
		        FROM chitietdonhang ctdh
		        JOIN donhang dh ON ctdh.MaDH = dh.MaDH
		        WHERE dh.TrangThai = 'Đã xác nhận'
		        GROUP BY ctdh.MaSach
		    ) tbh ON tbh.MaSach = s.MaSach

		    LEFT JOIN sach_theloai stl ON s.MaSach = stl.MaSach
		    LEFT JOIN theloai tl ON stl.MaTL = tl.MaTL
		    LEFT JOIN nhaxb nxb ON s.MaNXB = nxb.MaNXB

		    GROUP BY s.MaSach, s.tenSP, s.tacGia, nxb.TenNXB, s.giaBan, s.NgayCoHang
		    """, nativeQuery = true)
		List<Object[]> getDetailedStockReport();

}
