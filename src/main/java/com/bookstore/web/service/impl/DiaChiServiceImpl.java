package com.bookstore.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.entity.DiaChi;
import com.bookstore.web.repository.DiaChiRepository;
import com.bookstore.web.service.DiaChiService;

@Service
public class DiaChiServiceImpl implements DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Override
    public List<DiaChi> getByUserId(Integer userId) {
        return diaChiRepository.findByNguoiDung_IdOrderByMacDinhDesc(userId);
    }

    @Override
    public DiaChi findById(Integer id) {
        return diaChiRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public DiaChi save(DiaChi diaChi) {
        // Nếu đây là địa chỉ mặc định, bỏ mặc định của các địa chỉ khác
        if (diaChi.getMacDinh() != null && diaChi.getMacDinh()) {
            List<DiaChi> allAddresses = diaChiRepository.findByNguoiDung_IdOrderByMacDinhDesc(
                diaChi.getNguoiDung().getId());
            for (DiaChi addr : allAddresses) {
                if (!addr.getId().equals(diaChi.getId())) {
                    addr.setMacDinh(false);
                    diaChiRepository.save(addr);
                }
            }
        }
        return diaChiRepository.save(diaChi);
    }

    @Override
    public void deleteById(Integer id) {
        diaChiRepository.deleteById(id);
    }

    @Override
    public DiaChi getDefaultAddress(Integer userId) {
        return diaChiRepository.findByNguoiDung_IdAndMacDinh(userId, true).orElse(null);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Integer userId, Integer addressId) {
        List<DiaChi> allAddresses = diaChiRepository.findByNguoiDung_IdOrderByMacDinhDesc(userId);
        for (DiaChi addr : allAddresses) {
            addr.setMacDinh(addr.getId().equals(addressId));
            diaChiRepository.save(addr);
        }
    }

}
