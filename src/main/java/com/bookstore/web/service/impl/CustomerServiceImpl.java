//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.DonHang;
//import com.bookstore.web.entity.NguoiDung;
//import com.bookstore.web.repository.DonHangRepository;
//import com.bookstore.web.repository.NguoiDungRepository;
//import com.bookstore.web.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomerServiceImpl implements CustomerService {
//
//    private final DonHangRepository donHangRepository;
//    private final NguoiDungRepository nguoiDungRepository;
//
//    @Autowired
//    public CustomerServiceImpl(DonHangRepository donHangRepository, NguoiDungRepository nguoiDungRepository) {
//        this.donHangRepository = donHangRepository;
//        this.nguoiDungRepository = nguoiDungRepository;
//    }
//
//    @Override
//    public List<NguoiDung> getCustomersWithOrders() {
//        return donHangRepository.findAllCustomersWithOrders();
//    }
//
//    @Override
//    public List<DonHang> getOrdersByCustomerId(Integer maTK) {
//        return donHangRepository.findByMaTK(maTK);
//    }
//
//    @Override
//    public Page<Map<String, Object>> searchAndSortCustomersPaged(String keyword, String sort, Pageable pageable) {
//        // ✅ Lấy tất cả khách hàng đã từng mua hàng
//        List<NguoiDung> customers = donHangRepository.findAllCustomersWithOrders();
//
//        // ✅ Tính tổng chi tiêu & số đơn hàng cho từng khách
//        List<Map<String, Object>> data = customers.stream()
//                .map(c -> {
//                    List<DonHang> orders = donHangRepository.findByMaTK(c.getMaTK());
//                    double totalSpent = orders.stream().mapToDouble(DonHang::getTongTien).sum();
//                    int orderCount = orders.size();
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("maTK", c.getMaTK());
//                    map.put("hoTen", c.getHoTen());
//                    map.put("taiKhoan", c.getTaiKhoan());
//                    map.put("sdt", c.getSdt());
//                    map.put("diaChi", c.getDiaChi());
//                    map.put("tongChiTieu", totalSpent);
//                    map.put("soDon", orderCount); // ✅ đổi lại key để khớp Thymeleaf
//                    return map;
//                })
//                .filter(m -> keyword == null || keyword.isEmpty()
//                        || m.get("hoTen").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("taiKhoan").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("sdt").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("diaChi").toString().toLowerCase().contains(keyword.toLowerCase()))
//                .collect(Collectors.toList());
//
//        // ✅ Sắp xếp linh hoạt
//        Comparator<Map<String, Object>> comparator;
//        switch (sort) {
//            case "chiTieuAsc" -> comparator = Comparator.comparingDouble(c -> (Double) c.get("tongChiTieu"));
//            case "chiTieuDesc" -> comparator = Comparator.comparingDouble((Map<String, Object> c) -> (Double) c.get("tongChiTieu")).reversed();
//            case "soDonAsc" -> comparator = Comparator.comparingInt(c -> (Integer) c.get("soDon"));
//            case "soDonDesc" -> comparator = Comparator.comparingInt((Map<String, Object> c) -> (Integer) c.get("soDon")).reversed();
//            default -> comparator = Comparator.comparing(c -> c.get("hoTen").toString());
//        }
//        data.sort(comparator);
//
//        // ✅ Phân trang
//        int start = (int) pageable.getOffset();
//        int end = Math.min(start + pageable.getPageSize(), data.size());
//        List<Map<String, Object>> pagedData = data.subList(start, end);
//
//        return new PageImpl<>(pagedData, pageable, data.size());
//    }
//}

