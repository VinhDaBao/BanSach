package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "GioHang_Sach")
@Data
@IdClass(GioHangSachId.class)  // Nếu dùng composite key
public class GioHang_Sach {
    @Id
    @Column(name = "MaGH")
    private Integer maGH;

    @Id
    @Column(name = "MaSach")
    private Integer maSach;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "MaGH", insertable = false, updatable = false)
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "MaSach", insertable = false, updatable = false)
    private Sach sach;
}