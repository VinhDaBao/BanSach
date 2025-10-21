//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.NhaXuatBan;
//import com.bookstore.web.repository.NhaXuatBanRepository;
//import com.bookstore.web.service.NhaXuatBanService;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class NhaXuatBanServiceImpl implements NhaXuatBanService {
//
//    private final NhaXuatBanRepository repo;
//
//    public NhaXuatBanServiceImpl(NhaXuatBanRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public List<NhaXuatBan> findAll() {
//        return repo.findAll();
//    }
//
//    @Override
//    public NhaXuatBan findById(Integer MaNXB) {
//        return repo.findById(MaNXB).orElse(null);
//    }
//
//    @Override
//    public NhaXuatBan findByTenNXB(String TenNXB) {
//        return repo.findByTenNXB(TenNXB);
//    }
//
//    @Override
//    public NhaXuatBan save(NhaXuatBan nxb) {
//        return repo.save(nxb);
//    }
//
//    @Override
//    public void deleteById(Integer MaNXB) {
//        repo.deleteById(MaNXB);
//    }
//}
//

package com.bookstore.web.service.impl;

import com.bookstore.web.entity.NhaXB;
import com.bookstore.web.repository.NhaXBRepository;
import com.bookstore.web.service.NhaXBService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhaXBServiceImpl implements NhaXBService {

    private final NhaXBRepository nhaXBRepository;

    public NhaXBServiceImpl(NhaXBRepository nhaXBRepository) {
        this.nhaXBRepository = nhaXBRepository;
    }

    // ==================== CRUD CƠ BẢN ====================
    @Override
    public List<NhaXB> findAll() {
        return nhaXBRepository.findAll();
    }

    @Override
    public NhaXB findById(Integer maNXB) {
        return nhaXBRepository.findById(maNXB).orElse(null);
    }

    @Override
    public NhaXB findByTenNXB(String tenNXB) {
        return nhaXBRepository.findByTenNXB(tenNXB);
    }

    @Override
    public NhaXB save(NhaXB nhaXB) {
        return nhaXBRepository.save(nhaXB);
    }

    @Override
    public void deleteById(Integer maNXB) {
        nhaXBRepository.deleteById(maNXB);
    }
    
    @Override
    public NhaXB saveIfNotExists(String tenNXB) {
        if (tenNXB == null || tenNXB.isBlank()) return null;
        NhaXB existing = nhaXBRepository.findByTenNXB(tenNXB.trim());
        if (existing != null) return existing;

        NhaXB newNXB = new NhaXB();
        newNXB.setTenNXB(tenNXB.trim());
        newNXB.setDiaChi("Chưa cập nhật");   // giá trị mặc định
        newNXB.setNamThanhLap(2000);  
        return nhaXBRepository.save(newNXB);
    }
}