//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.DonHang;
//import com.bookstore.web.entity.NguoiDung;
//import com.bookstore.web.repository.DonHangRepository;
//import com.bookstore.web.repository.NguoiDungRepository;
//import com.bookstore.web.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomerServiceImpl implements CustomerService {
//
//    private final DonHangRepository donHangRepository;
//    private final NguoiDungRepository nguoiDungRepository;
//
//    @Autowired
//    public CustomerServiceImpl(DonHangRepository donHangRepository, NguoiDungRepository nguoiDungRepository) {
//        this.donHangRepository = donHangRepository;
//        this.nguoiDungRepository = nguoiDungRepository;
//    }
//
//    /**
//     * ✅ Lấy danh sách khách hàng đã từng đặt hàng
//     */
//    @Override
//    public List<NguoiDung> getCustomersWithOrders() {
//        return donHangRepository.findAllCustomersWithOrders();
//    }
//
//    /**
//     * ✅ Lấy danh sách đơn hàng theo mã tài khoản (userId)
//     */
//    @Override
//    public List<DonHang> getOrdersByCustomerId(Integer maTK) {
//        return donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(maTK);
//    }
//
//    /**
//     * ✅ Tìm kiếm + sắp xếp + phân trang danh sách khách hàng đã mua hàng
//     */
//    @Override
//    public Page<Map<String, Object>> searchAndSortCustomersPaged(String keyword, String sort, Pageable pageable) {
//        // 🔹 Lấy tất cả khách hàng đã có đơn hàng
//        List<NguoiDung> customers = donHangRepository.findAllCustomersWithOrders();
//
//        // 🔹 Chuẩn bị dữ liệu thống kê
//        List<Map<String, Object>> data = customers.stream()
//                .map(c -> {
//                    Integer id = c.getId(); // ⚠️ Sửa lại nếu tên khác (vd: getMaNguoiDung())
//                    List<DonHang> orders = donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(id);
//
//                    double totalSpent = orders.stream()
//                            .mapToDouble(d -> d.getTongTien().doubleValue()) // ✅ chuyển BigDecimal -> double
//                            .sum();
//                    int orderCount = orders.size();
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("maTK", id);
//                    map.put("hoTen", c.getHoTen());
//                    map.put("taiKhoan", c.getTaiKhoan());
//                    map.put("sdt", c.getSdt());
//                    map.put("diaChi", c.getDiaChi());
//                    map.put("tongChiTieu", totalSpent);
//                    map.put("soDon", orderCount);
//                    return map;
//                })
//                .filter(m -> keyword == null || keyword.isEmpty()
//                        || m.get("hoTen").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("taiKhoan").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("sdt").toString().toLowerCase().contains(keyword.toLowerCase())
//                        || m.get("diaChi").toString().toLowerCase().contains(keyword.toLowerCase()))
//                .collect(Collectors.toList());
//
//        // 🔹 Sắp xếp theo yêu cầu
//        Comparator<Map<String, Object>> comparator;
//        switch (sort) {
//            case "chiTieuAsc" ->
//                    comparator = Comparator.comparingDouble(c -> (Double) c.get("tongChiTieu"));
//            case "chiTieuDesc" ->
//                    comparator = Comparator.comparingDouble((Map<String, Object> c) -> (Double) c.get("tongChiTieu")).reversed();
//            case "soDonAsc" ->
//                    comparator = Comparator.comparingInt(c -> (Integer) c.get("soDon"));
//            case "soDonDesc" ->
//                    comparator = Comparator.comparingInt((Map<String, Object> c) -> (Integer) c.get("soDon")).reversed();
//            default ->
//                    comparator = Comparator.comparing(c -> c.get("hoTen").toString(), String.CASE_INSENSITIVE_ORDER);
//        }
//        data.sort(comparator);
//
//        // 🔹 Phân trang
//        int start = (int) pageable.getOffset();
//        int end = Math.min(start + pageable.getPageSize(), data.size());
//        List<Map<String, Object>> pagedData = data.subList(start, end);
//
//        return new PageImpl<>(pagedData, pageable, data.size());
//    }
//}


package com.bookstore.web.service.impl;

