//package com.bookstore.web.service;
//
//
//import java.time.LocalDateTime;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import com.bookstore.web.entity.DonHang;
//
//public interface DonHangService {
//	List<DonHang> getOrdersByUserId(Integer userId);
//
//	void cancelOrder(Integer orderId);
//
//	double sumTongTienByStatus(String status);
//
//	long countByStatus(String status);
//
//	Map<String, Long> getOrderStatusStats();
//
//	double sumTongTienByDateAndStatus(LocalDateTime date, String status);
//
//	long countByDateAndStatus(LocalDateTime dateTime, String status);
//
//	List<DonHang> findRecentOrders(int limit);
//
//	Page<DonHang> findAllOrders(Pageable pageable);
//
//	Page<DonHang> findByTrangThai(String trangThai, Pageable pageable);
//
//	Page<DonHang> findByUserId(Integer userId, Pageable pageable);
//
//	DonHang findById(Integer id);
//
//	DonHang updateStatus(Integer id, String newStatus);
//
//	void deleteById(Integer id);
//}

package com.bookstore.web.service;

import com.bookstore.web.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DonHangService {

	// Lấy tất cả đơn hàng (phân trang)
	Page<DonHang> findAllOrders(Pageable pageable);

	// Lấy danh sách đơn hàng của người dùng (phân trang)
	Page<DonHang> findByUserId(Integer userId, Pageable pageable);

	// Lấy danh sách đơn hàng theo người dùng (không phân trang)
	List<DonHang> getOrdersByUserId(Integer userId);

	// Lấy danh sách đơn hàng theo trạng thái (phân trang)
	Page<DonHang> findByTrangThai(String trangThai, Pageable pageable);

	// Tìm đơn hàng theo ID
	DonHang findById(Integer id);

	// Lưu hoặc cập nhật đơn hàng
	DonHang save(DonHang donHang);

	// Xóa đơn hàng theo ID
	void deleteById(Integer id);

	// Hủy đơn hàng
	void cancelOrder(Integer orderId);

	// Cập nhật trạng thái đơn hàng
	DonHang updateStatus(Integer id, String newStatus);

	// Tổng tiền theo trạng thái
	double sumTongTienByStatus(String status);

	// Đếm đơn theo trạng thái
	long countByStatus(String status);

	// Thống kê số lượng đơn theo trạng thái
	Map<String, Long> getOrderStatusStats();

	// Tổng doanh thu theo ngày và trạng thái
	double sumTongTienByDateAndStatus(LocalDateTime date, String status);

	// Đếm đơn theo thời gian và trạng thái
	long countByDateAndStatus(LocalDateTime dateTime, String status);

	// Lấy danh sách đơn hàng mới nhất (giới hạn số lượng)
	List<DonHang> findRecentOrders(int limit);

	double sumRevenueByDateRangeAndStatus(LocalDateTime startDate, LocalDateTime endDate, String status);

	long countByDateRangeAndStatus(LocalDateTime from, LocalDateTime to, String status);

	double sumRevenueByMonth(int year, int month, String status);

	double sumRevenueByDate(LocalDateTime date, String status);

	Map<String, Long> getOrderStatusStatsInRange(LocalDateTime from, LocalDateTime to);

//	long countBooksSoldBetween(LocalDateTime start, LocalDateTime end);

	double sumTongTienBetween(LocalDateTime start, LocalDateTime end);

	long countOrdersBetween(LocalDateTime startDate, LocalDateTime endDate);

	long countBooksSoldBetween(LocalDateTime startDate, LocalDateTime endDate);

	double sumRevenueBetween(LocalDateTime startDate, LocalDateTime endDate);

}
