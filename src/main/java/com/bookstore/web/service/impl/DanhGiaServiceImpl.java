package com.bookstore.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.web.dto.RatingStatsDTO;
import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.repository.DanhGiaRepository;
import com.bookstore.web.service.DanhGiaService;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    @Override
    public List<DanhGia> findBySachId(Integer sachId) {
        return danhGiaRepository.findBySach_IdOrderByNgayTaoDesc(sachId);
    }

    @Override
    public RatingStatsDTO calculateRatingStats(List<DanhGia> reviews) {
        RatingStatsDTO stats = new RatingStatsDTO();
        stats.setRatingCounts(new HashMap<>());
        stats.setRatingPercentages(new HashMap<>());
        for (int i = 1; i <= 5; i++) {
            stats.getRatingPercentages().put(i, 0);
        }

        if (reviews == null || reviews.isEmpty()) {
            return stats; 
        }
        stats.setTotalRatings(reviews.size());
        double sum = reviews.stream().mapToDouble(DanhGia::getSoSao).sum();
        stats.setAverageRating(sum / reviews.size());
        Map<Integer, Long> counts = reviews.stream()
                .collect(Collectors.groupingBy(DanhGia::getSoSao, Collectors.counting()));
        stats.setRatingCounts(counts);
        for (int i = 1; i <= 5; i++) {
            long count = counts.getOrDefault(i, 0L);
            int percentage = (int) Math.round((double) count * 100 / reviews.size());
            stats.getRatingPercentages().put(i, percentage);
        }
        return stats;
    }
    @Override
    public DanhGia save(DanhGia danhGia) {        
        return danhGiaRepository.save(danhGia);
    }
    @Override
    public int getReviewCount(Integer sachId) {
        return danhGiaRepository.countBySach_Id(sachId);
    }

    @Override
    public double getAverageRating(Integer sachId) {
        Long count = (long) danhGiaRepository.countBySach_Id(sachId);  
        if (count == 0) return 0.0;
        
        Double avg = danhGiaRepository.getAverageRatingBySachId(sachId); 
        return avg != null ? avg : 0.0;
    }
    
    @Override
    public List<DanhGia> findAllOrderByNgayTaoDesc() {
        return danhGiaRepository.findAllOrderByNgayTaoDesc();
    }

    @Override
    public DanhGia findById(Integer id) {
        return danhGiaRepository.findById(id).orElse(null);
    }

    @Override
    public List<DanhGia> getRecentReviews(int limit) {
        return danhGiaRepository.getRecentReviews(PageRequest.of(0, limit));
    }
    @Override
    public List<DanhGia> findAllWithReplies() {
        return danhGiaRepository.findAllWithReplies();
    }

    @Override
    public List<DanhGia> findAll() {
        return danhGiaRepository.findAll();
    }
    
}
