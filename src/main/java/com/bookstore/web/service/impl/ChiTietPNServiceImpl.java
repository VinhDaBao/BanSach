package com.bookstore.web.service.impl;

import com.bookstore.web.entity.ChiTietPN;
import com.bookstore.web.entity.ChiTietPNKey;
import com.bookstore.web.repository.ChiTietPNRepository;
import com.bookstore.web.service.ChiTietPNService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChiTietPNServiceImpl implements ChiTietPNService {

    private final ChiTietPNRepository repo;

    public ChiTietPNServiceImpl(ChiTietPNRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ChiTietPN> findAll() {
        return repo.findAll();
    }

    @Override
    public ChiTietPN findById(ChiTietPNKey id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public ChiTietPN save(ChiTietPN chiTietPN) {
        return repo.save(chiTietPN);
    }

    @Override
    public void deleteById(ChiTietPNKey id) {
        repo.deleteById(id);
    }
    
    /** ✅ Lấy danh sách chi tiết theo mã phiếu nhập */
    @Override
    public List<ChiTietPN> findByPhieuNhap(Integer maPN) {
        return repo.findByMaPhieuNhap(maPN);
    }

    /** ✅ Xóa toàn bộ chi tiết theo mã phiếu nhập */
    @Override
    public void deleteByPhieuNhap(Integer maPN) {
        repo.deleteByMaPhieuNhap(maPN);
    }
}
