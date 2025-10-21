//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//public class ChiTietPNKey implements Serializable {
//
//    @Column(name = "MaPN")
//    private Integer MaPN;
//
//    @Column(name = "MaSach")
//    private Integer MaSach;
//
//    public ChiTietPNKey() {}
//
//    public ChiTietPNKey(Integer MaPN, Integer MaSach) {
//        this.MaPN = MaPN;
//        this.MaSach = MaSach;
//    }
//
//    public Integer getMaPN() {
//        return MaPN;
//    }
//
//    public void setMaPN(Integer MaPN) {
//        this.MaPN = MaPN;
//    }
//
//    public Integer getMaSach() {
//        return MaSach;
//    }
//
//    public void setMaSach(Integer MaSach) {
//        this.MaSach = MaSach;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ChiTietPNKey)) return false;
//        ChiTietPNKey that = (ChiTietPNKey) o;
//        return Objects.equals(MaPN, that.MaPN) &&
//               Objects.equals(MaSach, that.MaSach);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(MaPN, MaSach);
//    }
//}

package com.bookstore.web.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ChiTietPNKey implements Serializable {

    @Column(name = "MaPN")
    private Integer maPN;

    @Column(name = "MaSach")
    private Integer maSach;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietPNKey)) return false;
        ChiTietPNKey that = (ChiTietPNKey) o;
        return Objects.equals(maPN, that.maPN) && Objects.equals(maSach, that.maSach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPN, maSach);
    }
}

