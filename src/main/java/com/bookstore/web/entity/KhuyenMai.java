package com.bookstore.web.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKM")
    private Integer maKM;

    @Column(name = "TenKM", nullable = false, length = 100)
    private String tenKM;
    
    @Column(name = "NgayBD", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBD;

    @Column(name = "NgayKT", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayKT;

    @Column(name = "GiaTri", nullable = false)
    private Double giaTri;

	public Integer getMaKM() {
		return maKM;
	}
	public void setMaKM(Integer maKM) {
		this.maKM = maKM;
	}
	public String getTenKM() {
		return tenKM;
	}
	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}
	public LocalDate getNgayBD() {
		return ngayBD;
	}
	public void setNgayBD(LocalDate ngayBD) {
		this.ngayBD = ngayBD;
	}
	public LocalDate getNgayKT() {
		return ngayKT;
	}
	public void setNgayKT(LocalDate ngayKT) {
		this.ngayKT = ngayKT;
	}
	public Double getGiaTri() {
		return giaTri;
	}
	public void setGiaTri(Double giaTri) {
		this.giaTri = giaTri;
	}

	public KhuyenMai(Integer maKM, String tenKM, LocalDate ngayBD, LocalDate ngayKT, Double giaTri
			) {
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.ngayBD = ngayBD;
		this.ngayKT = ngayKT;
		this.giaTri = giaTri;
	}
   @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private Set<Sach_KhuyenMai> sachKhuyenMaiSet;
   public Set<Sach_KhuyenMai> getSachKhuyenMaiSet() {
       return sachKhuyenMaiSet;
   }

   public void setSachKhuyenMaiSet(Set<Sach_KhuyenMai> sachKhuyenMaiSet) {
       this.sachKhuyenMaiSet = sachKhuyenMaiSet;

   }
}

   