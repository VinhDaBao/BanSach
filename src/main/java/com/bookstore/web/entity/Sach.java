//package com.bookstore.web.entity;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import jakarta.persistence.Transient;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//@Data
//@Entity
//@Table(name = "Sach")
//public class Sach {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaSach")
//    private Integer id;
//
//    @Column(name = "TenSP", nullable = false, length = 200)
//    private String tenSP;
//
//    @Column(name = "TacGia", nullable = false, length = 100)
//    private String tacGia;
//
//    @Column(name = "GiaBan", nullable = false, precision = 18, scale = 2)
//    private BigDecimal giaBan;
//
//    @Column(name = "SoLuongTon", nullable = false)
//    private int soLuongTon;
//
//    @Column(name = "MoTa", length = 500)
//    private String moTa;
//
//    @Column(name = "Anh", nullable = false, length = 200)
//    private String anh;
//
//    @Column(name = "NgayCoHang", nullable = false)
//    private LocalDateTime ngayCoHang;
//
//    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaHT", nullable = false)
//    private HinhThuc hinhThuc;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNXB", nullable = false)
//    private NhaXB nhaXB;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNCC", nullable = false)
//    private NhaCungCap nhaCungCap;
//    
//    @ManyToMany
//    @JoinTable(
//        name = "Sach_TheLoai",
//        joinColumns = @JoinColumn(name = "MaSach"),
//        inverseJoinColumns = @JoinColumn(name = "MaTL")
//    )
//    @EqualsAndHashCode.Exclude 
//    @ToString.Exclude
//    private Set<TheLoai> danhSachTheLoai;
//
//    @Transient
//    private BigDecimal giaGiam; 
//
//    @Transient
//    private String badgeDiscount;
//    
//    @Transient
//    private Double averageRating = 0.0;
//
//    @Transient
//    private Integer totalReviews = 0;
//    
//    @Transient
//    private Boolean isFavorite = false;
//    
//    @OneToMany(mappedBy = "sach", fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<YeuThich> danhSachYeuThich;  
//}
//

//package com.bookstore.web.entity;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToMany;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import jakarta.persistence.Transient;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//@Data
//@Entity
//@Table(name = "Sach")
//public class Sach {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaSach")
//    private Integer id;
//
//    @Column(name = "TenSP", nullable = false, length = 200)
//    private String tenSP;
//
//    @Column(name = "TacGia", nullable = false, length = 100)
//    private String tacGia;
//
//    @Column(name = "GiaBan", nullable = false, precision = 18, scale = 2)
//    private BigDecimal giaBan;
//
//    @Column(name = "SoLuongTon", nullable = false)
//    private int soLuongTon;
//
//    @Column(name = "MoTa", length = 500)
//    private String moTa;
//
//    @Column(name = "Anh", nullable = false, length = 200)
//    private String anh;
//
//    @Column(name = "NgayCoHang", nullable = false)
//    private LocalDateTime ngayCoHang;
//
//    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaHT", nullable = false)
//    private HinhThuc hinhThuc;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNXB", nullable = false)
//    private NhaXB nhaXB;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNCC", nullable = false)
//    private NhaCungCap nhaCungCap;
//    
//    @Column(name = "SoTrang")
//    private Integer soTrang;
//
//    @Column(name = "NamXB")
//    private Integer namXB;
//    
//    @Column(name = "TrangThai", nullable = false)
//    private Boolean trangThai = true;
//    
//    @ManyToMany
//    @JoinTable(
//        name = "Sach_TheLoai",
//        joinColumns = @JoinColumn(name = "MaSach"),
//        inverseJoinColumns = @JoinColumn(name = "MaTL")
//    )
//    @EqualsAndHashCode.Exclude 
//    @ToString.Exclude
//    private Set<TheLoai> danhSachTheLoai;
//
//    @Transient
//    private BigDecimal giaGiam; 
//
//    @Transient
//    private String badgeDiscount;
//    
//    @Transient
//    private Double averageRating = 0.0;
//
//    @Transient
//    private Integer totalReviews = 0;
//    
//    @Transient
//    private Boolean isFavorite = false;
//    
//    @OneToMany(mappedBy = "sach", fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<YeuThich> danhSachYeuThich;  
//}




package com.bookstore.web.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "sach")
public class Sach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSach")
    private Integer id;

    @Column(name = "TenSP", nullable = false, length = 200)
    private String tenSP;

    @Column(name = "TacGia", nullable = false, length = 100)
    private String tacGia;

    @Column(name = "GiaBan", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "SoLuongTon", nullable = false)
    private int soLuongTon = 0; // ✅ khởi tạo mặc định, không nhập tay
    
    @Column(name = "MoTa", length = 500)
    private String moTa;

    @Column(name = "Anh", nullable = false, length = 200)
    private String anh;

    @Column(name = "NgayCoHang", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
    private LocalDateTime ngayCoHang;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHT", nullable = false)
    private HinhThuc hinhThuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNXB", nullable = false)
    private NhaXB nhaXB;

    @Column(name = "SoTrang")
    private Integer soTrang;

    @Column(name = "NamXB")
    private Integer namXB;
    
    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai = false; // ✅ Chưa kinh doanh cho đến khi có hàng
    
    @ManyToMany
    @JoinTable(
        name = "sach_theloai",
        joinColumns = @JoinColumn(name = "MaSach"),
        inverseJoinColumns = @JoinColumn(name = "MaTL")
    )
    @EqualsAndHashCode.Exclude 
    @ToString.Exclude
    private Set<TheLoai> danhSachTheLoai;

    @Transient
    private BigDecimal giaGiam; 

    @Transient
    private String badgeDiscount;
    
    @Transient
    private Double averageRating = 0.0;

    @Transient
    private Integer totalReviews = 0;
    
    @Transient
    private Boolean isFavorite = false;
    
    @OneToMany(mappedBy = "sach", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<YeuThich> danhSachYeuThich;  
}