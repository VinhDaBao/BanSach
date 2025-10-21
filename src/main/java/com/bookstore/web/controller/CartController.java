package com.bookstore.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.dto.CartItemDto;
import com.bookstore.web.entity.DiaChi;
import com.bookstore.web.entity.KhuyenMai;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.service.CartService;
import com.bookstore.web.service.DiaChiService;
import com.bookstore.web.service.SachService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private SachService sachService;
    
    @Autowired 
    private DiaChiService diaChiService;

    @GetMapping
    public String showCart(Model model, HttpSession session, 
                          @RequestParam(value = "selectedItems", required = false) String selectedItemsParam) {
        Integer userId = (Integer) session.getAttribute("userId");
        List<CartItemDto> cartItems;
        BigDecimal subtotal;

        System.out.println("GET /cart - userId: " + userId + ", selectedItemsParam: " + selectedItemsParam);

        List<Integer> selectedItems = new ArrayList<>();
        if (selectedItemsParam != null && !selectedItemsParam.isEmpty()) {
            selectedItems = Arrays.stream(selectedItemsParam.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }

        if (userId == null) {
            cartItems = new ArrayList<>();
            subtotal = BigDecimal.ZERO;
            model.addAttribute("error", "Vui lòng đăng nhập để xem giỏ hàng");
        } else {
            cartItems = cartService.getCartItems(userId);
            
            // ⭐ ÁP DỤNG GIÁ GIẢM CHO CÁC ITEM TRONG GIỎ HÀNG
            applyDiscountsToCartItems(cartItems);
            
            System.out.println("Cart items retrieved: " + cartItems.size() + ", items: " + cartItems);
            subtotal = !selectedItems.isEmpty() 
                ? cartService.calculateSubtotalForSelectedItems(userId, selectedItems)
                : BigDecimal.ZERO;
            System.out.println("Subtotal calculated: " + subtotal);
            
            List<DiaChi> addresses = diaChiService.getByUserId(userId);
            DiaChi defaultAddress = diaChiService.getDefaultAddress(userId);
            
            model.addAttribute("addresses", addresses);
            model.addAttribute("defaultAddress", defaultAddress);
        }

        BigDecimal shipping = BigDecimal.valueOf(30000);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.add(shipping).subtract(discount);
        System.out.println("GrandTotal calculated: " + grandTotal);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("subtotal", subtotal.toString());
        model.addAttribute("shipping", shipping.toString());
        model.addAttribute("discount", discount.toString());
        model.addAttribute("grandTotal", grandTotal.toString());
        model.addAttribute("pageTitle", "Giỏ hàng");

        System.out.println("Model attributes: subtotal=" + subtotal + ", grandTotal=" + grandTotal + ", selectedItems=" + selectedItems);

        return "user/cart";
    }
    private void applyDiscountsToCartItems(List<CartItemDto> cartItems) {
        cartItems.forEach(item -> {
            Sach sach = sachService.findById(item.getId()).orElse(null);
            if (sach != null) {
                KhuyenMai km = sachService.getBestKhuyenMai(sach.getId());
                if (km != null && isActivePromotion(km)) {
                    BigDecimal originalPrice = sach.getGiaBan();
                    double gt = km.getGiaTri();
                    BigDecimal discountedPrice;
                    
                    if (gt < 1.0) { 
                        // Giảm theo %
                        discountedPrice = originalPrice.multiply(BigDecimal.valueOf(1 - gt));
                    } else {  
                        // Giảm theo số tiền cố định
                        discountedPrice = originalPrice.subtract(BigDecimal.valueOf(gt));
                    }
                    
                    // Cập nhật giá cho item trong giỏ hàng
                    item.setPrice(discountedPrice);
                    item.setOriginalPrice(originalPrice); // Lưu giá gốc để hiển thị
                    item.setHasDiscount(true);
                    
                    System.out.println("Applied discount to item " + item.getId() + 
                                     ": " + originalPrice + " → " + discountedPrice);
                } else {
                    item.setHasDiscount(false);
                }
            }
        });
    }
    
    /**
     * Kiểm tra khuyến mãi còn hiệu lực không
     */
    private boolean isActivePromotion(KhuyenMai km) {
        java.time.LocalDate today = java.time.LocalDate.now();  
        return (today.isAfter(km.getNgayBD()) || today.isEqual(km.getNgayBD())) 
               && (today.isBefore(km.getNgayKT()) || today.isEqual(km.getNgayKT()));
    }
    
    

    @PostMapping("/update-selection")
    public String updateSelection(@RequestParam(value = "selectedItems", required = false) List<Integer> selectedItems, HttpSession session) {
        System.out.println("POST /cart/update-selection - selectedItems: " + selectedItems); // Debug log
        if (selectedItems == null) {
            selectedItems = new ArrayList<>();
        }
        String selectedItemsParam = selectedItems.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return "redirect:/cart?selectedItems=" + selectedItemsParam;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("maSach") Integer maSach,
                            @RequestParam("soLuong") Integer soLuong,
                            @RequestParam("returnUrl") String returnUrl,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        System.out.println("Received addToCart: maSach=" + maSach + ", soLuong=" + soLuong + ", returnUrl=" + returnUrl);
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm vào giỏ hàng");
            return "redirect:/login";
        }
        try {
            cartService.addToCart(userId, maSach, soLuong);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + returnUrl;
    }

    @GetMapping("/add")
    public String addToCartGet(@RequestParam("maSach") Integer maSach,
                               @RequestParam("soLuong") Integer soLuong,
                               @RequestParam("returnUrl") String returnUrl,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        System.out.println("Received addToCart (GET): maSach=" + maSach + ", soLuong=" + soLuong + ", returnUrl=" + returnUrl);
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm vào giỏ hàng");
            return "redirect:/login";
        }
        try {
            cartService.addToCart(userId, maSach, soLuong);
            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + returnUrl;
    }
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("id") Integer maSach, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xóa sản phẩm");
            return "redirect:/login";
        }
        try {
            cartService.removeFromCart(userId, maSach);
            redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }
    @PostMapping("/update-quantity")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(
            @RequestParam("maSach") Integer maSach,
            @RequestParam("quantity") Integer quantity,
            HttpSession session) {
        
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vui lòng đăng nhập");
        }
        
        try {
            cartService.updateQuantity(userId, maSach, quantity);
            return ResponseEntity.ok("Cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}