package com.bookstore.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.bookstore.web.entity.SachKhuyenMaiId;
import com.bookstore.web.entity.Sach_KhuyenMai;
import com.bookstore.web.repository.SachKhuyenMaiRepository;
import com.bookstore.web.service.SachKhuyenMaiService;

@Service
public class SachKhuyenMaiServiceImpl implements SachKhuyenMaiService{

	@Autowired
	SachKhuyenMaiRepository sachKhuyenMaiRepository;
	@Override
	public List<Sach_KhuyenMai> findByKhuyenMai_MaKM(Integer maKM) {
		return sachKhuyenMaiRepository.findByKhuyenMai_MaKM(maKM);
	}
	@Override
	public boolean existsById(SachKhuyenMaiId id) {
		return sachKhuyenMaiRepository.existsById(id);
	}
	@Override
	public void save(Sach_KhuyenMai skm) {
		sachKhuyenMaiRepository.save(skm);
	}
	@Override
	public void deleteById(SachKhuyenMaiId id) {
		sachKhuyenMaiRepository.deleteById(id);
	}

}
