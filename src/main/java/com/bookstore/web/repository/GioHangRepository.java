package com.bookstore.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.web.entity.GioHang;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByNguoiDung_Id(Integer id);
}