package com.bookstore.web.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bookstore.web.service.SachService;

@Controller
@RequestMapping("/admin")
public class TonKhoController {

	@Autowired
	private SachService sachService;

	@GetMapping("/tonkho")
	public String quanLyTonKho(@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model) {

		List<Map<String, Object>> stockReport = sachService.getDetailedStockReport();

		// Thêm trạng thái cảnh báo
		for (Map<String, Object> item : stockReport) {
			int conLai = 0;
			Object conLaiObj = item.get("conLai");
			if (conLaiObj instanceof Number) {
				conLai = ((Number) conLaiObj).intValue();
			}
			String canhBao;
			if (conLai <= 10)
				canhBao = "Sắp hết";
			else if (conLai <= 50)
				canhBao = "Thấp";
			else
				canhBao = "Ổn định";
			item.put("canhBao", canhBao);
		}

		// Lọc theo tìm kiếm
		if (search != null && !search.isBlank()) {
			stockReport.removeIf(item -> {
				Object tenSPObj = item.get("tenSP");
				Object theLoaiObj = item.get("theLoai");

				String searchLower = search.toLowerCase();

				boolean matchTenSP = tenSPObj != null && tenSPObj.toString().toLowerCase().contains(searchLower);
				boolean matchTheLoai = theLoaiObj != null && theLoaiObj.toString().toLowerCase().contains(searchLower);

				// Giữ lại nếu KHÔNG khớp với cả hai
				return !(matchTenSP || matchTheLoai);
			});
		}

		// Lọc theo trạng thái
		if (status != null && !status.isBlank()) {
			stockReport.removeIf(item -> {
				Object canhBaoObj = item.get("canhBao");
				return canhBaoObj == null || !status.equals(canhBaoObj.toString());
			});
		}

		// Phân trang
		int totalItems = stockReport.size();
		int totalPages = (int) Math.ceil((double) totalItems / size);
		int fromIndex = Math.max(0, (page - 1) * size);
		int toIndex = Math.min(fromIndex + size, totalItems);
		List<Map<String, Object>> pageItems = stockReport.subList(fromIndex, toIndex);

		model.addAttribute("stockReport", pageItems);
		model.addAttribute("search", search != null ? search : "");
		model.addAttribute("selectedStatus", status != null ? status : "");
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);

		return "admin/tonkho";
	}
}
