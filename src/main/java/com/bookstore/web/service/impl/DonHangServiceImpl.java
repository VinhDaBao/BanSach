package com.bookstore.web.service.impl;

import com.bookstore.web.entity.ChiTietDonHang;
import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.repository.DonHangRepository;
import com.bookstore.web.repository.SachRepository;
import com.bookstore.web.service.DonHangService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonHangServiceImpl implements DonHangService {

	@Autowired
	private DonHangRepository donHangRepository;

	@Autowired
	private SachRepository sachRepository;

	// ===============================
	// === Các hàm chính cho người dùng ===
	// ===============================

	@Override
	public List<DonHang> getOrdersByUserId(Integer userId) {
		return donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(userId);
	}

	@Override
	public Page<DonHang> findByUserId(Integer userId, Pageable pageable) {
		return donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(userId, pageable);
	}

	// ===============================
	// === Các hàm quản lý / thống kê ===
	// ===============================

	@Override
	public double sumTongTienByStatus(String status) {
		return donHangRepository.sumTongTienByStatus(status);
	}

	@Override
	public long countByStatus(String status) {
		return donHangRepository.countByStatus(status);
	}

	@Override
	public Map<String, Long> getOrderStatusStats() {
		List<Object[]> results = donHangRepository.getOrderStatusStats();
		return results.stream().collect(Collectors.toMap(obj -> (String) obj[0], obj -> ((Number) obj[1]).longValue()));
	}

	@Override
	public double sumTongTienByDateAndStatus(LocalDateTime date, String status) {
		return donHangRepository.sumTongTienByDateAndStatus(date, status);
	}

	@Override
	public long countByDateAndStatus(LocalDateTime dateTime, String status) {
		return donHangRepository.countByDateAndStatus(dateTime, status);
	}

	@Override
	public List<DonHang> findRecentOrders(int limit) {
		Pageable pageable = PageRequest.of(0, limit, Sort.by("ngayDat").descending());
		Page<DonHang> page = donHangRepository.findAllByOrderByNgayDatDesc(pageable);
		return page.getContent();
	}

	@Override
	public Page<DonHang> findAllOrders(Pageable pageable) {
		return donHangRepository.findAllByOrderByNgayDatDesc(pageable);
	}

	@Override
	public Page<DonHang> findByTrangThai(String trangThai, Pageable pageable) {
		return donHangRepository.findByTrangThaiOrderByNgayDatDesc(trangThai, pageable);
	}

	// ===============================
	// === Các hàm CRUD ===
	// ===============================

	@Override
	public DonHang findById(Integer id) {
		return donHangRepository.findById(id).orElse(null);
	}

	@Override
	public DonHang save(DonHang donHang) {
		return donHangRepository.save(donHang);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		DonHang donHang = findById(id);
		if (donHang != null) {
			for (ChiTietDonHang item : donHang.getChiTietDonHangs()) {
				Sach sach = item.getSach();
				if (sach != null) {
					sach.setSoLuongTon(sach.getSoLuongTon() + item.getSoLuong());
					sachRepository.save(sach);
				}
			}
			donHangRepository.deleteById(id);
		}
	}

	// ===============================
	// === Cập nhật trạng thái & hủy đơn ===
	// ===============================

	@Override
	@Transactional
	public DonHang updateStatus(Integer id, String newStatus) {
		DonHang donHang = findById(id);
		if (donHang == null) {
			throw new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + id);
		}
		donHang.setTrangThai(newStatus);
		return donHangRepository.save(donHang);
	}

//	@Override
//	@Transactional
//	public void cancelOrder(Integer orderId) {
//		DonHang donHang = donHangRepository.findById(orderId)
//				.orElseThrow(() -> new IllegalStateException("Không tìm thấy đơn hàng #" + orderId));
//
//		String currentStatus = donHang.getTrangThai();
//		if (!"Đang xử lý".equals(currentStatus) && !"Chờ xác nhận".equals(currentStatus)) {
//			throw new IllegalStateException("Không thể hủy đơn hàng ở trạng thái '" + currentStatus + "'.");
//		}
//		
//		donHang.setTrangThai("Đã hủy");
//		System.out.print("ĐANG HUỶ");
//		for (ChiTietDonHang item : donHang.getChiTietDonHangs()) {
//			Sach sach = item.getSach();
//			if (sach != null) {
//				sach.setSoLuongTon(sach.getSoLuongTon() + item.getSoLuong());
//				sachRepository.save(sach);
//			}
//		}
//		System.out.print("Huỷ thành công");
//		donHangRepository.save(donHang);
//	}
	
    public boolean hasUserPurchasedBook(Integer userId, Integer bookId) {
        return donHangRepository.existsByNguoiDung_IdAndChiTietDonHangs_Sach_IdAndTrangThai(
            userId, bookId, "Đã giao" // Chỉ tính đơn đã giao
        );
    }

	
	@Override
	@Transactional
	public void cancelOrder(Integer orderId) {
	    DonHang donHang = donHangRepository.findById(orderId)
	            .orElseThrow(() -> new IllegalStateException("Không tìm thấy đơn hàng #" + orderId));

	    String currentStatus = donHang.getTrangThai();
	    if (!"Đang xử lý".equals(currentStatus) && !"Chờ xác nhận".equals(currentStatus)) {
	        throw new IllegalStateException("Không thể hủy đơn hàng ở trạng thái '" + currentStatus + "'.");
	    }

	    // ❌ KHÔNG cập nhật tồn kho trực tiếp
	    donHang.setTrangThai("Đã hủy");
	    donHangRepository.save(donHang);
	}

	@Override
	public double sumRevenueByDateRangeAndStatus(LocalDateTime startDate, LocalDateTime endDate, String status) {
		Double sum = donHangRepository.sumRevenueByDateRangeAndStatus(startDate, endDate, status);
		return sum != null ? sum : 0.0;
	}

	@Override
	public long countByDateRangeAndStatus(LocalDateTime from, LocalDateTime to, String status) {
		return donHangRepository.countByDateRangeAndStatus(from, to, status);
	}

	@Override
	public double sumRevenueByMonth(int year, int month, String status) {
		return donHangRepository.sumRevenueByMonth(year, month, status);
	}

	@Override
	public Map<String, Long> getOrderStatusStatsInRange(LocalDateTime from, LocalDateTime to) {
		List<Object[]> results = donHangRepository.getOrderStatusStatsInRange(from, to);
		return results.stream().collect(Collectors.toMap(obj -> (String) obj[0], obj -> ((Number) obj[1]).longValue()));
	}

	@Override
	public double sumRevenueByDate(LocalDateTime date, String status) {
		LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);
		Double sum = donHangRepository.sumRevenueByDate(startOfDay, endOfDay, status);
		return sum != null ? sum : 0.0;
	}

//	@Override
//	public long countBooksSoldBetween(LocalDateTime start, LocalDateTime end) {
//		return donHangRepository.countBooksSoldBetween(start, end);
//	}

	@Override
	public double sumTongTienBetween(LocalDateTime start, LocalDateTime end) {
		return donHangRepository.sumTongTienBetween(start, end);
	}

	@Override
	public long countOrdersBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return donHangRepository.countOrdersBetween(startDate, endDate);
	}

	@Override
	public long countBooksSoldBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return donHangRepository.countBooksSoldBetween(startDate, endDate);
	}

	@Override
	public double sumRevenueBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return donHangRepository.sumRevenueBetween(startDate, endDate);
	}
}
