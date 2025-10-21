//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import org.hibernate.annotations.NotFound;
//import org.hibernate.annotations.NotFoundAction;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "ChiTietPN")
//public class ChiTietPN implements Serializable {
//
//    @EmbeddedId
//    private ChiTietPNKey id = new ChiTietPNKey();
//
//    /** ================== LIÊN KẾT VỚI PHIẾU NHẬP ================== */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("MaPN") // ánh xạ với trường MaPN trong khóa chính
//    @JoinColumn(name = "MaPN", nullable = false)
//    private PhieuNhap PhieuNhap;
//
//    /** ================== LIÊN KẾT VỚI SÁCH ================== */
//    @ManyToOne(fetch = FetchType.EAGER) // ✅ Load luôn thông tin sách
//    @MapsId("MaSach")
//    @JoinColumn(name = "MaSach", nullable = false)
//    @NotFound(action = NotFoundAction.IGNORE) // ✅ Bỏ qua lỗi nếu sách bị xóa
//    private Sach Sach;
//
//    /** ================== THUỘC TÍNH ================== */
//    @Column(name = "SoLuong", nullable = false)
//    private Integer SoLuong;
//
//    @Column(name = "GiaNhap", nullable = false, precision = 10, scale = 2)
//    private BigDecimal GiaNhap;
//
//    /** ================== GETTERS / SETTERS ================== */
//    public ChiTietPNKey getId() {
//        return id;
//    }
//
//    public void setId(ChiTietPNKey id) {
//        this.id = id;
//    }
//
//    public PhieuNhap getPhieuNhap() {
//        return PhieuNhap;
//    }
//
//    public void setPhieuNhap(PhieuNhap PhieuNhap) {
//        this.PhieuNhap = PhieuNhap;
//    }
//
//    public Sach getSach() {
//        return Sach;
//    }
//
//    public void setSach(Sach Sach) {
//        this.Sach = Sach;
//    }
//
//    public Integer getSoLuong() {
//        return SoLuong;
//    }
//
//    public void setSoLuong(Integer SoLuong) {
//        this.SoLuong = SoLuong;
//    }
//
//    public BigDecimal getGiaNhap() {
//        return GiaNhap;
//    }
//
//    public void setGiaNhap(BigDecimal GiaNhap) {
//        this.GiaNhap = GiaNhap;
//    }
//
//    /** ================== TIỆN ÍCH ================== */
//    @Transient
//    public BigDecimal getThanhTien() {
//        if (GiaNhap == null || SoLuong == null) return BigDecimal.ZERO;
//        return GiaNhap.multiply(BigDecimal.valueOf(SoLuong));
//    }
//
//    @Override
//    public String toString() {
//        return "ChiTietPN{" +
//                "MaPN=" + (id != null ? id.getMaPN() : null) +
//                ", MaSach=" + (id != null ? id.getMaSach() : null) +
//                ", SoLuong=" + SoLuong +
//                ", GiaNhap=" + GiaNhap +
//                '}';
//    }
//}


//package com.bookstore.web.entity;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//@Data
//@Entity
//@Table(name = "ChiTietPN")
//public class ChiTietPN implements Serializable {
//
//    @EmbeddedId
//    private ChiTietPNKey id = new ChiTietPNKey();
//
//    /** ================== LIÊN KẾT VỚI PHIẾU NHẬP ================== */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("maPN")
//    @JoinColumn(name = "MaPN", nullable = false)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private PhieuNhap phieuNhap;
//
//    /** ================== LIÊN KẾT VỚI SÁCH ================== */
//    @ManyToOne(fetch = FetchType.EAGER)
//    @MapsId("maSach")
//    @JoinColumn(name = "MaSach", nullable = false)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private Sach sach;
//
//    /** ================== THUỘC TÍNH ================== */
//    @Column(name = "SoLuong", nullable = false)
//    private Integer soLuong;
//
//    @Column(name = "GiaNhap", nullable = false, precision = 10, scale = 2)
//    private BigDecimal giaNhap;
//
//    /** ================== TIỆN ÍCH ================== */
//    @Transient
//    public BigDecimal getThanhTien() {
//        if (giaNhap == null || soLuong == null) return BigDecimal.ZERO;
//        return giaNhap.multiply(BigDecimal.valueOf(soLuong));
//    }
//
//    @Override
//    public String toString() {
//        return "ChiTietPN{" +
//                "MaPN=" + (id != null ? id.getMaPN() : null) +
//                ", MaSach=" + (id != null ? id.getMaSach() : null) +
//                ", SoLuong=" + soLuong +
//                ", GiaNhap=" + giaNhap +
//                '}';
//    }
//}





package com.bookstore.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "ChiTietPN")
public class ChiTietPN implements Serializable {

    @EmbeddedId
    private ChiTietPNKey id = new ChiTietPNKey();

    /** ================== LIÊN KẾT VỚI PHIẾU NHẬP ================== */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("maPN")
    @JoinColumn(name = "MaPN", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PhieuNhap phieuNhap;

    /** ================== LIÊN KẾT VỚI SÁCH ================== */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("maSach")
    @JoinColumn(name = "MaSach", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Sach sach;

    /** ================== THUỘC TÍNH ================== */
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "GiaNhap", nullable = false, precision = 10, scale = 2)
    private BigDecimal giaNhap;

    /** ================== TIỆN ÍCH ================== */
    @Transient
    public BigDecimal getThanhTien() {
        if (giaNhap == null || soLuong == null) return BigDecimal.ZERO;
        return giaNhap.multiply(BigDecimal.valueOf(soLuong));
    }

    /** ================== HỖ TRỢ THIẾT LẬP MỐI QUAN HỆ ================== */
    public void setPhieuNhapWithUpdate(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
        if (!phieuNhap.getChiTietPNList().contains(this)) {
            phieuNhap.getChiTietPNList().add(this);
        }
    }

    @Override
    public String toString() {
        return "ChiTietPN{" +
                "MaPN=" + (id != null ? id.getMaPN() : null) +
                ", MaSach=" + (id != null ? id.getMaSach() : null) +
                ", SoLuong=" + soLuong +
                ", GiaNhap=" + giaNhap +
                '}';
    }
}
