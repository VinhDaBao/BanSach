package com.bookstore.web.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "traloi")
public class TraLoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTraLoi")
    private Integer id;

    @Column(name = "NoiDung", nullable = false, length = 500)
    private String noiDung;

    @CreationTimestamp
    @Column(name = "NgayTao", updatable = false)
    private LocalDateTime ngayTao;

    @UpdateTimestamp
    @Column(name = "NgaySua")
    private LocalDateTime ngaySua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDG") 
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DanhGia danhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK") 
    private NguoiDung nguoiDung;
}
