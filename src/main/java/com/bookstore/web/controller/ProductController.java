package com.bookstore.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.dto.RatingStatsDTO;
import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.entity.TheLoai;
import com.bookstore.web.entity.YeuThich;
import com.bookstore.web.entity.YeuThichId;
import com.bookstore.web.repository.DonHangRepository;
import com.bookstore.web.service.DanhGiaService;
import com.bookstore.web.service.SachService;
import com.bookstore.web.service.TheLoaiService;
import com.bookstore.web.service.TraLoiService;
import com.bookstore.web.service.YeuThichService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private SachService sachService;
	@Autowired
	private TheLoaiService theLoaiService;
	@Autowired
	private DanhGiaService danhGiaService;
	@Autowired
	private YeuThichService yeuThichService;
	@Autowired
	private DonHangRepository donHangRepository;
	@Autowired
	private TraLoiService traLoiService;

	/** =================== TẤT CẢ SẢN PHẨM =================== */
	@GetMapping("/products")
	public String viewAllProducts(@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model) {
		int pageSize = 32;
		Page<Sach> sachPage = sachService.getAllSach(pageNo, pageSize);

		calculateDiscounts(sachPage);
		calculateRatings(sachPage);

		model.addAttribute("sachPage", sachPage);
		model.addAttribute("listTheLoai", theLoaiService.getAllTheLoai());
		model.addAttribute("selectedCategoryId", null);
		model.addAttribute("pageTitle", "Tất cả sản phẩm");
		model.addAttribute("currentPage", pageNo);
		return "user/product";
	}

	/** =================== LỌC THEO THỂ LOẠI =================== */
	@GetMapping("/products/category/{categoryId}")
	public String viewProductsByCategory(@PathVariable("categoryId") Integer categoryId,
			@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model) {
		int pageSize = 32;
		Page<Sach> sachPage = sachService.findByTheLoaiId(categoryId, pageNo, pageSize);

		TheLoai selectedCategory = theLoaiService.findById(categoryId).orElse(null);

		calculateDiscounts(sachPage);
		calculateRatings(sachPage);

		model.addAttribute("sachPage", sachPage);
		model.addAttribute("listTheLoai", theLoaiService.getAllTheLoai());
		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("pageTitle", (selectedCategory != null ? selectedCategory.getTenTL() : "Sản phẩm"));
		model.addAttribute("currentPage", pageNo);
		return "user/product";
	}

	/** =================== TÌM KIẾM =================== */
	@GetMapping("/search")
	public String searchProducts(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model) {
		int pageSize = 32;
		Page<Sach> sachPage = sachService.searchByKeyword(keyword, pageNo, pageSize);

		calculateDiscounts(sachPage);
		calculateRatings(sachPage);

		model.addAttribute("sachPage", sachPage);
		model.addAttribute("listTheLoai", theLoaiService.getAllTheLoai());
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedCategoryId", null);
		model.addAttribute("pageTitle", "Kết quả tìm kiếm cho: '" + keyword + "'");
		model.addAttribute("currentPage", pageNo);
		return "user/product";
	}

	@GetMapping("/product-detail/{id}")
	public String productDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
		Sach sach = sachService.findById(id).orElse(null);
		if (sach == null) {
			model.addAttribute("error", "Sách không tồn tại.");
			return "redirect:/products";
		}

		List<DanhGia> reviews = danhGiaService.findBySachId(id);

		// Nạp danh sách phản hồi cho từng đánh giá
		for (DanhGia review : reviews) {
			review.setTraLois(traLoiService.findByDanhGiaId(review.getId()));
		}

		// Tính khuyến mãi
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

		// ⭐ KIỂM TRA ĐIỀU KIỆN ĐÁNH GIÁ
		NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
		boolean canReview = false;
		boolean hasReviewed = false;
		boolean hasPurchased = false;
		
		if (loggedUser != null) {
			YeuThichId favId = new YeuThichId(loggedUser.getId(), id);
			sach.setIsFavorite(yeuThichService.existsById(favId));
			hasPurchased = donHangRepository.existsByNguoiDung_IdAndChiTietDonHangs_Sach_IdAndTrangThai(
				loggedUser.getId(), id, "Đã xác nhận"
			);
			hasReviewed = danhGiaService.hasUserReviewedBook(loggedUser.getId(), id);	
			canReview = hasPurchased && !hasReviewed;
		} else {
			sach.setIsFavorite(false);
		}

		RatingStatsDTO ratingStats = danhGiaService.calculateRatingStats(reviews);
		Integer selectedCategoryId = sach.getDanhSachTheLoai().isEmpty() ? null
				: sach.getDanhSachTheLoai().iterator().next().getId();

		model.addAttribute("sach", sach);
		model.addAttribute("listTheLoai", theLoaiService.getAllTheLoai());
		model.addAttribute("selectedCategoryId", selectedCategoryId);
		model.addAttribute("reviews", reviews);
		model.addAttribute("ratingStats", ratingStats);
		model.addAttribute("pageTitle", sach.getTenSP());
		
		// ⭐ THÊM CÁC BIẾN KIỂM TRA
		model.addAttribute("canReview", canReview);
		model.addAttribute("hasReviewed", hasReviewed);
		model.addAttribute("hasPurchased", hasPurchased);

		return "user/product-detail";
	}

	private boolean isActivePromotion(KhuyenMai km) {
		LocalDate today = LocalDate.now();
		return (today.isAfter(km.getNgayBD()) || today.isEqual(km.getNgayBD()))
				&& (today.isBefore(km.getNgayKT()) || today.isEqual(km.getNgayKT()));
	}

	/** =================== YÊU THÍCH =================== */
	@PostMapping("/favorite/toggle/{sachId}")
	public String toggleFavorite(@PathVariable("sachId") Integer sachId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
		if (loggedUser == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để yêu thích sản phẩm.");
			return "redirect:/login";
		}

		YeuThichId id = new YeuThichId(loggedUser.getId(), sachId);
		if (yeuThichService.existsById(id)) {
			yeuThichService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khỏi yêu thích.");
		} else {
			YeuThich favorite = new YeuThich();
			favorite.setMaTK(loggedUser.getId());
			favorite.setMaSach(sachId);
			yeuThichService.save(favorite);
			redirectAttributes.addFlashAttribute("successMessage", "Đã thêm vào yêu thích.");
		}
		return "redirect:/product-detail/" + sachId;
	}

	/** =================== SẢN PHẨM YÊU THÍCH =================== */
	@GetMapping("/user/favorites")
	public String favorites(@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model,
			HttpSession session) {
		NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
		if (loggedUser == null)
			return "redirect:/login";

		Page<Sach> sachPage = sachService.getFavoritesByUserId(loggedUser.getId(), pageNo, 32);
		calculateDiscounts(sachPage);
		calculateRatings(sachPage);

		model.addAttribute("sachPage", sachPage);
		model.addAttribute("listTheLoai", theLoaiService.getAllTheLoai());
		model.addAttribute("selectedCategoryId", null);
		model.addAttribute("pageTitle", "Sản phẩm yêu thích");
		model.addAttribute("currentPage", pageNo);
		return "user/product";
	}

	/** =================== TÍNH GIẢM GIÁ =================== */
	private void calculateDiscounts(Page<Sach> sachPage) {
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

	/** =================== TÍNH ĐÁNH GIÁ =================== */
	private void calculateRatings(Page<Sach> sachPage) {
		sachPage.getContent().forEach(sach -> {
			sach.setTotalReviews(danhGiaService.getReviewCount(sach.getId()));
			sach.setAverageRating(danhGiaService.getAverageRating(sach.getId()));
		});
	}

	@PostMapping("/reviews/add")
	public String addReview(@RequestParam("bookId") Integer bookId, 
	                       @RequestParam("rating") int rating,
	                       @RequestParam("comment") String comment, 
	                       @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
	                       HttpSession session, 
	                       RedirectAttributes redirectAttributes) {

	    NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
	    
	    if (loggedUser == null) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để đánh giá.");
	        return "redirect:/login";
	    }

	    boolean hasPurchased = donHangRepository.existsByNguoiDung_IdAndChiTietDonHangs_Sach_IdAndTrangThai(
	        loggedUser.getId(), bookId, "Đã xác nhận"
	    );
	    
	    if (!hasPurchased) {
	        redirectAttributes.addFlashAttribute("errorMessage", 
	            "Bạn cần mua sách này và đơn hàng phải được xác nhận trước khi đánh giá.");
	        return "redirect:/product-detail/" + bookId + "#reviews";
	    }

	    boolean hasReviewed = danhGiaService.hasUserReviewedBook(loggedUser.getId(), bookId);
	    
	    if (hasReviewed) {
	        redirectAttributes.addFlashAttribute("errorMessage", 
	            "Bạn đã đánh giá sách này rồi. Mỗi tài khoản chỉ được đánh giá 1 lần.");
	        return "redirect:/product-detail/" + bookId + "#reviews";
	    }

	    try {
	        Optional<Sach> sachOpt = sachService.findById(bookId);
	        if (!sachOpt.isPresent()) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại.");
	            return "redirect:/product-detail/" + bookId;
	        }

	        Sach sach = sachOpt.get();
	        
	        DanhGia newReview = new DanhGia();
	        newReview.setSach(sach);
	        newReview.setNguoiDung(loggedUser);
	        newReview.setSoSao(rating);
	        newReview.setNoiDung(comment);
	        newReview.setNgayTao(LocalDateTime.now());

	        List<String> savedFileNames = new ArrayList<>();
	        if (imageFiles != null) {
	            for (MultipartFile file : imageFiles) {
	                if (file != null && !file.isEmpty()) {
	                    Path uploadDir = Paths.get("uploads");
	                    if (!Files.exists(uploadDir)) {
	                        Files.createDirectories(uploadDir);
	                    }

	                    String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                    Path filePath = uploadDir.resolve(uniqueFilename);

	                    try (InputStream inputStream = file.getInputStream()) {
	                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	                        savedFileNames.add(uniqueFilename);
	                    }
	                }
	            }
	        }

	        if (!savedFileNames.isEmpty()) {
	            newReview.setMediaUrls(String.join(";", savedFileNames));
	        }

	        danhGiaService.save(newReview);
	        redirectAttributes.addFlashAttribute("successMessage", "Gửi đánh giá thành công!");

	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Đã có lỗi xảy ra: " + e.getMessage());
	    }

	    return "redirect:/product-detail/" + bookId + "#reviews";
	}
	
	@PostMapping("/reviews/update")
	public String updateReview(@RequestParam("reviewId") Integer reviewId, @RequestParam("comment") String comment,
			@RequestParam("rating") int rating,
			@RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles, HttpSession session,
			RedirectAttributes redirectAttributes) {

		NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
		if (loggedUser == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để sửa đánh giá.");
			return "redirect:/login";
		}

		DanhGia review = danhGiaService.findById(reviewId);
		if (review == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Đánh giá không tồn tại.");
			return "redirect:/";
		}

		if (!review.getNguoiDung().getId().equals(loggedUser.getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể sửa đánh giá của người khác.");
			return "redirect:/product-detail/" + review.getSach().getId();
		}

		review.setNoiDung(comment);
		review.setSoSao(rating);
		review.setNgayTao(LocalDateTime.now());

		// Handle file uploads
		if (imageFiles != null && imageFiles.length > 0) {
			List<String> savedFileNames = new ArrayList<>();
			try {
				Path uploadDir = Paths.get("uploads");
				if (!Files.exists(uploadDir)) {
					Files.createDirectories(uploadDir);
				}
				for (MultipartFile file : imageFiles) {
					if (file != null && !file.isEmpty()) {
						String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
						Path filePath = uploadDir.resolve(uniqueFilename);
						try (InputStream inputStream = file.getInputStream()) {
							Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
							savedFileNames.add(uniqueFilename);
						}
					}
				}
				if (!savedFileNames.isEmpty()) {
					String mediaUrls = String.join(";", savedFileNames);
					review.setMediaUrls(mediaUrls); // Overwrite existing mediaUrls or append as needed
				}
			} catch (IOException e) {
				redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu tệp: " + e.getMessage());
			    return "redirect:/product-detail/" + review.getSach().getId() + "#reviews";
			}
		}

		danhGiaService.save(review);
		redirectAttributes.addFlashAttribute("successMessage", "Cập nhật đánh giá thành công!");
	    return "redirect:/product-detail/" + review.getSach().getId() + "#reviews";
	}

	@GetMapping("/reviews/delete/{id}")
	public String deleteReview(@PathVariable("id") Integer id, HttpSession session,
			RedirectAttributes redirectAttributes) {

		NguoiDung loggedUser = (NguoiDung) session.getAttribute("loggedUser");
		if (loggedUser == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để xóa đánh giá.");
			return "redirect:/login";
		}

		DanhGia review = danhGiaService.findById(id);
		if (review == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Đánh giá không tồn tại.");
			return "redirect:/";
		}

		if (!review.getNguoiDung().getId().equals(loggedUser.getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể xóa đánh giá của người khác.");
			return "redirect:/product-detail/" + review.getSach().getId();
		}

		danhGiaService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Xóa đánh giá thành công!");
		return "redirect:/product-detail/" + review.getSach().getId();
	}

}
