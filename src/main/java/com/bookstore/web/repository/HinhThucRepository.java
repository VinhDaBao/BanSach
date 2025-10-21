package com.bookstore.web.repository;

import com.bookstore.web.entity.HinhThuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhThucRepository extends JpaRepository<HinhThuc, Integer> {

    // ✅ Cách 2: Dùng @Query để tránh lỗi do khác biệt hoa/thường
	@Query("SELECT h FROM HinhThuc h WHERE h.tenHT = :tenHT")
    HinhThuc findByTenHT(@Param("tenHT") String tenHT);
}
