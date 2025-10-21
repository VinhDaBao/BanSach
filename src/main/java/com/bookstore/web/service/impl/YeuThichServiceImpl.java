package com.bookstore.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bookstore.web.entity.YeuThich;
import com.bookstore.web.entity.YeuThichId;
import com.bookstore.web.repository.YeuThichRepository;
import com.bookstore.web.service.YeuThichService;

@Service
public class YeuThichServiceImpl implements YeuThichService {
    @Autowired
    private YeuThichRepository yeuThichRepository;

    @Override
    public boolean existsById(YeuThichId id) {
        return yeuThichRepository.existsById(id);
    }

    @Override
    public void save(YeuThich favorite) {
        yeuThichRepository.save(favorite);
    }

    @Override
    public void deleteById(YeuThichId id) {
        yeuThichRepository.deleteById(id);
    }
    
    @Override
    public List<Map<String, Object>> getTop5SachYeuThich() {
        return yeuThichRepository.findTopYeuThich(PageRequest.of(0, 5)).getContent();
    }
}