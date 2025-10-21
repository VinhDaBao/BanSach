package com.bookstore.web.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.web.entity.YeuThich;
import com.bookstore.web.entity.YeuThichId;

public interface YeuThichRepository extends JpaRepository<YeuThich, YeuThichId> {
	@Query("""
			SELECT new map(
			     s.id as maSach,
			     s.tenSP as tenSach,
			     s.tacGia as tacGia,
			     COUNT(y) as soLuotYeuThich
			 )
			 FROM YeuThich y
			 JOIN y.sach s
			 GROUP BY s.id, s.tenSP, s.tacGia
			 ORDER BY COUNT(y) DESC
			 """)
	Page<Map<String, Object>> findTopYeuThich(Pageable pageable);
}
