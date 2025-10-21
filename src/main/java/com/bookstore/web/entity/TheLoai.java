//package com.bookstore.web.entity;
//
//import jakarta.persistence.*;
//import java.util.Set;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//
//
//@Data
//@Entity
//@Table(name = "TheLoai")
//public class TheLoai {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "MaTL")
//    private Integer id;
//
//    @Column(name = "TenTL", nullable = false, length = 100)
//    private String tenTL;
//    
//
//    @ManyToMany(mappedBy = "danhSachTheLoai")
//    @EqualsAndHashCode.Exclude 
//    @ToString.Exclude 
//    private Set<Sach> sach;
//
//
//}
//

package com.bookstore.web.entity;

import jakarta.persistence.*;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;



@Data
@Entity
@Table(name = "theloai")
public class TheLoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTL")
    private Integer id;

    @Column(name = "TenTL", nullable = false, length = 100)
    private String tenTL;
    

    @ManyToMany(mappedBy = "danhSachTheLoai")
    @EqualsAndHashCode.Exclude 
    @ToString.Exclude 
    private Set<Sach> sach;


}

