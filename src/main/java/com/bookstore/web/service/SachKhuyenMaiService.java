package com.bookstore.web.service;

import java.util.List;

import com.bookstore.web.entity.SachKhuyenMaiId;
import com.bookstore.web.entity.Sach_KhuyenMai;

public interface SachKhuyenMaiService {

	List<Sach_KhuyenMai> findByKhuyenMai_MaKM(Integer maKM);

	boolean existsById(SachKhuyenMaiId id);

	void save(Sach_KhuyenMai skm);

	void deleteById(SachKhuyenMaiId id);

}
