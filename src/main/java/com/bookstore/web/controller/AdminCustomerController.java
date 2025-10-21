//package com.bookstore.web.controller;
//
//import com.bookstore.web.entity.DonHang;
//import com.bookstore.web.service.CustomerService;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/admin/users")
//public class AdminCustomerController {
//
//    private final CustomerService customerService;
//
//    public AdminCustomerController(CustomerService customerService) {
//        this.customerService = customerService;
//    }
//
//    /**
//     * ✅ Trang danh sách khách hàng — có tìm kiếm, sắp xếp, phân trang
//     */
//    @GetMapping
//    public String listCustomers(
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            Model model) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Map<String, Object>> customersPage = customerService.searchAndSortCustomersPaged(keyword, sort, pageable);
//
//        model.addAttribute("customersPage", customersPage);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);
//
//        return "admin/customer-list";
//    }
//
//    /**
//     * ✅ Xem lịch sử mua hàng (đơn hàng) của từng khách hàng — có phân trang
//     */
//    @GetMapping("/{id}/orders")
//    public String viewCustomerOrders(
//            @PathVariable("id") Integer id,
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            Model model) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayDat"));
//
//        // ✅ Lấy toàn bộ đơn hàng theo mã khách hàng
//        List<DonHang> allOrders = customerService.getOrdersByCustomerId(id);
//
//        // ✅ Phân trang thủ công (Pageable)
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), allOrders.size());
//        List<DonHang> pagedOrders = allOrders.subList(start, end);
//        Page<DonHang> ordersPage = new PageImpl<>(pagedOrders, pageable, allOrders.size());
//
//        // ✅ Gửi dữ liệu sang View
//        model.addAttribute("ordersPage", ordersPage);
//        model.addAttribute("customerId", id);
//        model.addAttribute("totalOrders", allOrders.size());
//        model.addAttribute("totalAmount", allOrders.stream().mapToDouble(DonHang::getTongTien).sum());
//
//        // ✅ Giữ trạng thái khi quay lại danh sách
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);
//        model.addAttribute("currentPage", page);
//
//        return "admin/customer-orders";
//    }
//}


//package com.bookstore.web.controller;
//
//import com.bookstore.web.entity.DonHang;
//import com.bookstore.web.service.CustomerService;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//@Controller
//@RequestMapping("/admin/users")
//public class AdminCustomerController {
//
//    private final CustomerService customerService;
//
//    public AdminCustomerController(CustomerService customerService) {
//        this.customerService = customerService;
//    }
//
//    /**
//     * ✅ Trang danh sách khách hàng — có tìm kiếm, sắp xếp, phân trang
//     */
//    @GetMapping
//    public String listCustomers(
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            Model model) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Map<String, Object>> customersPage =
//                customerService.searchAndSortCustomersPaged(keyword, sort, pageable);
//
//        model.addAttribute("customersPage", customersPage);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);
//
//        return "admin/customer-list";
//    }
//
//    /**
//     * ✅ Xem lịch sử mua hàng (đơn hàng) của từng khách hàng — có phân trang
//     */
//    @GetMapping("/{id}/orders")
//    public String viewCustomerOrders(
//            @PathVariable("id") Integer id,
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            Model model) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayDat"));
//
//        // ✅ Lấy toàn bộ đơn hàng theo mã khách hàng
//        List<DonHang> allOrders = customerService.getOrdersByCustomerId(id);
//
//        // ✅ Phân trang thủ công
//        int start = (int) pageable.getOffset();
//        int end = Math.min(start + pageable.getPageSize(), allOrders.size());
//        List<DonHang> pagedOrders = allOrders.subList(start, end);
//        Page<DonHang> ordersPage = new PageImpl<>(pagedOrders, pageable, allOrders.size());
//
//        // ✅ Tính tổng chi tiêu an toàn (BigDecimal)
//        BigDecimal totalAmount = allOrders.stream()
//                .map(DonHang::getTongTien)
//                .filter(Objects::nonNull)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // ✅ Gửi dữ liệu sang view
//        model.addAttribute("ordersPage", ordersPage);
//        model.addAttribute("customerId", id);
//        model.addAttribute("totalOrders", allOrders.size());
//        model.addAttribute("totalAmount", totalAmount);
//
//        // ✅ Giữ trạng thái khi quay lại danh sách
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);
//        model.addAttribute("currentPage", page);
//
//        return "admin/customer-orders";
//    }
//}

package com.bookstore.web.controller;

import com.bookstore.web.entity.DonHang;
import com.bookstore.web.service.CustomerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/users")
public class AdminCustomerController {

    private final CustomerService customerService;

    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * ✅ Trang danh sách khách hàng — có tìm kiếm, sắp xếp, phân trang
     */
    @GetMapping
    public String listCustomers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
            @RequestParam(value = "status", defaultValue = "all") String status, // <--- mới
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> customersPage =
                customerService.searchAndSortCustomersPaged(keyword, sort, pageable);

        model.addAttribute("customersPage", customersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("status", status); // <--- mới

        return "admin/customer-list";
    }

    /**
     * ✅ Xem lịch sử mua hàng (đơn hàng) của từng khách hàng — có phân trang
     */
    @GetMapping("/{id}/orders")
    public String viewCustomerOrders(
            @PathVariable("id") Integer id,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", defaultValue = "chiTieuDesc") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayDat"));

        // ✅ Lấy toàn bộ đơn hàng theo mã khách hàng
        List<DonHang> allOrders = customerService.getOrdersByCustomerId(id);

        // ✅ Phân trang thủ công
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allOrders.size());
        List<DonHang> pagedOrders = allOrders.subList(start, end);
        Page<DonHang> ordersPage = new PageImpl<>(pagedOrders, pageable, allOrders.size());

        // ✅ Tính tổng chi tiêu an toàn (BigDecimal)
        BigDecimal totalAmount = allOrders.stream()
                .map(DonHang::getTongTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ✅ Gửi dữ liệu sang view
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("customerId", id);
        model.addAttribute("totalOrders", allOrders.size());
        model.addAttribute("totalAmount", totalAmount);

        // ✅ Giữ trạng thái khi quay lại danh sách
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("currentPage", page);

        return "admin/customer-orders";
    }
}
