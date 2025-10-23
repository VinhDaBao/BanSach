package com.bookstore.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bookstore.web.entity.DonHang;
import com.bookstore.web.service.DonHangService;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

	private final AdminChiTietPNController adminChiTietPNController;

	@Autowired
	private DonHangService donHangService;

	AdminOrderController(AdminChiTietPNController adminChiTietPNController) {
		this.adminChiTietPNController = adminChiTietPNController;
	}

	// Danh sách đơn hàng có phân trang + lọc trạng thái
	@GetMapping("/orders")
	public String listOrders(
			@RequestParam(value = "trangThai", required = false, defaultValue = "all") String trangThai,
			@RequestParam(defaultValue = "0") int page, Model model) {

		Pageable pageable = PageRequest.of(page, 10, Sort.by("ngayDat").descending());
		Page<DonHang> orders;

		// Kiểm tra nếu không phải "all" thì lọc theo trạng thái
		if (!"all".equalsIgnoreCase(trangThai)) {
			orders = donHangService.findByTrangThai(trangThai, pageable);
		} else {
			orders = donHangService.findAllOrders(pageable);
		}

		model.addAttribute("orders", orders);
		model.addAttribute("statuses", List.of("Chờ xác nhận", "Đã xác nhận", "Đã hủy"));
		model.addAttribute("selectedStatus", trangThai);

		return "admin/orders/list";
	}

	//  Xem chi tiết đơn hàng
	@GetMapping("/orders/{id}/detail")
	public String viewOrder(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
		DonHang order = donHangService.findById(id);
		if (order == null) {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
			return "redirect:/admin/orders";
		}
		model.addAttribute("order", order);
		return "admin/orders/detail";
	}

	//  Cập nhật trạng thái đơn hàng
	@PostMapping("/orders/{id}/update-status")
	public String updateStatus(@PathVariable Integer id, @RequestParam String newStatus,
			RedirectAttributes redirectAttributes) {
		try {
			DonHang order = donHangService.findById(id);
			if (order == null) {
			    redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
			    return "redirect:/admin/orders";
			}
			String currentStatus = order.getTrangThai().trim();
			if (currentStatus.equalsIgnoreCase("Đã huỷ") || currentStatus.equalsIgnoreCase("Đã hủy")) {
			    redirectAttributes.addFlashAttribute("error", "Đơn hàng đã bị huỷ, không thể cập nhật nữa!");
			    return "redirect:/admin/orders";
			}

			if (newStatus.trim().equalsIgnoreCase("Đã huỷ") || newStatus.trim().equalsIgnoreCase("Đã hủy")) {
			    donHangService.cancelOrder(id);
			    redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được huỷ!");
			} else {
			    donHangService.updateStatus(id, newStatus.trim());
			    redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công: " + newStatus);
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}

	// Hủy đơn hàng
	@PostMapping("/orders/{id}/cancel")
	public String cancelOrder(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			donHangService.cancelOrder(id);
			redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi hủy đơn hàng: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}

	//  Xóa đơn hàng
	@PostMapping("/orders/{id}/delete")
	public String deleteOrder(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			donHangService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa đơn hàng thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa đơn hàng: " + e.getMessage());
		}
		return "redirect:/admin/orders";
	}
}
