//package com.bookstore.web.service.impl;
//
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.bookstore.web.entity.TheLoai;
//import com.bookstore.web.repository.TheLoaiRepository;
//import com.bookstore.web.service.TheLoaiService;
//
//@Service
//public class TheLoaiServiceImpl implements TheLoaiService {
//
//    private final TheLoaiRepository theLoaiRepository;
//
//    public TheLoaiServiceImpl(TheLoaiRepository theLoaiRepository) {
//        this.theLoaiRepository = theLoaiRepository;
//    }
//
//    @Override
//    public List<TheLoai> findAll() {
//        return theLoaiRepository.findAll();
//    }
//
//    @Override
//    public TheLoai findById(Integer id) {
//        return theLoaiRepository.findById(id).orElse(null);
//    }
//    @Override
//    public List<TheLoai> getAllTheLoai() {
//        return theLoaiRepository.findAllDistinct();
//    }
//}

package com.bookstore.web.service.impl;

import com.bookstore.web.entity.TheLoai;
import com.bookstore.web.repository.TheLoaiRepository;
import com.bookstore.web.service.TheLoaiService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheLoaiServiceImpl implements TheLoaiService {

    private final TheLoaiRepository theLoaiRepository;

    // ✅ Constructor Injection — chuẩn Spring Boot, dễ test
    public TheLoaiServiceImpl(TheLoaiRepository theLoaiRepository) {
        this.theLoaiRepository = theLoaiRepository;
    }

    // ==================== CRUD CƠ BẢN ====================

    @Override
    public List<TheLoai> findAll() {
        return theLoaiRepository.findAll();
    }

    @Override
    public Optional<TheLoai> findById(Integer maTL) {
        return theLoaiRepository.findById(maTL);
    }

    @Override
    public TheLoai save(TheLoai theLoai) {
        return theLoaiRepository.save(theLoai);
    }

    @Override
    public void deleteById(Integer maTL) {
        theLoaiRepository.deleteById(maTL);
    }

    // ==================== TRUY VẤN MỞ RỘNG ====================

    @Override
    public List<TheLoai> getAllTheLoai() {
        // Dùng query DISTINCT trong repository để tránh dữ liệu trùng
        return theLoaiRepository.findAllDistinct();
    }

    @Override
    public TheLoai findByTenTL(String tenTL) {
        // Nếu repository chưa có phương thức findByTenTL, bạn cần thêm nó
        // vào TheLoaiRepository: TheLoai findByTenTLIgnoreCase(String tenTL);
        return theLoaiRepository.findAll()
                .stream()
                .filter(t -> t.getTenTL().equalsIgnoreCase(tenTL))
                .findFirst()
                .orElse(null);
    }

    // ==================== TIỆN ÍCH ====================

    @Override
    public long countAll() {
        return theLoaiRepository.count();
    }
}

