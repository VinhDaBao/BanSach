package com.bookstore.web.service.impl;

import com.bookstore.web.dto.CartItemDto;
import com.bookstore.web.entity.GioHang;
import com.bookstore.web.entity.GioHang_Sach;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.entity.Sach;
import com.bookstore.web.repository.GioHangRepository;
import com.bookstore.web.repository.GioHangSachRepository;
import com.bookstore.web.repository.SachRepository;
import com.bookstore.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangSachRepository gioHangSachRepository;

    @Autowired
    private SachRepository sachRepository;

    @Override
    public List<CartItemDto> getCartItems(Integer userId) {
        List<CartItemDto> cartItems = new ArrayList<>();
        if (userId == null) {
            System.out.println("userId is null, returning empty cart");
            return cartItems;
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId);
            return cartItems;
        }

        List<GioHang_Sach> gioHangSachList = gioHangSachRepository.findByMaGH(gioHang.getMaGH());
        System.out.println("Found " + gioHangSachList.size() + " GioHangSach for MaGH=" + gioHang.getMaGH());
        for (GioHang_Sach gioHangSach : gioHangSachList) {
            Optional<Sach> optionalSach = sachRepository.findById(gioHangSach.getSach().getId());
            if (optionalSach.isPresent()) {
                Sach sach = optionalSach.get();
                CartItemDto item = new CartItemDto();
                item.setId(gioHangSach.getSach().getId());
                item.setTitle(sach.getTenSP());
                item.setAuthor(sach.getTacGia());
                item.setQuantity(1);
                item.setPrice(sach.getGiaBan());
                item.setImageUrl(sach.getAnh()); 
                item.setStock(sach.getSoLuongTon());

                cartItems.add(item);

                
                System.out.println("CartItem: maSach=" + item.getId() + ", title=" + item.getTitle() + ", price=" + item.getPrice() + ", quantity=" + item.getQuantity());
            } else {
                System.out.println("Sach not found for maSach=" + gioHangSach.getSach().getId());
            }
        }
        return cartItems;
    }

    @Override
    public BigDecimal calculateSubtotal(List<CartItemDto> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Cart items is null or empty, returning ZERO");
            return BigDecimal.ZERO;
        }
        BigDecimal subtotal = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Calculated subtotal for all items: " + subtotal);
        return subtotal;
    }

    @Override
    public BigDecimal calculateSubtotalForSelectedItems(Integer userId, List<Integer> selectedItemIds) {
        System.out.println("Calculating subtotal for userId=" + userId + ", selectedItemIds=" + selectedItemIds);
        if (userId == null || selectedItemIds == null || selectedItemIds.isEmpty()) {
            System.out.println("Returning ZERO: userId or selectedItemIds is null/empty");
            return BigDecimal.ZERO;
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId);
            return BigDecimal.ZERO;
        }

        List<GioHang_Sach> gioHangSachList = gioHangSachRepository.findByMaGH(gioHang.getMaGH());
        System.out.println("Found " + gioHangSachList.size() + " GioHangSach for MaGH=" + gioHang.getMaGH());
        if (gioHangSachList.isEmpty()) {
            System.out.println("No GioHangSach found for MaGH=" + gioHang.getMaGH());
            return BigDecimal.ZERO;
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (Integer maSach : selectedItemIds) {
            Optional<GioHang_Sach> gioHangSachOptional = gioHangSachList.stream()
                    .filter(gioHangSach -> gioHangSach.getSach().getId().equals(maSach))
                    .findFirst();
            if (gioHangSachOptional.isPresent()) {
                GioHang_Sach gioHangSach = gioHangSachOptional.get();
                Optional<Sach> optionalSach = sachRepository.findById(maSach);
                if (optionalSach.isPresent()) {
                    Sach sach = optionalSach.get();
                    BigDecimal itemTotal = sach.getGiaBan().multiply(BigDecimal.valueOf(gioHangSach.getSoLuong()));
                    subtotal = subtotal.add(itemTotal);
                    System.out.println("Adding to subtotal: maSach=" + maSach + ", price=" + sach.getGiaBan() + ", quantity=" + gioHangSach.getSoLuong() + ", itemTotal=" + itemTotal);
                } else {
                    System.out.println("Sach not found for maSach=" + maSach);
                }
            } else {
                System.out.println("GioHangSach not found for maSach=" + maSach);
            }
        }
        System.out.println("Final subtotal: " + subtotal);
        return subtotal;
    }

    @Override
    public void addToCart(Integer userId, Integer bookId, Integer quantity) throws Exception {
        System.out.println("Adding to cart: userId=" + userId + ", bookId=" + bookId + ", quantity=" + quantity);
        if (userId == null) {
            throw new Exception("Vui lòng đăng nhập để thêm vào giỏ hàng");
        }
        if (quantity <= 0) {
            throw new Exception("Số lượng phải lớn hơn 0");
        }

        Optional<Sach> sachOptional = sachRepository.findById(bookId);
        if (sachOptional.isEmpty()) {
            throw new Exception("Sách không tồn tại");
        }
        Sach sach = sachOptional.get();
        if (sach.getSoLuongTon() < quantity) {
            throw new Exception("Số lượng tồn kho không đủ");
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId + ", creating new one");
            gioHang = createNewGioHang(userId);
            if (gioHang.getMaGH() == null) {
                throw new Exception("Không thể tạo mới GioHang, MaGH is null");
            }
        }

        Optional<GioHang_Sach> gioHangSachOptional = gioHangSachRepository.findByGioHangMaGHAndSachId(gioHang.getMaGH(), bookId);
        GioHang_Sach gioHangSach;
        if (gioHangSachOptional.isEmpty()) {
            gioHangSach = new GioHang_Sach();
            gioHangSach.setMaGH(gioHang.getMaGH());
            gioHangSach.setMaSach(bookId);
            gioHangSach.setSoLuong(quantity);
            gioHangSach.setGioHang(gioHang);
            gioHangSach.setSach(sach);
            System.out.println("Created new GioHangSach: MaSach=" + bookId + ", quantity=" + quantity);
        } else {
            gioHangSach = gioHangSachOptional.get();
            int newQuantity = gioHangSach.getSoLuong() + quantity;
            if (newQuantity > sach.getSoLuongTon()) {
                throw new Exception("Số lượng tồn kho không đủ");
            }
            gioHangSach.setSoLuong(newQuantity);
            System.out.println("Updated GioHangSach: MaSach=" + bookId + ", newQuantity=" + newQuantity);
        }

        gioHangSachRepository.saveAndFlush(gioHangSach);
    }

    @Override
    public int countCartItems(Integer userId) {
        System.out.println("Counting cart items for userId=" + userId);
        if (userId == null) {
            System.out.println("userId is null, returning 0");
            return 0;
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId);
            return 0;
        }

        long count = gioHangSachRepository.countByGioHangMaGH(gioHang.getMaGH());
        System.out.println("Cart item count for userId=" + userId + ": " + count);
        return (int) count;
    }

    @Override
    public void removeFromCart(Integer userId, Integer maSach) {
        System.out.println("Removing from cart: userId=" + userId + ", maSach=" + maSach);
        if (userId == null || maSach == null) {
            System.out.println("userId or maSach is null, skipping removal");
            return;
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId);
            return;
        }

        Optional<GioHang_Sach> gioHangSachOptional = gioHangSachRepository.findByGioHangMaGHAndSachId(gioHang.getMaGH(), maSach);
        if (gioHangSachOptional.isPresent()) {
            gioHangSachRepository.delete(gioHangSachOptional.get());
            System.out.println("Removed GioHangSach: maSach=" + maSach + " from MaGH=" + gioHang.getMaGH());
        } else {
            System.out.println("No GioHangSach found for maSach=" + maSach + " in MaGH=" + gioHang.getMaGH());
        }
    }

    @Override
    public void updateQuantity(Integer userId, Integer maSach, Integer newQuantity) {
        System.out.println("Updating quantity: userId=" + userId + ", maSach=" + maSach + ", newQuantity=" + newQuantity);
        if (userId == null || maSach == null || newQuantity == null) {
            System.out.println("userId, maSach, or newQuantity is null, skipping update");
            return;
        }

        if (newQuantity <= 0) {
            System.out.println("New quantity must be greater than 0, removing item instead");
            removeFromCart(userId, maSach);
            return;
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_Id(userId);
        if (gioHang == null) {
            System.out.println("No GioHang found for userId=" + userId);
            return;
        }

        Optional<GioHang_Sach> gioHangSachOptional = gioHangSachRepository.findByGioHangMaGHAndSachId(gioHang.getMaGH(), maSach);
        if (gioHangSachOptional.isPresent()) {
            GioHang_Sach gioHangSach = gioHangSachOptional.get();
            Optional<Sach> sachOptional = sachRepository.findById(maSach);
            if (sachOptional.isPresent()) {
                Sach sach = sachOptional.get();
                if (newQuantity <= sach.getSoLuongTon()) {
                    gioHangSach.setSoLuong(newQuantity);
                    gioHangSachRepository.save(gioHangSach);
                    System.out.println("Updated quantity: maSach=" + maSach + ", newQuantity=" + newQuantity);
                } else {
                    System.out.println("New quantity exceeds stock, keeping current quantity: " + gioHangSach.getSoLuong());
                }
            } else {
                System.out.println("Sach not found for maSach=" + maSach);
            }
        } else {
            System.out.println("No GioHangSach found for maSach=" + maSach + " in MaGH=" + gioHang.getMaGH());
        }
    }
    private GioHang createNewGioHang(Integer userId) {
        GioHang gioHang = new GioHang();
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setId(userId);
        gioHang.setNguoiDung(nguoiDung);
        gioHang = gioHangRepository.saveAndFlush(gioHang);
        System.out.println("Created new GioHang for userId=" + userId + " with MaGH=" + gioHang.getMaGH());
        return gioHang;
    }
}