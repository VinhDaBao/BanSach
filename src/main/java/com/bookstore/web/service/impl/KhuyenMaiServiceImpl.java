package com.bookstore.web.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.repository.KhuyenMaiRepository;
import com.bookstore.web.service.KhuyenMaiService;
@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

	@Autowired
	private KhuyenMaiRepository khuyenMaiRepository;

	@Override

	public KhuyenMai save(KhuyenMai khuyenMai) {
		return khuyenMaiRepository.save(khuyenMai);
	}

	@Override
	public List<KhuyenMai> findAll() {
		return khuyenMaiRepository.findAll();
	}

	@Override
	public Optional<KhuyenMai> findById(Integer maKM) {
		return khuyenMaiRepository.findById(maKM);
	}

	@Override
	public void deleteById(Integer maKM) {
		khuyenMaiRepository.deleteById(maKM);

	}

	 @Override
	    public double calculateDiscountedPrice(KhuyenMai km, double giaBan) {
	        if (km == null) return giaBan;
	        double giaTri = km.getGiaTri(); 

	        double discountAmount = 0.0;

	        if (giaTri <= 1.0) {
	            discountAmount = giaBan * giaTri;
	        } else if (giaTri <= 100.0) {
	            discountAmount = giaBan * (giaTri / 100.0);
	        } else {
	            discountAmount = giaTri;
	        }

	        double discounted = giaBan - discountAmount;
	        if (discounted < 0) discounted = 0.0;


	        return discounted;
	    }

	 @Override
	 public List<KhuyenMai> findByTenKMContaining(String keyword) {
	     return khuyenMaiRepository.findByTenKMContainingIgnoreCase(keyword);
	 }
		@Override
		public List<KhuyenMai> findActivePromotions() {
			return khuyenMaiRepository.findActivePromotions(LocalDate.now());
		}


}
