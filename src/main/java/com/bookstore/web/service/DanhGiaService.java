package com.bookstore.web.service;

import java.util.List;

import com.bookstore.web.dto.RatingStatsDTO;
import com.bookstore.web.entity.DanhGia;

public interface DanhGiaService {
	List<DanhGia> findBySachId(Integer sachId);
    DanhGia save(DanhGia danhGia);
    RatingStatsDTO calculateRatingStats(List<DanhGia> reviews);
    int getReviewCount(Integer sachId); 
    double getAverageRating(Integer sachId);
    List<DanhGia> findAllOrderByNgayTaoDesc();
    DanhGia findById(Integer id);
    List<DanhGia> getRecentReviews(int limit);
    List<DanhGia> findAllWithReplies();
    List<DanhGia> findAll();
    
}
