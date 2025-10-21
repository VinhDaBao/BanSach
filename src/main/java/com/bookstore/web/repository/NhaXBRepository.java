//package com.bookstore.web.repository;
//
//import com.bookstore.web.entity.NhaXuatBan;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface NhaXuatBanRepository extends JpaRepository<NhaXuatBan, Integer> {
//
//    // ✅ Dùng @Query để Spring không cố suy diễn "tenNXB" nữa
//    @Query("SELECT n FROM NhaXuatBan n WHERE n.TenNXB = :TenNXB")
//    NhaXuatBan findByTenNXB(@Param("TenNXB") String TenNXB);
//}

package com.bookstore.web.repository;

import com.bookstore.web.entity.NhaXB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NhaXBRepository extends JpaRepository<NhaXB, Integer> {

    // ✅ Tìm nhà xuất bản theo tên
    @Query("SELECT n FROM NhaXB n WHERE LOWER(n.tenNXB) = LOWER(:tenNXB)")
    NhaXB findByTenNXB(@Param("tenNXB") String tenNXB);
}
