//package com.bookstore.web.entity;
//
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "NhaXB")
//public class NhaXB {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaNXB")
//    private Integer id;
//
//    @Column(name = "TenNXB", nullable = false, length = 100)
//    private String tenNXB;
//
//    @Column(name = "DiaChi", nullable = false, length = 200)
//    private String diaChi;
//
//    @Column(name = "NamThanhLap")
//    private Integer namThanhLap;
//}

package com.bookstore.web.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nhaxb")
public class NhaXB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNXB")
    private Integer id;

    @Column(name = "TenNXB", nullable = false, length = 100)
    private String tenNXB;

    @Column(name = "DiaChi", nullable = false, length = 200)
    private String diaChi;

    @Column(name = "NamThanhLap")
    private Integer namThanhLap;
}