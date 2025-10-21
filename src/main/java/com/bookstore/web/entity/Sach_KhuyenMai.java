package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sach_KhuyenMai")
@IdClass(SachKhuyenMaiId.class)
public class Sach_KhuyenMai {
    @Id
    private Integer maSach;
    @Id
    private Integer maKM;

    @ManyToOne
    @JoinColumn(name = "maSach", insertable = false, updatable = false)
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "maKM", insertable = false, updatable = false)
    private KhuyenMai khuyenMai;
    

}