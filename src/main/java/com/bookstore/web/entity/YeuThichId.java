package com.bookstore.web.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class YeuThichId implements Serializable {
    private Integer maTK;
    private Integer maSach;

    // Constructors
    public YeuThichId() {}

    public YeuThichId(Integer maTK, Integer maSach) {
        this.maTK = maTK;
        this.maSach = maSach;
    }
}