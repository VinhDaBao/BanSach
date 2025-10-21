package com.bookstore.web.service;

import java.util.List;

import com.bookstore.web.entity.DiaChi;

public interface DiaChiService {
    List<DiaChi> getByUserId(Integer userId);
    DiaChi findById(Integer id);
    DiaChi save(DiaChi diaChi);
    void deleteById(Integer id);
    DiaChi getDefaultAddress(Integer userId);
    void setDefaultAddress(Integer userId, Integer addressId);
}