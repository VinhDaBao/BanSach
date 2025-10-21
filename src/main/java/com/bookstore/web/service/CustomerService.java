//package com.bookstore.web.service;
//
//import com.bookstore.web.entity.NguoiDung;
//import com.bookstore.web.entity.DonHang;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface CustomerService {
//
//    List<NguoiDung> getCustomersWithOrders();
//
//    List<DonHang> getOrdersByCustomerId(Integer maTK);
//    
//    Page<Map<String, Object>> searchAndSortCustomersPaged(String keyword, String sort, Pageable pageable);
//}


package com.bookstore.web.service;

import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    /**
     * ✅ Lấy danh sách tất cả khách hàng (vai trò USER),
     * kèm theo thông tin cơ bản: họ tên, tài khoản, sđt, địa chỉ, số đơn, tổng chi tiêu.
     */
    List<NguoiDung> getCustomersWithOrders();

    /**
     * ✅ Lấy danh sách đơn hàng theo mã tài khoản (ID khách hàng),
     * chỉ gồm thông tin cơ bản: mã đơn, ngày đặt, tổng tiền, trạng thái.
     */
    List<DonHang> getOrdersByCustomerId(Integer maTK);

    /**
     * ✅ Tìm kiếm và sắp xếp danh sách khách hàng có phân trang.
     * Có thể tìm theo từ khóa (họ tên, tài khoản, SĐT) và sắp xếp theo:
     *  - chiTieuDesc / chiTieuAsc: tổng chi tiêu
     *  - soDonDesc / soDonAsc: số đơn hàng
     */
    Page<Map<String, Object>> searchAndSortCustomersPaged(String keyword, String sort, Pageable pageable);
}
