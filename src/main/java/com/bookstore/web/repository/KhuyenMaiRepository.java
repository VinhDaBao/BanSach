package com.bookstore.web.repository;

import com.bookstore.web.entity.KhuyenMai;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {

	@Query("SELECT k FROM KhuyenMai k WHERE :now BETWEEN k.ngayBD AND k.ngayKT")
    List<KhuyenMai> findActivePromotions(@Param("now") LocalDate localDate);
	List<KhuyenMai> findByTenKMContainingIgnoreCase(String keyword);

}