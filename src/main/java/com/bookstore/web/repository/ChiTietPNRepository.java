package com.bookstore.web.repository;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.ChiTietPNKey;
import com.bookstore.web.entity.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChiTietPNRepository extends JpaRepository<ChiTietPN, ChiTietPNKey> {

	 // ✅ Lấy danh sách chi tiết theo mã phiếu nhập
	@Query("SELECT c FROM ChiTietPN c WHERE c.phieuNhap.maPN = :maPN")
    List<ChiTietPN> findByMaPhieuNhap(@Param("maPN") Integer maPN);

    @Transactional
    @Modifying
    @Query("DELETE FROM ChiTietPN c WHERE c.phieuNhap.maPN = :maPN")
    void deleteByMaPhieuNhap(@Param("maPN") Integer maPN);
}
