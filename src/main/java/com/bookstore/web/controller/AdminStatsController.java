package com.bookstore.web.controller;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bookstore.web.service.DonHangService;
import com.bookstore.web.service.NguoiDungService;
import com.bookstore.web.service.SachService;

@Controller
@RequestMapping("/admin")
public class AdminStatsController {

	@Autowired
	private SachService sachService;

	@Autowired
	private DonHangService donHangService;

	@Autowired
	private NguoiDungService nguoiDungService;

	@GetMapping("/thongke")
	public String thongKe(Model model, @RequestParam(name = "range", defaultValue = "today") String range) {

		// 🕓 Xác định khoảng thời gian lọc
		LocalDateTime[] rangeDates = getDateRange(range);
		LocalDateTime from = rangeDates[0];
		LocalDateTime to = rangeDates[1];

		// Thống kê tổng quan
		double totalRevenue = donHangService.sumRevenueByDateRangeAndStatus(from, to, "Đã xác nhận");
		long totalOrders = donHangService.countByDateRangeAndStatus(from, to, "Đã xác nhận");
		long totalBooks = sachService.countAll();
		long totalCustomers = nguoiDungService.countByVaiTro("USER");
		long newCustomers = nguoiDungService.countByCreatedAtBetween(from, to);

		model.addAttribute("totalRevenue", String.format("%,.0f ₫", totalRevenue));
		model.addAttribute("totalOrders", totalOrders);
		model.addAttribute("totalBooks", totalBooks);
		model.addAttribute("totalCustomers", totalCustomers);
		model.addAttribute("newCustomers", newCustomers);

		// Sách bán chạy nhất
		Map<String, Object> topSellingBook = sachService.getTopSellingBookInRange(from, to, "Đã xác nhận");
		model.addAttribute("topSellingBook", topSellingBook != null ? topSellingBook.get("tenSP") : "N/A");

		// Biểu đồ doanh thu theo thời gian
		model.addAttribute("revenueOverTimeData", getRevenueOverTimeData(range, from, to));

		// Phân bổ doanh thu theo thể loại
		List<Map<String, Object>> genreRevenue = sachService.getRevenueByCategoryInRange(from, to, "Đã xác nhận");

		// Lấy nhãn (tên thể loại) và giá trị (doanh thu)
		List<String> labels = genreRevenue.stream().map(m -> (String) m.get("tenTL")).toList();

		List<Long> values = genreRevenue.stream().map(m -> ((Number) m.get("tongDoanhThu")).longValue()).toList();

		// Tạo object Chart.js
		Map<String, Object> genreRevenueData = new HashMap<>();
		genreRevenueData.put("labels", labels);
		genreRevenueData.put("datasets", List.of(Map.of("label", "Doanh Thu", "data", values, "backgroundColor",
				List.of("#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF", "#FF9F40"))));

		model.addAttribute("categoryRevenueData", genreRevenueData);

		// Top 10 sách bán chạy
		model.addAttribute("top10Books", sachService.getTopSellingBooks(10));

		// Tăng trưởng khách hàng
		model.addAttribute("customerGrowthData", getCustomerGrowthData(range, from, to));

		// Báo cáo tồn kho
		List<Map<String, Object>> stockReport = sachService.getDetailedStockReport();

		for (Map<String, Object> item : stockReport) {
			Object conLaiObj = item.get("conLai");
			int conLai = 0;
			if (conLaiObj instanceof Number) {
				conLai = ((Number) conLaiObj).intValue();
			}

			String canhBao = "";
			if (conLai < 10) {
				canhBao = "Sắp hết";
			} else if (conLai < 50) {
				canhBao = "Thấp";
			}

			item.put("canhBao", canhBao);
		}

		model.addAttribute("detailedStockReport", stockReport);

		// Thống kê đơn hàng theo trạng thái
		Map<String, Long> rawStats = donHangService.getOrderStatusStatsInRange(from, to);

		// Đảm bảo đủ 3 trạng thái
		Map<String, Long> orderStatusStats = new LinkedHashMap<>();
		orderStatusStats.put("Chờ xác nhận", rawStats.getOrDefault("Chờ xác nhận", 0L));
		orderStatusStats.put("Đã xác nhận", rawStats.getOrDefault("Đã xác nhận", 0L));
		orderStatusStats.put("Đã hủy", rawStats.getOrDefault("Đã hủy", 0L));

		model.addAttribute("orderStatusStats", orderStatusStats);

		// Nhãn bộ lọc hiển thị giao diện
		String label;
		switch (range) {
		case "today":
			label = "Hôm nay";
			break;
		case "week":
			label = "Tuần này";
			break;
		case "year":
			label = "Năm nay";
			break;
		default:
			label = "Tháng này";
		}
		model.addAttribute("selectedRange", label);

		return "admin/thongke/stats";
	}

