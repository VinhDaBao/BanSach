//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "DonHang")
//public class DonHang {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaDH")
//    private Integer id;
//
//    @Column(name = "NgayDat")
//    private LocalDateTime ngayDat;
//
//    @Column(name = "TrangThai", nullable = false, length = 50)
//    private String trangThai;
//
//    @Column(name = "TongTien", precision = 18, scale = 2)
//    private BigDecimal tongTien;
//
//    @Column(name = "DiaChi", nullable = false, length = 200)
//    private String diaChi;
//
//    @Column(name = "Sdt", nullable = false, length = 20)
//    private String sdt;
//
//    @ManyToOne
//    @JoinColumn(name = "MaTK", nullable = false)
//    private NguoiDung nguoiDung;
//
//    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<ChiTietDonHang> chiTietDonHangs;
//}


package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DonHang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH")
    private Integer id;

    @Column(name = "NgayDat", nullable = false)
    private LocalDateTime ngayDat;

    @Column(name = "TrangThai", nullable = false, length = 50)
    private String trangThai;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "DiaChi", nullable = false, length = 200)
    private String diaChi;

    @Column(name = "Sdt", nullable = false, length = 20)
    private String sdt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK", nullable = false)
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ChiTietDonHang> chiTietDonHangs;
}
