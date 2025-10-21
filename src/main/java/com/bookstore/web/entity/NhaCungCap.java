//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//
//@Entity
//@Table(name = "nha_cung_cap")
//@NoArgsConstructor
//@AllArgsConstructor
//public class NhaCungCap {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY) 
//	private Integer maNCC;
//
//	@Column(nullable = false)
//	private String tenNCC;
//
//	// ✅ Viết getter/setter thủ công để giữ đúng tên "maNCC", "tenNCC"
//	public Integer getMaNCC() {
//		return maNCC;
//	}
//
//	public void setMaNCC(Integer maNCC) {
//		this.maNCC = maNCC;
//	}
//
//	public String getTenNCC() {
//		return tenNCC;
//	}
//
//	public void setTenNCC(String tenNCC) {
//		this.tenNCC = tenNCC;
//	}
//}

//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import java.util.List;
//import lombok.NoArgsConstructor;
//import lombok.AllArgsConstructor;
//
//@Entity
//@Table(name = "NhaCungCap")
//@NoArgsConstructor
//@AllArgsConstructor
//public class NhaCungCap {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer MaNCC;
//
//    @Column(nullable = false, length = 100)
//    private String TenNCC;
//
//    // Một nhà cung cấp có nhiều phiếu nhập
//    @OneToMany(mappedBy = "NhaCungCap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PhieuNhap> PhieuNhapList;
//
//    public Integer getMaNCC() {
//        return MaNCC;
//    }
//
//    public void setMaNCC(Integer MaNCC) {
//        this.MaNCC = MaNCC;
//    }
//
//    public String getTenNCC() {
//        return TenNCC;
//    }
//
//    public void setTenNCC(String TenNCC) {
//        this.TenNCC = TenNCC;
//    }
//
//    public List<PhieuNhap> getPhieuNhapList() {
//        return PhieuNhapList;
//    }
//
//    public void setPhieuNhapList(List<PhieuNhap> PhieuNhapList) {
//        this.PhieuNhapList = PhieuNhapList;
//    }
//}

package com.bookstore.web.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "nhacungcap")
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "MaNCC", nullable = false)
	private Integer maNCC;

	@Column(name = "TenNCC",nullable = false)
	private String tenNCC;

	// ✅ Viết getter/setter thủ công để giữ đúng tên "maNCC", "tenNCC"
	public Integer getMaNCC() {
		return maNCC;
	}

	public void setMaNCC(Integer maNCC) {
		this.maNCC = maNCC;
	}

	public String getTenNCC() {
		return tenNCC;
	}

	public void setTenNCC(String tenNCC) {
		this.tenNCC = tenNCC;
	}
	
	 @OneToMany(
		        mappedBy = "nhaCungCap",
		        cascade = CascadeType.ALL,
		        orphanRemoval = true,
		        fetch = FetchType.LAZY
		    )
		    @ToString.Exclude
		    @EqualsAndHashCode.Exclude
		    private List<PhieuNhap> phieuNhapList = new ArrayList<>();

		    /** ================== TIỆN ÍCH ================== */

		    public void addPhieuNhap(PhieuNhap pn) {
		        pn.setNhaCungCap(this);
		        this.phieuNhapList.add(pn);
		    }

		    public void removePhieuNhap(PhieuNhap pn) {
		        pn.setNhaCungCap(null);
		        this.phieuNhapList.remove(pn);
		    }
		    
		    public List<PhieuNhap> getPhieuNhapList() {
		        return phieuNhapList;
		    }

		    public void setPhieuNhapList(List<PhieuNhap> phieuNhapList) {
		        this.phieuNhapList = phieuNhapList;
		    }
}

