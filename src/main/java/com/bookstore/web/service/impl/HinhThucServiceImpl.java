package com.bookstore.web.service.impl;

import com.bookstore.web.entity.HinhThuc;
import com.bookstore.web.repository.HinhThucRepository;
import com.bookstore.web.service.HinhThucService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HinhThucServiceImpl implements HinhThucService {

    private final HinhThucRepository repo;

    public HinhThucServiceImpl(HinhThucRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<HinhThuc> findAll() {
        return repo.findAll();
    }

    @Override
    public HinhThuc findById(Integer MaHT) {
        return repo.findById(MaHT).orElse(null);
    }

    @Override
    public HinhThuc findByTenHT(String TenHT) {
        return repo.findByTenHT(TenHT);
    }

    @Override
    public HinhThuc save(HinhThuc hinhThuc) {
        return repo.save(hinhThuc);
    }

    @Override
    public void deleteById(Integer MaHT) {
        repo.deleteById(MaHT);
    }
}
