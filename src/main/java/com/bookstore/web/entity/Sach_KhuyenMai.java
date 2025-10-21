package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sachkhuyenmai")
@IdClass(SachKhuyenMaiId.class)
public class Sach_KhuyenMai {
    @Id
    
    private Integer maSach;
    @Id
    private Integer maKM;

    @ManyToOne
    @JoinColumn(name = "MaSach", insertable = false, updatable = false)
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "MaKM", insertable = false, updatable = false)
    private KhuyenMai khuyenMai;
    

}