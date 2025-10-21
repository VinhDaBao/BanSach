package com.bookstore.web.entity;

import java.io.Serializable;
import java.util.Objects;

public class GioHangSachId implements Serializable {
    private Integer maGH;
    private Integer maSach;

    // Default constructor
    public GioHangSachId() {}

    // Parameterized constructor
    public GioHangSachId(Integer maGH, Integer maSach) {
        this.maGH = maGH;
        this.maSach = maSach;
    }

    // Getters and Setters
    public Integer getMaGH() {
        return maGH;
    }

    public void setMaGH(Integer maGH) {
        this.maGH = maGH;
    }

    public Integer getMaSach() {
        return maSach;
    }

    public void setMaSach(Integer maSach) {
        this.maSach = maSach;
    }

    // equals and hashCode for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GioHangSachId that = (GioHangSachId) o;
        return Objects.equals(maGH, that.maGH) && Objects.equals(maSach, that.maSach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maGH, maSach);
    }
}