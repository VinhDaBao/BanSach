package com.bookstore.web.repository;

import com.bookstore.web.entity.TraLoi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TraLoiRepository extends JpaRepository<TraLoi, Integer> {
    List<TraLoi> findByDanhGia_Id(Integer maDG);

}
