//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "HinhThuc")
//public class HinhThuc {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaHT")
//    private Integer id;
//
//    @Column(name = "TenHT", nullable = false, length = 50)
//    private String tenHT;
//}

package com.bookstore.web.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "HinhThuc")
public class HinhThuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHT")
    private Integer id;

    @Column(name = "TenHT", nullable = false, length = 50)
    private String tenHT;
}

