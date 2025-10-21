//package com.bookstore.web.entity;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//import jakarta.persistence.*;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(name = "NguoiDung")
//public class NguoiDung {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaTK")
//    private Integer id;
//
//    @Column(name = "HoTen", nullable = false, length = 100)
//    private String hoTen;
//
//    @Column(name = "TaiKhoan", nullable = false, unique = true, length = 50)
//    private String taiKhoan;
//
//    @Column(name = "MatKhau", nullable = false, length = 50)
//    private String matKhau;
//
//    @Column(name = "GioiTinh", length = 10)
//    private String gioiTinh;
//
//    @Column(name = "NgaySinh", nullable = false)
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy/MM/dd")
//    private LocalDate ngaySinh;
//
//    @Column(name = "DiaChi", length = 200)
//    private String diaChi;
//
//    @Column(name = "Avatar", length = 200)
//    private String avatar;
//
//    @Column(name = "Sdt", length = 20)
//    private String sdt;
//
//    @Column(name = "VaiTro", nullable = false, length = 50)
//    private String vaiTro;
//
//    @CreationTimestamp
//    @Column(name = "CreatedAt", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "UpdatedAt")
//    private LocalDateTime updatedAt;
//    
//    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<DonHang> donHangs;
//}


package com.bookstore.web.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "NguoiDung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTK")
    private Integer id;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "TaiKhoan", nullable = false, unique = true, length = 50)
    private String taiKhoan;

    @Column(name = "MatKhau", nullable = false, length = 50)
    private String matKhau;

    @Column(name = "GioiTinh", length = 10)
    private String gioiTinh;

    @Column(name = "NgaySinh", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate ngaySinh;

    @Column(name = "DiaChi", length = 200)
    private String diaChi;

    @Column(name = "Avatar", length = 200)
    private String avatar;

    @Column(name = "Sdt", length = 20)
    private String sdt;

    @Column(name = "VaiTro", nullable = false, length = 50)
    private String vaiTro;

//    @CreationTimestamp
//    @Column(name = "CreatedAt", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "UpdatedAt")
//    private LocalDateTime updatedAt;
    
    @CreationTimestamp
    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<DonHang> donHangs;
}