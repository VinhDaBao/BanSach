//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//import java.math.BigDecimal;
//
//@Data
//@Entity
//@Table(name = "ChiTietDonHang")
//public class ChiTietDonHang {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaCT")
//    private Integer id;
//
//    @Column(name = "SoLuong", nullable = false)
//    private int soLuong;
//
//    @Column(name = "Gia", nullable = false, precision = 18, scale = 2)
//    private BigDecimal gia;
//
//
//    @ManyToOne
//    @JoinColumn(name = "MaDH", nullable = false)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private DonHang donHang;
//
//
//    @ManyToOne
//    @JoinColumn(name = "MaSach", nullable = false)
//    private Sach sach;
//}


package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ChiTietDonHang")
public class ChiTietDonHang { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCT")
    private Integer id;

    @Column(name = "SoLuong", nullable = false)
    private int soLuong;

    @Column(name = "Gia", nullable = false, precision = 18, scale = 2)
    private BigDecimal gia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDH", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSach", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Sach sach;
}