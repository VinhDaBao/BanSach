package com.bookstore.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.entity.Sach_KhuyenMai;
import com.bookstore.web.service.CaculatingService;
import com.bookstore.web.service.KhuyenMaiService;
import com.bookstore.web.service.SachKhuyenMaiService;
import com.bookstore.web.service.SachService;

@Controller
public class UserController {
	@Autowired
	private KhuyenMaiService khuyenMaiService;
	@Autowired
	private SachKhuyenMaiService sachKhuyenMaiService;
	@Autowired
	private SachService sachService;
	
	@Autowired
	private CaculatingService calculatingService;

	
	
	@GetMapping("/user/orderHistory")
	public String orderHistory() {
		return "user/orderHistory";
	}
	@GetMapping("/user/dashBoard")
	public String dashBoard() {
		return "user/dashboard";
	}
	@GetMapping("/")
	public String home( Model model) {
		System.out.println("MYSQLURL = " + System.getenv("MYSQLURL"));
		System.out.println("MYSQLPASSWORD = " + System.getenv("MYSQLPASSWORD"));
	    Page<Sach> featured = sachService.getAllSach(1, 5);
	    calculatingService.calculateDiscounts(featured);
        calculatingService.calculateRatings(featured);
        model.addAttribute("featuredBooks", featured);

		model.addAttribute("pageTitle", "Trang chủ");
		List<KhuyenMai> activePromotions = khuyenMaiService.findActivePromotions();
	    model.addAttribute("activePromotions", activePromotions);
		return "user/index";
	}
    @GetMapping("/user/promotion/{maKM}")
    public String viewPromotionDetail(@PathVariable Integer maKM, Model model) {
        KhuyenMai km = khuyenMaiService.findById(maKM)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khuyến mãi!"));

        List<Sach_KhuyenMai> dsSachKM = sachKhuyenMaiService.findByKhuyenMai_MaKM(maKM);
		List<Sach> dsTatCaSach = sachService.findAll();
		  Map<Integer, Double> priceAfterMap = new HashMap<>();
	        for (Sach_KhuyenMai skm : dsSachKM) {
	            Sach s = skm.getSach();
	            if (s != null) {
	                double giaBan = s.getGiaBan().doubleValue();
	                double giaSau = khuyenMaiService.calculateDiscountedPrice(km, giaBan);
	                priceAfterMap.put(s.getId(), giaSau);
	            }
	        }
        model.addAttribute("promotion", km);
        model.addAttribute("dsSachKM", dsSachKM);
        model.addAttribute("sachList", dsTatCaSach);
        model.addAttribute("priceAfter", priceAfterMap);

        return "/user/promotion-detail.html";
    }
}
