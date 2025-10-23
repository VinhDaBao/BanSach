package com.bookstore.web.service.impl;

import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.repository.ChiTietDonHangRepository;
import com.bookstore.web.repository.SachKhuyenMaiRepository;
import com.bookstore.web.repository.SachRepository;
import com.bookstore.web.service.SachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SachServiceImpl implements SachService {

	// ==================== REPOSITORY INJECTION ====================
	private final SachRepository sachRepository;

	@Autowired
	private ChiTietDonHangRepository chiTietDonHangRepository;

	@Autowired
	private SachKhuyenMaiRepository sachKhuyenMaiRepository;

	// Constructor injection
	public SachServiceImpl(SachRepository sachRepository) {
		this.sachRepository = sachRepository;
	}

	// ==================== CRUD CƠ BẢN ====================
	@Override
	public List<Sach> findAll() {
		return sachRepository.findAll();
	}

	@Override
	public Page<Sach> findAll(Pageable pageable) {
		return sachRepository.findAllDistinct(pageable);
	}

	@Override
	public Optional<Sach> findById(Integer maSach) {
		return sachRepository.findById(maSach);
	}

	@Override
	public Sach save(Sach sach) {
		if (sach.getNgayCoHang() == null)
			sach.setNgayCoHang(LocalDateTime.now());
		if (sach.getGiaBan() == null)
			sach.setGiaBan(BigDecimal.ZERO);
		int soLuongTon = sach.getSoLuongTon(); 
	    if (soLuongTon <= 0) {
	        sach.setSoLuongTon(0);
	        sach.setTrangThai(false);
	    } else {
	        sach.setTrangThai(true);
	    }
		if (sach.getAnh() == null || sach.getAnh().isBlank())
			sach.setAnh("default.png");
		return sachRepository.save(sach);
	}

	@Override
	public void deleteById(Integer maSach) {
		sachRepository.deleteById(maSach);
	}

	// ==================== TÌM KIẾM & LỌC ====================
	@Override
	public Sach findByTenSP(String tenSP) {
		return sachRepository.findAll().stream().filter(s -> s.getTenSP().equalsIgnoreCase(tenSP)).findFirst()
				.orElse(null);
	}

	@Override
	public Sach saveIfNotExists(String tenSP) {
		Sach existing = findByTenSP(tenSP);
		if (existing != null)
			return existing;

		Sach newSach = new Sach();
		newSach.setTenSP(tenSP);
		newSach.setGiaBan(BigDecimal.ZERO);
		newSach.setSoLuongTon(0);
		newSach.setNgayCoHang(LocalDateTime.now());
		newSach.setAnh("default.png");
		return sachRepository.save(newSach);
	}

//    @Override
//    public Page<Sach> searchByKeyword(String keyword, Pageable pageable) {
//        return sachRepository.searchByKeyword(keyword, pageable);
//    }
//
//    @Override
//    public Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable) {
//        return sachRepository.findByTheLoaiId(categoryId, pageable);
//    }

	@Override
	public Optional<Sach> findSachById(Integer id) {
		return sachRepository.findSachById(id);
	}

	// ==================== QUẢN LÝ SỐ LƯỢNG ====================
//	@Override
//	public void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap) {
//		sachRepository.findById(maSach).ifPresent(sach -> {
//			sach.setSoLuongTon(sach.getSoLuongTon() + soLuongNhap);
//			sach.setNgayCoHang(LocalDateTime.now());
//			sachRepository.save(sach);
//		});
//	}
	
	@Override
	@Transactional
	public void updateSoLuongTonSauNhap(Integer maSach, int soLuongNhap) {
	    sachRepository.findById(maSach).ifPresent(sach -> {
	        Integer soLuongHienTai = sach.getSoLuongTon(); // dùng Integer
	        int newSoLuong = (soLuongHienTai != null ? soLuongHienTai : 0) + soLuongNhap;
	        
	        sach.setSoLuongTon(newSoLuong);
	        sach.setNgayCoHang(LocalDateTime.now());
	        
	        // trạng thái tự động: >0 = đang kinh doanh, 0 = ngừng kinh doanh
	        sach.setTrangThai(newSoLuong > 0);
	        
	        sachRepository.save(sach);
	    });
	}



	// ==================== KHUYẾN MÃI ====================
	@Override
	public KhuyenMai getBestKhuyenMai(int maSach) {
		Optional<Sach> sachOpt = sachRepository.findById(maSach);
		if (sachOpt.isEmpty())
			return null;
		Sach sach = sachOpt.get();
		double giaBan = sach.getGiaBan().doubleValue();

		// Giảm % lớn nhất
		List<KhuyenMai> percentKMs = sachKhuyenMaiRepository.findMaxPercentDiscount(maSach);
		double maxPercent = percentKMs.isEmpty() ? 0 : percentKMs.get(0).getGiaTri();

		// Giảm tiền lớn nhất
		List<KhuyenMai> moneyKMs = sachKhuyenMaiRepository.findMaxMoneyDiscount(maSach);
		double maxMoney = moneyKMs.isEmpty() ? 0 : moneyKMs.get(0).getGiaTri();

		// So sánh hai loại giảm
		double percentAmount = giaBan * maxPercent / 100;
		double moneyAmount = maxMoney;

		if (percentAmount > moneyAmount && maxPercent > 0) {
			return percentKMs.get(0);
		} else if (maxMoney > 0) {
			return moneyKMs.get(0);
		} else {
			return null;
		}
	}

	// ==================== NGƯỜI DÙNG / YÊU THÍCH ====================
	@Override
	public Page<Sach> getFavoritesByUserId(Integer userId, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return sachRepository.findFavoritesByUserId(userId, pageable);
	}

	// ==================== THỐNG KÊ ====================


	@Override
	public Page<Sach> getAllSach(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return sachRepository.findAllDistinct(pageable);
	}

	@Override
	public Page<Sach> findByTheLoaiId(Integer categoryId, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return sachRepository.findByTheLoaiId(categoryId, pageable);
	}

	@Override
	public Page<Sach> searchByKeyword(String keyword, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return sachRepository.searchByKeyword(keyword, pageable);
	}

	@Override
	public Page<Sach> findByTheLoaiId(Integer categoryId, Pageable pageable) {
		return sachRepository.findByTheLoaiId(categoryId, pageable);
	}

	@Override
	public Page<Sach> searchByKeyword(String keyword, Pageable pageable) {
		return sachRepository.searchByKeyword(keyword, pageable);
	}

	@Override
	public long countAll() {
		return sachRepository.count();
	}

	@Override
	public Map<String, Object> getTopSellingBookInRange(LocalDateTime from, LocalDateTime to, String status) {
		List<Object[]> result = sachRepository.getTopSellingBookInRange(from, to, status);
		if (result.isEmpty())
			return null;

		Object[] row = result.get(0);
		Map<String, Object> map = new HashMap<>();
		map.put("tenSP", row[0]);
		map.put("soLuong", row[1]);
		return map;
	}



	@Override
	public List<Map<String, Object>> getTopSellingBooksInRange(int limit, LocalDateTime from, LocalDateTime to,
			String status) {
		List<Object[]> raw = sachRepository.getTopSellingBooksInRange(from, to, status);
		List<Map<String, Object>> result = new ArrayList<>();
		int count = 0;
		for (Object[] row : raw) {
			if (count++ >= limit)
				break;
			Map<String, Object> map = new HashMap<>();
			map.put("tenSach", row[0]);
			map.put("tacGia", row[1]);
			map.put("soLuongBan", row[2]);
			result.add(map);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getTopSellingBooks(int limit) {
		LocalDateTime from = LocalDateTime.of(1970, 1, 1, 0, 0);
		LocalDateTime to = LocalDateTime.now();
		String status = "Đã xác nhận";
		return getTopSellingBooksInRange(limit, from, to, status);
	}

	@Override
	public List<Map<String, Object>> getRevenueByCategoryInRange(LocalDateTime from, LocalDateTime to, String status) {
		List<Object[]> raw = sachRepository.getRevenueByCategoryInRange(from, to, status);
		List<Map<String, Object>> result = new ArrayList<>();

		for (Object[] row : raw) {
			Map<String, Object> map = new HashMap<>();
			map.put("tenTL", row[0]); // đổi từ tenDanhMuc -> tenTheLoai
			map.put("tongDoanhThu", row[1]);
			result.add(map);
		}

		return result;
	}

	@Override
	public List<Map<String, Object>> getDetailedStockReport() {
	    List<Object[]> list = sachRepository.getDetailedStockReport();
	    List<Map<String, Object>> data = new ArrayList<>();

	    for (Object[] row : list) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", row[0]);
	        map.put("tenSP", row[1]);
	        map.put("tacGia", row[2]);
	        map.put("theLoai", row[3]);
	        map.put("nhaXuatBan", row[4]);
	        map.put("gia", row[5]);
	        map.put("soLuongNhap", row[6]);
	        map.put("soLuongBan", row[7]);
	        map.put("conLai", row[8]);
	        map.put("ngayNhap", row[9]);
	        map.put("trangThai", row[10]);
	        data.add(map);
	    }

	    return data;
	}

	@Override
	public List<Map<String, Object>> getStockReport() {
		List<Object[]> list = sachRepository.getStockReport();
		List<Map<String, Object>> data = new ArrayList<>();
		for (Object[] row : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("tenSP", row[0]);
			map.put("soLuongTon", row[1]);
			map.put("giaBan", row[2]);
			data.add(map);
		}
		return data;
	}



	@Override
	public List<Map<String, Object>> findTopSellingBooks() {
		return chiTietDonHangRepository.findTopSellingBooks().stream().limit(3) // chỉ lấy top 3 quyển
				.collect(Collectors.toList());
	}

}
