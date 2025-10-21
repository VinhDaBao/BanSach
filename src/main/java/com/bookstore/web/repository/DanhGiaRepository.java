package com.bookstore.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.web.entity.DanhGia;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
	List<DanhGia> findBySach_IdOrderByNgayTaoDesc(Integer sachId);

	Page<DanhGia> findAllByOrderByNgayTaoDesc(Pageable pageable);

	@Query("SELECT COUNT(d) FROM DanhGia d WHERE d.sach.id = :sachId")
	int countBySach_Id(@Param("sachId") Integer sachId);

	@Query("SELECT d FROM DanhGia d WHERE d.sach.id = :sachId")
	List<DanhGia> findBySach_Id(@Param("sachId") Integer sachId);

	@Query("SELECT AVG(d.soSao) FROM DanhGia d WHERE d.sach.id = :sachId")
	Double getAverageRatingBySachId(@Param("sachId") Integer sachId);

	@Query("SELECT dg FROM DanhGia dg ORDER BY dg.ngayTao DESC")
	List<DanhGia> findAllOrderByNgayTaoDesc();

	@Query("SELECT dg FROM DanhGia dg ORDER BY dg.ngayTao DESC")
	List<DanhGia> getRecentReviews(Pageable pageable);

	@Query("SELECT DISTINCT dg FROM DanhGia dg LEFT JOIN FETCH dg.traLois ORDER BY dg.ngayTao DESC")
	List<DanhGia> findAllWithReplies();

	
}
