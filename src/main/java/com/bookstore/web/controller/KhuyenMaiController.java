package com.bookstore.web.controller;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.entity.SachKhuyenMaiId;
import com.bookstore.web.entity.Sach_KhuyenMai;
import com.bookstore.web.service.KhuyenMaiService;
import com.bookstore.web.service.SachKhuyenMaiService;
import com.bookstore.web.service.SachService;
import com.bookstore.web.service.impl.KhuyenMaiServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promotion")
public class KhuyenMaiController {

	@Autowired
	private KhuyenMaiService khuyenMaiService;
	@Autowired
	private SachKhuyenMaiService sachKhuyenMaiService;
	@Autowired
	private SachService sachService;
	
	@GetMapping("/search")
	public String searchPromotions(@RequestParam("keyword") String keyword, Model model) {
	    List<KhuyenMai> result = khuyenMaiService.findByTenKMContaining(keyword);
	    model.addAttribute("promotions", result);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("currentPage", 1);
	    model.addAttribute("totalPages", 1);
	    return "admin/promotion/promotion-list";
	}
	
	@GetMapping("/promotion-list")
	
    public String promoList(Model model) {
        model.addAttribute("promotions", khuyenMaiService.findAll());
        model.addAttribute("keyword", "");
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "admin/promotion/promotion-list";
    }
	@GetMapping("/edit/{maKM}")
	public String editPromotion(@PathVariable("maKM") Integer maKM, Model model) {
	    KhuyenMai promo = khuyenMaiService.findById(maKM).orElse(null);
	    System.out.println("Ngày bắt đầu: " + promo.getNgayBD());
	    System.out.println("Ngày kết thúc: " + promo.getNgayKT());
	    model.addAttribute("promotion", promo);
	    return "admin/promotion/promotion-edit"; 
	}

	@GetMapping("/promotion-create")
	public String promoCreate(Model model) {
	    model.addAttribute("promotion", new KhuyenMai()); 

		return "admin/promotion/promotion-create";

	}
    @PostMapping("/save")
    public String savePromotion(@ModelAttribute("promotion") KhuyenMai khuyenMai,BindingResult bindingResult) {
    	if (khuyenMai.getNgayKT().isBefore(khuyenMai.getNgayBD())) {
    	    bindingResult.rejectValue("ngayKT", "error.khuyenMai", "Ngày kết thúc phải sau ngày bắt đầu!");
    	    return "admin/promotion/promotion-create";
    	}
        khuyenMaiService.save(khuyenMai);
        return "redirect:/admin/promotion/promotion-list";
    }
    @PostMapping("/update")
    public String updatePromotion(@ModelAttribute("promotion") KhuyenMai promotion, Model model) {
    	if (promotion.getNgayKT().isBefore(promotion.getNgayBD())) {
            model.addAttribute("promotion", promotion);
            model.addAttribute("errorDate", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
            return "admin/promotion/promotion-edit";
    	}
        khuyenMaiService.save(promotion);
        return "redirect:/admin/promotion/promotion-list";
    }
    @GetMapping("/delete/{id}")
    public String deleteGet(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        khuyenMaiService.deleteById(id);
        return "redirect:/admin/promotion/promotion-list";
    }
    
    
    @GetMapping("/{maKM}")
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

        return "admin/promotion/promotion-detail";
    }
    
    @PostMapping("/{maKM}/addBook")
    public String addBookToPromotion(@PathVariable Integer maKM, @RequestParam Integer maSach) {
        SachKhuyenMaiId id = new SachKhuyenMaiId(maSach, maKM);

        if (!sachKhuyenMaiService.existsById(id)) {
            Sach_KhuyenMai skm = new Sach_KhuyenMai();
            skm.setMaSach(maSach);
            skm.setMaKM(maKM);
            sachKhuyenMaiService.save(skm);
        }

        return "redirect:/admin/promotion/" + maKM;
    }

    @PostMapping("/{maKM}/removeBook")
    public String removeBookFromPromotion(@PathVariable Integer maKM, @RequestParam Integer maSach) {
        SachKhuyenMaiId id = new SachKhuyenMaiId(maSach, maKM);
        sachKhuyenMaiService.deleteById(id);
        return "redirect:/admin/pro	motion/" + maKM;
    }


}