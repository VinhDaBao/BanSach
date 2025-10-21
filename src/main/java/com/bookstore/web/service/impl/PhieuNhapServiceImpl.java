//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.ChiTietPN;
//import com.bookstore.web.entity.ChiTietPNKey;
//import com.bookstore.web.entity.NhaCungCap;
//import com.bookstore.web.entity.PhieuNhap;
//import com.bookstore.web.entity.Sach;
//import com.bookstore.web.repository.PhieuNhapRepository;
//import com.bookstore.web.service.ChiTietPNService;
//import com.bookstore.web.service.PhieuNhapService;
//import com.bookstore.web.service.SachService;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class PhieuNhapServiceImpl implements PhieuNhapService {
//
//    private final PhieuNhapRepository repo;
//    
//    private final ChiTietPNService chiTietPNService; 
//    
//    private final SachService sachService;
//
//    public PhieuNhapServiceImpl(PhieuNhapRepository repo, ChiTietPNService chiTietPNService, SachService sachService) {
//        this.repo = repo;
//        this.chiTietPNService = chiTietPNService;
//        this.sachService = sachService;
//    }
//
//    @Override
//    public List<PhieuNhap> findAll() {
//        return repo.findAll();
//    }
//
//    @Override
//    public PhieuNhap findById(Integer MaPN) {
//        return repo.findById(MaPN).orElse(null);
//    }
//
//    @Override
//    public PhieuNhap save(PhieuNhap phieuNhap) {
//        return repo.save(phieuNhap);
//    }
//
//    @Override
//    public void deleteById(Integer MaPN) {
//        repo.deleteById(MaPN);
//    }
//    
// // ✅ Triển khai phương thức mới
//    @Override
//    public List<PhieuNhap> findByNhaCungCap(NhaCungCap NhaCungCap) {
//        return repo.findByNhaCungCap(NhaCungCap);
//    }
//    
//    /** ---------------- LƯU PHIẾU NHẬP KÈM CHI TIẾT ---------------- */
//    @Transactional
//    @Override
//    public PhieuNhap saveWithDetails(PhieuNhap phieuNhap, List<ChiTietPN> chiTietList) {
//        PhieuNhap saved = (phieuNhap.getMaPN() != null)
//                ? repo.findById(phieuNhap.getMaPN()).orElse(phieuNhap)
//                : phieuNhap;
//
//        saved.setNgayNhap(phieuNhap.getNgayNhap());
//        saved.setNhaCungCap(phieuNhap.getNhaCungCap());
//
//        // Lưu phiếu trước để có MaPN
//        saved = repo.save(saved);
//
//        for (ChiTietPN ct : chiTietList) {
//            Sach sach = ct.getSach();
//            if (sach != null) {
//                // ❗ Không cộng tồn kho ở đây
//            	sachService.updateSoLuongTonSauNhap(sach.getMaSach(), ct.getSoLuong());
//            }
//
//            // Nếu chi tiết đã tồn tại trong phiếu -> cộng dồn số lượng nhập
//            Optional<ChiTietPN> existedOpt = saved.getChiTietPNList().stream()
//                    .filter(old -> old.getSach() != null &&
//                            old.getSach().getMaSach().equals(ct.getSach().getMaSach()))
//                    .findFirst();
//
//            if (existedOpt.isPresent()) {
//                ChiTietPN existed = existedOpt.get();
//                existed.setSoLuong(ct.getSoLuong());
//                existed.setGiaNhap(ct.getGiaNhap());
//            } else {
//                ChiTietPN newCT = new ChiTietPN();
//                newCT.setPhieuNhap(saved);
//                newCT.setSach(sach);
//                newCT.setSoLuong(ct.getSoLuong());
//                newCT.setGiaNhap(ct.getGiaNhap());
//                newCT.setId(new ChiTietPNKey(saved.getMaPN(), sach.getMaSach()));
//
//                saved.getChiTietPNList().add(newCT);
//            }
//        }
//
//        // ✅ Tổng tiền chỉ tính theo phiếu nhập (không cộng tồn kho)
//        BigDecimal tongTien = saved.getChiTietPNList().stream()
//                .map(ct -> ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        saved.setTongTien(tongTien);
//
//        return repo.save(saved);
//    }
//}

