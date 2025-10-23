//package com.bookstore.web.repository;
//
//import com.bookstore.web.entity.DonHang;
//
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDateTime;
//
//import java.util.List;
//
//public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
//   
//    List<DonHang> findByNguoiDung_IdOrderByNgayDatDesc(Integer userId);
//	
//	@Query("SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d WHERE d.trangThai = :status")
//    double sumTongTienByStatus(@Param("status") String status);
//
//    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.trangThai = :status")
//    long countByStatus(@Param("status") String status);
//
//    @Query("SELECT d.trangThai, COUNT(d) FROM DonHang d GROUP BY d.trangThai")
//    List<Object[]> getOrderStatusStats();
//
//    @Query(value = "SELECT COALESCE(SUM(tongTien), 0) FROM DonHang WHERE DATE(ngayDat) = :date AND trangThai = :status", nativeQuery = true)
//    double sumTongTienByDateAndStatus(@Param("date") LocalDateTime date, @Param("status") String status);
//
//    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayDat > :dateTime AND d.trangThai = :status")
//    long countByDateAndStatus(@Param("dateTime") LocalDateTime dateTime, @Param("status") String status);
//
//    Page<DonHang> findAllByOrderByNgayDatDesc(Pageable pageable);
//    
//    
// // Thêm cho quản lý: Tìm đơn hàng theo trạng thái
//    Page<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai, Pageable pageable);
//
//    // Tìm đơn hàng theo user
//    Page<DonHang> findByNguoiDung_IdOrderByNgayDatDesc(Integer userId, Pageable pageable);
//}

package com.bookstore.web.repository;

import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

	// Lấy danh sách đơn hàng theo người dùng, mới nhất trước
	List<DonHang> findByNguoiDung_IdOrderByNgayDatDesc(Integer userId);

	// Tổng tiền theo trạng thái
	@Query("SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d WHERE d.trangThai = :status")
	double sumTongTienByStatus(@Param("status") String status);

	// Đếm số đơn theo trạng thái
	@Query("SELECT COUNT(d) FROM DonHang d WHERE d.trangThai = :status")
	long countByStatus(@Param("status") String status);

	// Lấy thống kê số lượng đơn theo trạng thái
	@Query("SELECT d.trangThai, COUNT(d) FROM DonHang d GROUP BY d.trangThai")
	List<Object[]> getOrderStatusStats();

	// Tổng doanh thu theo ngày và trạng thái
	@Query(value = "SELECT COALESCE(SUM(tongTien), 0) FROM DonHang WHERE DATE(ngayDat) = :date AND trangThai = :status", nativeQuery = true)
	double sumTongTienByDateAndStatus(@Param("date") LocalDateTime date, @Param("status") String status);

	// Đếm đơn theo ngày đặt và trạng thái
	@Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayDat > :dateTime AND d.trangThai = :status")
	long countByDateAndStatus(@Param("dateTime") LocalDateTime dateTime, @Param("status") String status);

	// Phân trang: tất cả đơn hàng (mới nhất trước)
	Page<DonHang> findAllByOrderByNgayDatDesc(Pageable pageable);

	// Phân trang: theo trạng thái
	Page<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai, Pageable pageable);

	// Phân trang: theo người dùng
	Page<DonHang> findByNguoiDung_IdOrderByNgayDatDesc(Integer userId, Pageable pageable);

	// Lấy danh sách khách hàng đã từng mua hàng
	@Query("SELECT DISTINCT d.nguoiDung FROM DonHang d")
	List<NguoiDung> findAllCustomersWithOrders();

	// Lấy thống kê khách hàng: số đơn & tổng chi tiêu
	@Query("""
			    SELECT
			        d.nguoiDung.id AS maTK,
			        d.nguoiDung.hoTen AS hoTen,
			        d.nguoiDung.taiKhoan AS taiKhoan,
			        d.nguoiDung.sdt AS sdt,
			        d.nguoiDung.diaChi AS diaChi,
			        COUNT(d.id) AS soDon,
			        COALESCE(SUM(d.tongTien), 0) AS tongChiTieu
			    FROM DonHang d
			    GROUP BY
			        d.nguoiDung.id, d.nguoiDung.hoTen,
			        d.nguoiDung.taiKhoan, d.nguoiDung.sdt, d.nguoiDung.diaChi
			""")
	List<Object[]> findCustomerOrderStats();

	boolean existsByNguoiDung_IdAndChiTietDonHangs_Sach_IdAndTrangThai(Integer userId, Integer bookId,
			String trangThai);

    @Query("SELECT COUNT(dh) > 0 FROM DonHang dh " +
            "JOIN dh.chiTietDonHangs ct " +
            "WHERE dh.nguoiDung.id = :userId " +
            "AND ct.sach.id = :sachId " +
            "AND dh.trangThai = :trangThai")
     boolean checkUserPurchasedBook(
         @Param("userId") Integer userId, 
         @Param("sachId") Integer sachId, 
         @Param("trangThai") String trangThai
     );

	// Đếm số đơn hàng trong khoảng ngày theo trạng thái
	@Query("SELECT COUNT(d) FROM DonHang d WHERE d.trangThai = :status AND d.ngayDat BETWEEN :from AND :to")
	long countByDateRangeAndStatus(LocalDateTime from, LocalDateTime to, String status);

	// Doanh thu theo tháng
	@Query("SELECT COALESCE(SUM(d.tongTien), 0) FROM DonHang d WHERE YEAR(d.ngayDat) = :year AND MONTH(d.ngayDat) = :month AND d.trangThai = :status")
	double sumRevenueByMonth(int year, int month, String status);

	// Doanh thu theo ngày
