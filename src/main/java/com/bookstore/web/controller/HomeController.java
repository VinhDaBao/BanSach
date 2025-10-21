package com.bookstore.web.controller;



import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.repository.KhuyenMaiRepository;
import com.bookstore.web.service.SachService;
import com.bookstore.web.service.TheLoaiService;

@Controller
public class HomeController {
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;
    
	@GetMapping("/test-active-promotions")
	@ResponseBody
	public List<KhuyenMai> testActivePromotions() {
	    return khuyenMaiRepository.findActivePromotions(LocalDate.now());
	}
	
	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user/dashboard";
	}

	@GetMapping("/admin")
	public String testLayout() {
		return "admin/admin-base";
	}

}
