package com.bookstore.web.repository;

import com.bookstore.web.entity.ChiTietDonHang;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
	// Tìm sách bán chạy nhất (top 1)
	@Query("""
		    SELECT new map(
		        ctdh.sach.tenSP as tenSP,
		        ctdh.sach.tacGia as tacGia,
		        SUM(ctdh.soLuong) as soLuongBan
		    )
		    FROM ChiTietDonHang ctdh
		    JOIN ctdh.donHang dh
		    WHERE dh.trangThai = 'Đã xác nhận'
		    GROUP BY ctdh.sach.tenSP, ctdh.sach.tacGia
		    ORDER BY SUM(ctdh.soLuong) DESC
		    """)
		List<Map<String, Object>> findTopSellingBooks();

}