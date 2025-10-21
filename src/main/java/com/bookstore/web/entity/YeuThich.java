package com.bookstore.web.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "YeuThich")
@IdClass(YeuThichId.class) 
public class YeuThich {

    @Id
    @Column(name = "MaTK", nullable = false)
    private Integer maTK;

    @Id
    @Column(name = "MaSach", nullable = false)
    private Integer maSach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSach", insertable = false, updatable = false)
    private Sach sach;
}