package com.bookstore.web.service;

import java.util.List;
import java.util.Map;

import com.bookstore.web.entity.YeuThich;
import com.bookstore.web.entity.YeuThichId;

public interface YeuThichService {
    boolean existsById(YeuThichId id);
    void save(YeuThich favorite);
    void deleteById(YeuThichId id);
    List<Map<String, Object>> getTop5SachYeuThich();
}