package com.bookstore.web.repository;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.SachKhuyenMaiId;
import com.bookstore.web.entity.Sach_KhuyenMai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SachKhuyenMaiRepository extends JpaRepository<Sach_KhuyenMai, SachKhuyenMaiId> {
    @Query("""
        SELECT km FROM Sach_KhuyenMai skm
        JOIN skm.khuyenMai km
        WHERE skm.maSach = :maSach AND km.giaTri < 1
        ORDER BY km.giaTri DESC
        """)
    java.util.List<KhuyenMai> findMaxPercentDiscount(@Param("maSach") Integer maSach);

    @Query("""
        SELECT km FROM Sach_KhuyenMai skm
        JOIN skm.khuyenMai km
        WHERE skm.maSach = :maSach AND km.giaTri >= 1
        ORDER BY km.giaTri DESC
        """)
    java.util.List<KhuyenMai> findMaxMoneyDiscount(@Param("maSach") Integer maSach);
    List<Sach_KhuyenMai> findByKhuyenMai_MaKM(Integer maKM);


}