//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.ChiTietPN;
//import com.bookstore.web.entity.ChiTietPNKey;
//import com.bookstore.web.entity.NhaCungCap;
//import com.bookstore.web.entity.PhieuNhap;
//import com.bookstore.web.entity.Sach;
//import com.bookstore.web.repository.PhieuNhapRepository;
//import com.bookstore.web.service.ChiTietPNService;
//import com.bookstore.web.service.PhieuNhapService;
//import com.bookstore.web.service.SachService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PhieuNhapServiceImpl implements PhieuNhapService {
//
//    private final PhieuNhapRepository phieuNhapRepository;
//    private final ChiTietPNService chiTietPNService;
//    private final SachService sachService;
//
//    // ==================== CRUD CƠ BẢN ====================
//    @Override
//    public List<PhieuNhap> findAll() {
//        return phieuNhapRepository.findAll();
//    }
//
//    @Override
//    public PhieuNhap findById(Integer maPN) {
//        return phieuNhapRepository.findById(maPN).orElse(null);
//    }
//
//    @Override
//    public PhieuNhap save(PhieuNhap phieuNhap) {
//        return phieuNhapRepository.save(phieuNhap);
//    }
//
//    @Override
//    public void deleteById(Integer maPN) {
//        phieuNhapRepository.deleteById(maPN);
//    }
//
//    // ==================== TÌM THEO NHÀ CUNG CẤP ====================
//    @Override
//    public List<PhieuNhap> findByNhaCungCap(NhaCungCap nhaCungCap) {
//        return phieuNhapRepository.findByNhaCungCap(nhaCungCap);
//    }
//
//    // ==================== LƯU PHIẾU NHẬP KÈM CHI TIẾT ====================
//    @Transactional
//    @Override
//    public PhieuNhap saveWithDetails(PhieuNhap phieuNhap, List<ChiTietPN> chiTietList) {
//        PhieuNhap saved = (phieuNhap.getMaPN() != null)
//                ? phieuNhapRepository.findById(phieuNhap.getMaPN()).orElse(phieuNhap)
//                : phieuNhap;
//
//        // Cập nhật thông tin cơ bản
//        saved.setNgayNhap(phieuNhap.getNgayNhap());
//        saved.setNhaCungCap(phieuNhap.getNhaCungCap());
//
//        // Lưu phiếu nhập trước để có ID
//        saved = phieuNhapRepository.save(saved);
//
//        // Duyệt qua danh sách chi tiết
//        for (ChiTietPN ct : chiTietList) {
//            Sach sach = ct.getSach();
//            if (sach != null && sach.getId() != null) {
//                // ✅ Cập nhật số lượng tồn (sau khi nhập hàng)
//                sachService.updateSoLuongTonSauNhap(sach.getId(), ct.getSoLuong());
//            }
//
//            // ✅ Kiểm tra chi tiết đã tồn tại trong phiếu hay chưa
//            Optional<ChiTietPN> existedOpt = saved.getChiTietPNList().stream()
//                    .filter(old -> old.getSach() != null &&
//                            old.getSach().getId().equals(ct.getSach().getId()))
//                    .findFirst();
//
//            if (existedOpt.isPresent()) {
//                // Nếu đã có -> cập nhật lại số lượng và giá nhập
//                ChiTietPN existed = existedOpt.get();
//                existed.setSoLuong(ct.getSoLuong());
//                existed.setGiaNhap(ct.getGiaNhap());
//            } else {
//                // Nếu chưa có -> tạo mới
//                ChiTietPN newCT = new ChiTietPN();
//                newCT.setPhieuNhap(saved);
//                newCT.setSach(sach);
//                newCT.setSoLuong(ct.getSoLuong());
//                newCT.setGiaNhap(ct.getGiaNhap());
//                newCT.setId(new ChiTietPNKey(saved.getMaPN(), sach.getId()));
//                saved.getChiTietPNList().add(newCT);
//            }
//        }
//
//        // ✅ Tính tổng tiền của phiếu nhập
//        BigDecimal tongTien = saved.getChiTietPNList().stream()
//                .map(ct -> ct.getGiaNhap().multiply(BigDecimal.valueOf(ct.getSoLuong())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        saved.setTongTien(tongTien);
//
//        // Lưu lại phiếu nhập hoàn chỉnh
//        return phieuNhapRepository.save(saved);
//    }
//}



package com.bookstore.web.service.impl;

