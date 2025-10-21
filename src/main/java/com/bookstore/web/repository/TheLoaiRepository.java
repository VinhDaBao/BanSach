//package com.bookstore.web.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import com.bookstore.web.entity.TheLoai;
//
//public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
//	  @Query("SELECT DISTINCT tl FROM TheLoai tl LEFT JOIN FETCH tl.sach")
//	   List<TheLoai> findAllDistinct();
//}

package com.bookstore.web.repository;

import com.bookstore.web.entity.TheLoai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
	@Query("SELECT DISTINCT tl FROM TheLoai tl LEFT JOIN FETCH tl.sach")
	   List<TheLoai> findAllDistinct();
	
	 @Query("SELECT t FROM TheLoai t WHERE LOWER(t.tenTL) = LOWER(:tenTL)")
	  TheLoai findByTenTL(@Param("tenTL") String tenTL);
}
