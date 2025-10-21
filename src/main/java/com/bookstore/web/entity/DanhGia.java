package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "danhgia")
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDG")
    private Integer id;

    @Column(name = "NoiDung", nullable = false, length = 500)
    private String noiDung;

    @Column(name = "SoSao", nullable = false)
    private int soSao;

    @CreationTimestamp 
    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp 
    @Column(name = "NgaySua")
    private LocalDateTime ngaySua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSach", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Sach sach;

    @Column(name = "MediaUrls", columnDefinition = "TEXT")
    private String mediaUrls;
    
    @OneToMany(mappedBy = "danhGia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TraLoi> traLois;

}