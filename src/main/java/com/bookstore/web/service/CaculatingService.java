package com.bookstore.web.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
@Service
public class CaculatingService {
    @Autowired
    private SachService sachService;

    @Autowired
    private TheLoaiService theLoaiService;
    
    @Autowired
    private DanhGiaService danhGiaService;
    public void calculateDiscounts(Page<Sach> sachPage) {
        sachPage.getContent().forEach(sach -> {
            KhuyenMai km = sachService.getBestKhuyenMai(sach.getId());
            if (km != null && isActivePromotion(km)) {
                BigDecimal original = sach.getGiaBan();
                double gt = km.getGiaTri();
                BigDecimal discounted;
                String badge;
                if (gt < 1.0) { 
                    discounted = original.multiply(BigDecimal.valueOf(1 - gt));
                    badge = String.format("%.0f%% OFF", gt * 100); 
                } else {  
                    discounted = original.subtract(BigDecimal.valueOf(gt));
                    badge = String.format("%.0fk VND OFF", gt / 1000); 
                }
                sach.setGiaGiam(discounted);
                sach.setBadgeDiscount(badge);
            }
        });
    }
    public void calculateRatings(Page<Sach> sachPage) {
        sachPage.getContent().forEach(sach -> {
        	Integer totalReviews = danhGiaService.getReviewCount(sach.getId()); 
            double avgRating = danhGiaService.getAverageRating(sach.getId());
            sach.setTotalReviews(totalReviews);  
            sach.setAverageRating(avgRating);
        });
    }
    public boolean isActivePromotion(KhuyenMai km) {
        LocalDate today = LocalDate.now();  
        return (today.isAfter(km.getNgayBD()) || today.isEqual(km.getNgayBD())) 
               && (today.isBefore(km.getNgayKT()) || today.isEqual(km.getNgayKT()));
    }

}