//	@Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.trangThai = :status AND DATE(d.ngayTao) = DATE(:date)")
//	Double sumRevenueByDate(@Param("date") LocalDateTime date, @Param("status") String status);

	// ✅ Tổng doanh thu theo ngày (sử dụng khoảng thời gian để tránh lỗi kiểu dữ
	// liệu)
	@Query("""
			    SELECT SUM(d.tongTien)
			    FROM DonHang d
			    WHERE d.trangThai = :status
			      AND d.ngayDat BETWEEN :startOfDay AND :endOfDay
			""")
	Double sumRevenueByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay,
			@Param("status") String status);

	// Thống kê đơn hàng theo trạng thái trong khoảng thời gian
	@Query("SELECT d.trangThai, COUNT(d) FROM DonHang d WHERE d.ngayDat BETWEEN :from AND :to GROUP BY d.trangThai")
	List<Object[]> getOrderStatusStatsInRange(LocalDateTime from, LocalDateTime to);

	// Tổng doanh thu trong khoảng ngày theo trạng thái
	@Query("SELECT COALESCE(SUM(d.tongTien), 0) " + "FROM DonHang d " + "WHERE d.trangThai = :status "
			+ "AND d.ngayDat BETWEEN :startDate AND :endDate")
	double sumRevenueByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate, @Param("status") String status);

//	// Đếm tổng số sách bán ra trong khoảng thời gian
//	@Query("SELECT COALESCE(SUM(ct.soLuong), 0) FROM ChiTietDonHang ct " + "JOIN ct.donHang dh "
//			+ "WHERE dh.ngayDat BETWEEN :start AND :end AND dh.trangThai = 'Hoàn thành'")
//	long countBooksSoldBetween(LocalDateTime start, LocalDateTime end);

	// Tính tổng doanh thu trong khoảng thời gian
	@Query("SELECT COALESCE(SUM(dh.tongTien), 0) FROM DonHang dh "
			+ "WHERE dh.ngayDat BETWEEN :start AND :end AND dh.trangThai = 'Hoàn thành'")
	double sumTongTienBetween(LocalDateTime start, LocalDateTime end);

//	// Đếm đơn hàng theo trạng thái và ngày
//	@Query("SELECT COUNT(dh) FROM DonHang dh WHERE dh.ngayDat >= :from AND dh.trangThai = :status")
//	long countByDateAndStatus(LocalDateTime from, String status);

	// Lấy các đơn hàng gần nhất
	@Query("SELECT dh FROM DonHang dh ORDER BY dh.ngayDat DESC")
	List<DonHang> findRecentOrders(Pageable pageable);

	Page<DonHang> findByTrangThai(String trangThai, Pageable pageable);

	// ✅ Đếm số đơn hàng trong khoảng thời gian
	@Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayDat BETWEEN :startDate AND :endDate")
	long countOrdersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	// ✅ Tổng số sách đã bán trong khoảng thời gian
	@Query("SELECT COALESCE(SUM(ct.soLuong), 0) " + "FROM ChiTietDonHang ct " + "JOIN ct.donHang d "
			+ "WHERE d.ngayDat BETWEEN :startDate AND :endDate AND d.trangThai = 'Đã xác nhận'")
	long countBooksSoldBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	// ✅ Tổng doanh thu trong khoảng thời gian
	@Query("SELECT COALESCE(SUM(d.tongTien), 0) " + "FROM DonHang d "
			+ "WHERE d.ngayDat BETWEEN :startDate AND :endDate AND d.trangThai = 'Đã xác nhận'")
	double sumRevenueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
