package com.bookstore.web.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CartItemDto {
    private Integer id;
    private String title;
    private String author;
    private Integer quantity;
    private BigDecimal price;           // Giá sau khi giảm (hoặc giá gốc nếu không giảm)
    private BigDecimal originalPrice;   // Giá gốc (để hiển thị gạch ngang)
    private Boolean hasDiscount = false; // Có giảm giá không
    private String imageUrl;
    private int stock;
}