import com.bookstore.web.entity.DonHang;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.repository.DonHangRepository;
import com.bookstore.web.repository.NguoiDungRepository;
import com.bookstore.web.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final DonHangRepository donHangRepository;
    private final NguoiDungRepository nguoiDungRepository;

    @Autowired
    public CustomerServiceImpl(DonHangRepository donHangRepository, NguoiDungRepository nguoiDungRepository) {
        this.donHangRepository = donHangRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    /**
     * ✅ Lấy danh sách khách hàng đã từng đặt hàng
     */
    @Override
    public List<NguoiDung> getCustomersWithOrders() {
        return donHangRepository.findAllCustomersWithOrders();
    }

    /**
     * ✅ Lấy danh sách đơn hàng theo mã tài khoản (userId)
     */
    @Override
    public List<DonHang> getOrdersByCustomerId(Integer maTK) {
        return donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(maTK);
    }

    /**
     * ✅ Tìm kiếm + sắp xếp + phân trang danh sách khách hàng đã mua hàng
     */
    @Override
    public Page<Map<String, Object>> searchAndSortCustomersPaged(String keyword, String sort, Pageable pageable) {
        // 1️⃣ Lấy tất cả khách USER
        List<NguoiDung> allCustomers = nguoiDungRepository.findAllCustomers();

        // 2️⃣ Chuẩn bị dữ liệu thống kê
        List<Map<String, Object>> data = allCustomers.stream()
                .map(c -> {
                    Integer id = c.getId();
                    List<DonHang> orders = donHangRepository.findByNguoiDung_IdOrderByNgayDatDesc(id);

                    // Tổng chi tiêu
                    double totalSpent = orders.stream()
                            .mapToDouble(d -> d.getTongTien() != null ? d.getTongTien().doubleValue() : 0)
                            .sum();

                    int orderCount = orders != null ? orders.size() : 0;

                    Map<String, Object> map = new HashMap<>();
                    map.put("maTK", id);
                    map.put("hoTen", c.getHoTen() != null ? c.getHoTen() : "");
                    map.put("taiKhoan", c.getTaiKhoan() != null ? c.getTaiKhoan() : "");
                    map.put("sdt", c.getSdt() != null ? c.getSdt() : "");
                    map.put("diaChi", c.getDiaChi() != null ? c.getDiaChi() : "");
                    map.put("tongChiTieu", totalSpent); // luôn >= 0
                    map.put("soDon", orderCount);        // luôn >= 0
                    return map;
                })
                // 3️⃣ Filter an toàn
                .filter(m -> {
                    if (keyword == null || keyword.isEmpty()) return true;
                    String kw = keyword.toLowerCase();
                    return Optional.ofNullable(m.get("hoTen")).map(Object::toString).orElse("").toLowerCase().contains(kw)
                            || Optional.ofNullable(m.get("taiKhoan")).map(Object::toString).orElse("").toLowerCase().contains(kw)
                            || Optional.ofNullable(m.get("sdt")).map(Object::toString).orElse("").toLowerCase().contains(kw)
                            || Optional.ofNullable(m.get("diaChi")).map(Object::toString).orElse("").toLowerCase().contains(kw);
                })
                .collect(Collectors.toList());

        // 4️⃣ Sắp xếp
        Comparator<Map<String, Object>> comparator;
        switch (sort) {
            case "chiTieuAsc" -> comparator = Comparator.comparingDouble(c -> (Double) c.get("tongChiTieu"));
            case "chiTieuDesc" -> comparator = Comparator.comparingDouble((Map<String, Object> c) -> (Double) c.get("tongChiTieu")).reversed();
            case "soDonAsc" -> comparator = Comparator.comparingInt(c -> (Integer) c.get("soDon"));
            case "soDonDesc" -> comparator = Comparator.comparingInt((Map<String, Object> c) -> (Integer) c.get("soDon")).reversed();
            default -> comparator = Comparator.comparing(c -> c.get("hoTen").toString(), String.CASE_INSENSITIVE_ORDER);
        }
        data.sort(comparator);

        // 5️⃣ Phân trang
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<Map<String, Object>> pagedData = start <= end ? data.subList(start, end) : new ArrayList<>();

        return new PageImpl<>(pagedData, pageable, data.size());
    }

}

