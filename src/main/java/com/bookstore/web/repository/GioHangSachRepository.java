package com.bookstore.web.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.web.entity.GioHangSachId;
import com.bookstore.web.entity.GioHang_Sach;

import java.util.List;
import java.util.Optional;
	
public interface GioHangSachRepository extends JpaRepository<GioHang_Sach, GioHangSachId> {
    List<GioHang_Sach> findByMaGH(Integer maGH);  // Lấy items theo giỏ hàng ID
    int countByGioHangMaGH(@Param("maGH") Integer maGH);
    Optional<GioHang_Sach> findByGioHangMaGHAndSachId(Integer maGH, Integer sachId);
}