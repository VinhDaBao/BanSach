package com.bookstore.web.service.impl;

import com.bookstore.web.entity.NhaCungCap;
import com.bookstore.web.repository.NhaCungCapRepository;
import com.bookstore.web.service.NhaCungCapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    // Constructor injection
    @Autowired
    public NhaCungCapServiceImpl(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    @Override
    public List<NhaCungCap> findAll() {
        return nhaCungCapRepository.findAll();
    } 

    @Override
    public NhaCungCap findById(Integer id) {
        return nhaCungCapRepository.findById(id).orElse(null);
    }

    @Override
    public NhaCungCap save(NhaCungCap ncc) {
        return nhaCungCapRepository.save(ncc);
    }

    @Override
    public void deleteById(Integer id) {
        nhaCungCapRepository.deleteById(id); 
    }
    
    @Override
    public NhaCungCap findByTenNCC(String tenNCC) {
        return nhaCungCapRepository.findByTenNCC(tenNCC);
    }
}