	// Biểu đồ doanh thu theo thời gian
	private Map<String, Object> getRevenueOverTimeData(String range, LocalDateTime from, LocalDateTime to) {
		Map<String, Object> data = new HashMap<>();
		List<String> labels = new ArrayList<>();
		List<Double> values = new ArrayList<>();

		String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

		// Nếu là biểu đồ theo năm hoặc theo tháng thì hiển thị 12 tháng
		if ("year".equalsIgnoreCase(range) || "month".equalsIgnoreCase(range)) {
			int currentYear = LocalDate.now().getYear();

			for (int month = 1; month <= 12; month++) {
				labels.add(monthNames[month - 1]);

				double revenue = donHangService.sumRevenueByMonth(currentYear, month, "Đã xác nhận");
				values.add(revenue);
			}
		} else {
			// Nếu không phải theo năm/tháng thì hiển thị 7 ngày gần nhất
			for (int i = 6; i >= 0; i--) {
				LocalDateTime date = LocalDateTime.now().minusDays(i);
				labels.add(date.getDayOfMonth() + "/" + date.getMonthValue());
				double revenue = donHangService.sumRevenueByDate(date, "Đã xác nhận");
				values.add(revenue);
			}
		}

		data.put("labels", labels);
		data.put("datasets", List
				.of(Map.of("label", "Doanh Thu", "data", values, "borderColor", "rgb(75, 192, 192)", "tension", 0.1)));
		return data;
	}

	// Biểu đồ tăng trưởng khách hàng (giống biểu đồ doanh thu)

	private Map<String, Object> getCustomerGrowthData(String range, LocalDateTime from, LocalDateTime to) {
		Map<String, Object> data = new HashMap<>();
		List<String> labels = new ArrayList<>();
		List<Long> values = new ArrayList<>();

		String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

		// Nếu là biểu đồ theo năm hoặc theo tháng thì hiển thị 12 tháng
		if ("year".equalsIgnoreCase(range) || "month".equalsIgnoreCase(range)) {
			int currentYear = LocalDate.now().getYear();

			for (int month = 1; month <= 12; month++) {
				labels.add(monthNames[month - 1]);

				YearMonth ym = YearMonth.of(currentYear, month);
				LocalDateTime start = ym.atDay(1).atStartOfDay();
				LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

				long count = nguoiDungService.countByCreatedAtBetween(start, end);
				values.add(count);
			}
		} else {
			// Nếu không phải theo năm/tháng thì hiển thị 7 ngày gần nhất
			for (int i = 6; i >= 0; i--) {
				LocalDateTime date = LocalDateTime.now().minusDays(i);
				labels.add(date.getDayOfMonth() + "/" + date.getMonthValue());

				LocalDateTime start = date.toLocalDate().atStartOfDay();
				LocalDateTime end = date.toLocalDate().atTime(23, 59, 59);

				long count = nguoiDungService.countByCreatedAtBetween(start, end);
				values.add(count);
			}
		}

		data.put("labels", labels);
		data.put("datasets", List.of(Map.of("label", "Khách hàng mới", "data", values, "borderColor",
				"rgb(153, 102, 255)", "tension", 0.2)));
		return data;
	}

	// Xác định khoảng thời gian lọc
	private LocalDateTime[] getDateRange(String range) {
		LocalDate today = LocalDate.now();
		LocalDateTime from, to;

		switch (range) {
		case "today":
			from = today.atStartOfDay();
			to = today.atTime(23, 59, 59);
			break;
		case "week":
			from = today.with(DayOfWeek.MONDAY).atStartOfDay();
			to = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
			break;
		case "year":
			from = LocalDate.of(today.getYear(), 1, 1).atStartOfDay();
			to = LocalDate.of(today.getYear(), 12, 31).atTime(23, 59, 59);
			break;
		case "month":
		default:
			from = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
			to = today.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
			break;
		}
		return new LocalDateTime[] { from, to };
	}
}
