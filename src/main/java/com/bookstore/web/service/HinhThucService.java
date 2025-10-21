package com.bookstore.web.service;

import com.bookstore.web.entity.HinhThuc;
import java.util.List;

public interface HinhThucService {

    List<HinhThuc> findAll();

    HinhThuc findById(Integer MaHT);

    HinhThuc findByTenHT(String TenHT);

    HinhThuc save(HinhThuc hinhThuc);

    void deleteById(Integer MaHT);
}
