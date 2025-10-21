//package com.bookstore.web.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.bookstore.web.entity.NhaCungCap;
//
//public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {}

package com.bookstore.web.repository;

import com.bookstore.web.entity.NhaCungCap;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {

    // ✅ Tìm nhà cung cấp theo tên (không phân biệt hoa thường)
	@Query("SELECT n FROM NhaCungCap n WHERE LOWER(n.tenNCC) = LOWER(:tenNCC)")
    NhaCungCap findByTenNCC(@Param("tenNCC") String tenNCC);

    List<NhaCungCap> findAll();
}