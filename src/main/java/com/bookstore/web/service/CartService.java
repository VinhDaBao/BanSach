package com.bookstore.web.service;

import com.bookstore.web.dto.CartItemDto;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    List<CartItemDto> getCartItems(Integer userId);
    BigDecimal calculateSubtotal(List<CartItemDto> cartItems);
    BigDecimal calculateSubtotalForSelectedItems(Integer userId, List<Integer> selectedItemIds);
    void addToCart(Integer userId, Integer bookId, Integer quantity) throws Exception;
    int countCartItems(Integer userId);
    void removeFromCart(Integer userId, Integer maSach);
    void updateQuantity(Integer userId, Integer maSach, Integer newQuantity);
}