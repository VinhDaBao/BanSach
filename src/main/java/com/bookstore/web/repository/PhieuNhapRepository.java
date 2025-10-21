package com.bookstore.web.repository;

import com.bookstore.web.entity.NhaCungCap;
import com.bookstore.web.entity.PhieuNhap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Integer> {

    // ✅ Tìm phiếu nhập theo nhà cung cấp
    @Query("SELECT p FROM PhieuNhap p WHERE p.nhaCungCap = :ncc")
    List<PhieuNhap> findByNhaCungCap(@Param("ncc") NhaCungCap nhaCungCap);
}
