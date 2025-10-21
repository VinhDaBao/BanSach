package com.bookstore.web.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DiaChi")
public class DiaChi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDC")
    private Integer id;
    
    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;
    
    @Column(name = "Sdt", nullable = false, length = 20)
    private String sdt;
    
    @Column(name = "DiaChiChiTiet", nullable = false, length = 200)
    private String diaChiChiTiet;
    
    @Column(name = "PhuongXa", length = 100)
    private String phuongXa;
    
    @Column(name = "QuanHuyen", length = 100)
    private String quanHuyen;
    
    @Column(name = "TinhThanh", length = 100)
    private String tinhThanh;
    
    @Column(name = "MacDinh")
    private Boolean macDinh = false;
    
    @ManyToOne
    @JoinColumn(name = "MaTK", nullable = false)
    private NguoiDung nguoiDung;
}
