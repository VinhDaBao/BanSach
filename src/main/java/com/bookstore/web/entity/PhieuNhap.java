//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "PhieuNhap")
//public class PhieuNhap {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer MaPN;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNCC", nullable = false)
//    private NhaCungCap NhaCungCap;
//
//    @Column(name = "NgayNhap", nullable = false)
//    private LocalDateTime NgayNhap = LocalDateTime.now();
//
//    /** Một phiếu nhập có thể có nhiều chi tiết phiếu nhập */
//    @OneToMany(
//        mappedBy = "PhieuNhap",
//        cascade = CascadeType.ALL,
//        orphanRemoval = true,
//        fetch = FetchType.EAGER // ✅ Load luôn chi tiết để tránh lỗi Lazy khi xem detail
//    )
//    private List<ChiTietPN> ChiTietPNList = new ArrayList<>();
//
//    @Transient
//    private BigDecimal TongTien = BigDecimal.ZERO;
//
//    /** =================== GETTERS & SETTERS =================== */
//    public Integer getMaPN() {
//        return MaPN;
//    }
//
//    public void setMaPN(Integer MaPN) {
//        this.MaPN = MaPN;
//    }
//
//    public NhaCungCap getNhaCungCap() {
//        return NhaCungCap;
//    }
//
//    public void setNhaCungCap(NhaCungCap NhaCungCap) {
//        this.NhaCungCap = NhaCungCap;
//    }
//
//    public LocalDateTime getNgayNhap() {
//        return NgayNhap;
//    }
//
//    public void setNgayNhap(LocalDateTime NgayNhap) {
//        this.NgayNhap = NgayNhap;
//    }
//
//    public List<ChiTietPN> getChiTietPNList() {
//        return ChiTietPNList;
//    }
//
//    public void setChiTietPNList(List<ChiTietPN> ChiTietPNList) {
//        this.ChiTietPNList.clear();
//        if (ChiTietPNList != null) {
//            this.ChiTietPNList.addAll(ChiTietPNList);
//            for (ChiTietPN ct : this.ChiTietPNList) {
//                ct.setPhieuNhap(this);
//            }
//        }
//    }
//
//    public BigDecimal getTongTien() {
//        if (ChiTietPNList != null && !ChiTietPNList.isEmpty()) {
//            TongTien = ChiTietPNList.stream()
//                    .map(ct -> ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//        }
//        return TongTien;
//    }
//
//    public void setTongTien(BigDecimal TongTien) {
//        this.TongTien = TongTien;
//    }
//
//    /** =================== TIỆN ÍCH =================== */
//    public void addChiTietPN(ChiTietPN ct) {
//        ct.setPhieuNhap(this);
//        this.ChiTietPNList.add(ct);
//    }
//
//    public void removeChiTietPN(ChiTietPN ct) {
//        ct.setPhieuNhap(null);
//        this.ChiTietPNList.remove(ct);
//    }
//}


//package com.bookstore.web.entity;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//@Data
//@Entity
//@Table(name = "PhieuNhap")
//public class PhieuNhap {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaPN")
//    private Integer maPN;
//
//    /** ================== NHÀ CUNG CẤP ================== */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MaNCC", nullable = false)
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private NhaCungCap nhaCungCap;
//
//    /** ================== NGÀY NHẬP ================== */
//    @Column(name = "NgayNhap", nullable = false)
//    private LocalDateTime ngayNhap = LocalDateTime.now();
//
//    /** ================== DANH SÁCH CHI TIẾT PHIẾU NHẬP ================== */
//    @OneToMany(
//        mappedBy = "phieuNhap",
//        cascade = CascadeType.ALL,
//        orphanRemoval = true,
//        fetch = FetchType.EAGER // Load sẵn chi tiết để tránh lỗi lazy khi xem chi tiết
//    )
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    private List<ChiTietPN> chiTietPNList = new ArrayList<>();
//
//    /** ================== TỔNG TIỀN (KHÔNG LƯU DB) ================== */
//    @Transient
//    private BigDecimal tongTien;
//
//    /** ================== TIỆN ÍCH ================== */
//
//    @Transient
//    public BigDecimal getTongTien() {
//        if (chiTietPNList == null || chiTietPNList.isEmpty()) {
//            return BigDecimal.ZERO;
//        }
//        return chiTietPNList.stream()
//                .map(ct -> ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    /** Thêm chi tiết phiếu nhập */
//    public void addChiTietPN(ChiTietPN ct) {
//        ct.setPhieuNhap(this);
//        this.chiTietPNList.add(ct);
//    }
//
//    /** Xóa chi tiết phiếu nhập */
//    public void removeChiTietPN(ChiTietPN ct) {
//        ct.setPhieuNhap(null);
//        this.chiTietPNList.remove(ct);
//    }
//}


package com.bookstore.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "phieunhap")
public class PhieuNhap {

    /** Mã phiếu nhập */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPN")
    private Integer maPN;

    /** ================== NHÀ CUNG CẤP ================== */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNCC", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private NhaCungCap nhaCungCap;

    /** ================== NGÀY NHẬP ================== */
    @Column(name = "NgayNhap", nullable = false)
    private LocalDateTime ngayNhap = LocalDateTime.now();

    /** ================== DANH SÁCH CHI TIẾT PHIẾU NHẬP ================== */
    @OneToMany(
        mappedBy = "phieuNhap",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER // ✅ Load sẵn chi tiết để hiển thị nhanh
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<ChiTietPN> chiTietPNList = new ArrayList<>();

    /** ================== TỔNG TIỀN (TẠM TÍNH, KHÔNG LƯU DB) ================== */
    @Transient
    private BigDecimal tongTien;

    /** ================== HÀM TIỆN ÍCH ================== */

    /** ✅ Tính tổng tiền của phiếu nhập */
    @Transient
    public BigDecimal getTongTien() {
        if (chiTietPNList == null || chiTietPNList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return chiTietPNList.stream()
                .map(ct -> {
                    if (ct.getGiaNhap() != null && ct.getSoLuong() != null)
                        return ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong()));
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** ✅ Thêm chi tiết vào phiếu nhập */
    public void addChiTietPN(ChiTietPN ct) {
        if (ct == null) return;
        ct.setPhieuNhap(this);
        this.chiTietPNList.add(ct);
    }

    /** ✅ Xóa chi tiết khỏi phiếu nhập */
    public void removeChiTietPN(ChiTietPN ct) {
        if (ct == null) return;
        ct.setPhieuNhap(null);
        this.chiTietPNList.remove(ct);
    }

    /** ✅ Cập nhật tồn kho sách khi lưu phiếu nhập (thực tế xử lý ở service) */
    @Transient
    public void capNhatTonKhoSauNhap() {
        if (chiTietPNList == null) return;
        for (ChiTietPN ct : chiTietPNList) {
            if (ct.getSach() != null && ct.getSoLuong() != null) {
                int tonMoi = ct.getSach().getSoLuongTon() + ct.getSoLuong();
                ct.getSach().setSoLuongTon(tonMoi);
                ct.getSach().setTrangThai(true); // bật lại kinh doanh nếu trước đó ngừng
            }
        }
    }
}

