package com.bookstore.web.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.web.entity.DiaChi;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    List<DiaChi> findByNguoiDung_IdOrderByMacDinhDesc(Integer userId);
    Optional<DiaChi> findByNguoiDung_IdAndMacDinh(Integer userId, Boolean macDinh);
}