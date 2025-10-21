package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "giohang")
@Data
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maGH;

    @ManyToOne
    @JoinColumn(name = "MaTK", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}