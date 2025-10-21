package com.bookstore.web.controller;

import java.time.LocalDateTime;
//import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.service.DanhGiaService;
import com.bookstore.web.service.DonHangService;
import com.bookstore.web.service.KhuyenMaiService;
import com.bookstore.web.service.NguoiDungService;
import com.bookstore.web.service.SachService;
import com.bookstore.web.service.TraLoiService;
import com.bookstore.web.service.YeuThichService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

	@Autowired
	private SachService sachService;

	@Autowired
	private DonHangService donHangService;

	@Autowired
	private NguoiDungService nguoiDungService;

	@Autowired
	private DanhGiaService danhGiaService;

	@Autowired
	private KhuyenMaiService khuyenMaiService;
	@Autowired
	private TraLoiService traLoiService;
	@Autowired
	private YeuThichService yeuThichService;

	@GetMapping("/dashboard")
	public String dashboard(@RequestParam(name = "range", required = false, defaultValue = "today") String range,
			Model model) {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate;

		// ✅ Xác định mốc thời gian dựa theo lựa chọn
		switch (range) {
		case "week":
			startDate = now.minusWeeks(1);
			break;
		case "month":
			startDate = now.minusMonths(1);
			break;
		case "year":
			startDate = now.minusYears(1);
			break;
		default:
			startDate = now.toLocalDate().atStartOfDay();
			break;
		}

		// ✅ Tên hiển thị trên giao diện
		String rangeDisplay;
		switch (range) {
		case "week":
			rangeDisplay = "Tuần này";
			break;
		case "month":
			rangeDisplay = "Tháng này";
			break;
		case "year":
			rangeDisplay = "Năm nay";
			break;
		default:
			rangeDisplay = "Hôm nay";
			break;
		}

		// ✅ Gửi nhãn hiển thị sang view
		model.addAttribute("rangeDisplay", rangeDisplay);

		// ✅ Sách đã bán
		long totalBooksSold = donHangService.countBooksSoldBetween(startDate, now);

		// ✅ Doanh thu
		double revenue = donHangService.sumRevenueBetween(startDate, now);

		// ✅ Đơn hàng mới
		long newOrders = donHangService.countOrdersBetween(startDate, now);

		// ✅ Khách hàng đăng ký
		long registeredUsers = nguoiDungService.countRegisteredBetween(startDate, now);

		NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
		// ✅ Gửi dữ liệu sang view
		model.addAttribute("totalBooksSold", totalBooksSold);
		model.addAttribute("newOrders", newOrders);
		model.addAttribute("dailyRevenue", numberFormat.format(revenue) + " ₫");
		model.addAttribute("registeredUsers", registeredUsers);

		// ✅ Đánh giá gần đây
		List<DanhGia> recentReviews = danhGiaService.getRecentReviews(3);
		model.addAttribute("recentReviews", recentReviews);

		// Top sách được yêu thích nhất
		List<Map<String, Object>> topFavoriteBooks = yeuThichService.getTop5SachYeuThich();
		model.addAttribute("topFavoriteBooks", topFavoriteBooks);
		// Đơn hàng gần đây
		List<DonHang> recentOrders = donHangService.findRecentOrders(5);
		model.addAttribute("recentOrders", recentOrders);

		// Khuyến mãi đang chạy
		List<KhuyenMai> activePromotions = khuyenMaiService.findActivePromotions();
		model.addAttribute("activePromotions", activePromotions);

		return "admin/dashboard";
	}

	// Form trả lời
	@GetMapping("/review/reply/{maDG}")
	public String showReplyForm(@PathVariable Integer maDG, Model model) {
		DanhGia review = danhGiaService.findById(maDG);
		model.addAttribute("review", review);
		return "admin/danhgia/reply-form";
	}

	// ✅ Trang chi tiết đơn hàng
	@GetMapping("orders/detail/{id}")
	public String viewOrderDetail(@PathVariable("id") Integer id, Model model) {
		DonHang order = donHangService.findById(id);
		if (order == null) {
			model.addAttribute("error", "Không tìm thấy đơn hàng #" + id);
			return "redirect:/admin/orders";
		}
		model.addAttribute("order", order);
		return "admin/orders/detail"; // file templates/admin/orders/detail.html
	}

	@PostMapping("/review/reply/{maDG}")
	public ResponseEntity<?> replyToReview(@PathVariable Integer maDG, @RequestParam("noiDung") String noiDung) {
		try {
			// Không cần session, maTK = null
			traLoiService.saveReply(maDG, null, noiDung);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi gửi phản hồi"));
		}
	}

	@PostMapping("/review/reply/update/{id}")
	@ResponseBody
	public ResponseEntity<?> updateReply(@PathVariable("id") Integer id,
			@RequestParam("noiDungMoi") String noiDungMoi) {
		try {
			traLoiService.updateReply(id, noiDungMoi);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi cập nhật phản hồi"));
		}
	}

	@PostMapping("/review/reply/delete/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteReply(@PathVariable("id") Integer id) {
		try {
			traLoiService.deleteReply(id);
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi xóa phản hồi"));
		}
	}

	@GetMapping("/review-all")
	public String viewAllReviews(Model model) {
		List<DanhGia> allReviews = danhGiaService.findAll();
		model.addAttribute("allReviews", allReviews);
		return "admin/review-all"; // file templates/admin/review-all.html
	}

}