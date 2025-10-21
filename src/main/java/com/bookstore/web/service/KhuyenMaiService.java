package com.bookstore.web.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.bookstore.web.entity.KhuyenMai;


public interface KhuyenMaiService {


	List<KhuyenMai> findAll();

	KhuyenMai save(KhuyenMai khuyenMai);

	Optional<KhuyenMai> findById(Integer maKM);

	void deleteById(Integer id);

	double calculateDiscountedPrice(KhuyenMai km, double giaBan);

	List<KhuyenMai> findByTenKMContaining(String keyword);

	List<KhuyenMai> findActivePromotions();


}