import com.bookstore.web.entity.*;
import com.bookstore.web.repository.PhieuNhapRepository;
import com.bookstore.web.service.ChiTietPNService;
import com.bookstore.web.service.PhieuNhapService;
import com.bookstore.web.service.SachService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhieuNhapServiceImpl implements PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPNService chiTietPNService;
    private final SachService sachService;

    // ==================== CRUD CƠ BẢN ====================
    @Override
    public List<PhieuNhap> findAll() {
        return phieuNhapRepository.findAll();
    }

    @Override
    public PhieuNhap findById(Integer maPN) {
        return phieuNhapRepository.findById(maPN).orElse(null);
    }

    @Override
    public PhieuNhap save(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    @Override
    public void deleteById(Integer maPN) {
        phieuNhapRepository.deleteById(maPN);
    }

    // ==================== TÌM THEO NHÀ CUNG CẤP ====================
    @Override
    public List<PhieuNhap> findByNhaCungCap(NhaCungCap nhaCungCap) {
        return phieuNhapRepository.findByNhaCungCap(nhaCungCap);
    }

    // ==================== LƯU PHIẾU NHẬP KÈM CHI TIẾT ====================
    @Transactional
    @Override
    public PhieuNhap saveWithDetails(PhieuNhap phieuNhap, List<ChiTietPN> newChiTietList) {

        PhieuNhap saved;
        if (phieuNhap.getMaPN() != null) {
            // Cập nhật phiếu nhập cũ
            saved = phieuNhapRepository.findById(phieuNhap.getMaPN())
                    .orElseThrow(() -> new RuntimeException("Phiếu nhập không tồn tại!"));
            saved.setNgayNhap(phieuNhap.getNgayNhap());
            saved.setNhaCungCap(phieuNhap.getNhaCungCap());
        } else {
            // Tạo phiếu nhập mới
            saved = phieuNhap;
        }

        // ==================== XÓA CHI TIẾT CŨ KHÔNG CÓ TRONG DS MỚI ====================
        List<ChiTietPN> toRemove = new ArrayList<>();
        for (ChiTietPN oldCT : saved.getChiTietPNList()) {
            boolean exists = newChiTietList.stream()
                    .anyMatch(nc -> nc.getSach().getId().equals(oldCT.getSach().getId()));
            if (!exists) {
                toRemove.add(oldCT);
            }
        }
        for (ChiTietPN ct : toRemove) {
            saved.removeChiTietPN(ct);
        }

        // ==================== THÊM/CẬP NHẬT CHI TIẾT MỚI ====================
        for (ChiTietPN ct : newChiTietList) {
            if (ct.getSach() == null || ct.getSach().getId() == null) continue;

            // Cập nhật tồn kho
            sachService.findById(ct.getSach().getId()).ifPresent(existingSach -> {
                int tonMoi = existingSach.getSoLuongTon() + ct.getSoLuong();
                existingSach.setSoLuongTon(tonMoi);
                if (Boolean.FALSE.equals(existingSach.getTrangThai())) {
                    existingSach.setTrangThai(true);
                }
                sachService.save(existingSach);
            });

            // Kiểm tra đã tồn tại chi tiết với sách này chưa
            boolean exists = saved.getChiTietPNList().stream()
                    .anyMatch(oldCT -> oldCT.getSach().getId().equals(ct.getSach().getId()));
            if (!exists) {
                ChiTietPN newCT = new ChiTietPN();
                newCT.setPhieuNhap(saved);
                newCT.setSach(ct.getSach());
                newCT.setSoLuong(ct.getSoLuong());
                newCT.setGiaNhap(ct.getGiaNhap());
                newCT.setId(new ChiTietPNKey(saved.getMaPN(), ct.getSach().getId()));
                saved.addChiTietPN(newCT);
            } else {
                // Cập nhật số lượng và giá nếu đã tồn tại
                for (ChiTietPN oldCT : saved.getChiTietPNList()) {
                    if (oldCT.getSach().getId().equals(ct.getSach().getId())) {
                        oldCT.setSoLuong(ct.getSoLuong());
                        oldCT.setGiaNhap(ct.getGiaNhap());
                    }
                }
            }
        }

        // ==================== CẬP NHẬT TỔNG TIỀN ====================
        saved.setTongTien(saved.getTongTien());
        // ==================== LƯU LẠI PHIẾU NHẬP HOÀN CHỈNH ====================
        return phieuNhapRepository.save(saved);
    }
}
