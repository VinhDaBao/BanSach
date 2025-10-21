//// src/main/java/com/bookstore/web/service/impl/NguoiDungServiceImpl.java
//package com.bookstore.web.service.impl;
//
//import com.bookstore.web.entity.NguoiDung;
//import com.bookstore.web.repository.NguoiDungRepository;
//import com.bookstore.web.service.NguoiDungService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class NguoiDungServiceImpl implements NguoiDungService {
//
//    @Autowired
//    private NguoiDungRepository nguoiDungRepository;
//
//    @Override
//    public List<NguoiDung> findAll() {
//        return nguoiDungRepository.findAll();
//    }
//
//    @Override
//    public Optional<NguoiDung> findById(Integer id) {
//        return nguoiDungRepository.findById(id);
//    }
//
//    @Override
//    public NguoiDung save(NguoiDung nguoiDung) {
//        return nguoiDungRepository.save(nguoiDung);
//    }
//
//    @Override
//    public void deleteById(Integer id) {
//        nguoiDungRepository.deleteById(id);
//    }
//
//    @Override
//    public long countByVaiTro(String vaiTro) {
//        return nguoiDungRepository.countByVaiTro(vaiTro);
//    }
//
//    @Override
//    public long countByCreatedAtAfter(LocalDateTime date) {
//        return nguoiDungRepository.countByCreatedAtAfter(date);
//    }
//
//    @Override
//    public Optional<NguoiDung> findByTaiKhoan(String taiKhoan) {
//        return nguoiDungRepository.findByTaiKhoan(taiKhoan);
//    }
//
//    @Override
//    public NguoiDung login(String taikhoan, String matkhau) {
//        return nguoiDungRepository.findByTaiKhoanAndMatKhau(taikhoan, matkhau);
//    }
//
//    @Override
//    public Optional<NguoiDung> findById(int id) {
//        return nguoiDungRepository.findById(id);
//    }
//
//    @Override
//    public boolean existsByTaiKhoan(String taiKhoan) {
//        System.out.println("DEBUG service existsByTaiKhoan: " + taiKhoan);
//        boolean result = nguoiDungRepository.existsByTaiKhoan(taiKhoan);
//        System.out.println("DEBUG repository result: " + result);
//        return result;
//    }
//
//    @Override
//    public void updateUser(NguoiDung nguoiDung) {
//        Optional<NguoiDung> existingUser = nguoiDungRepository.findById(nguoiDung.getId());
//        if (existingUser.isPresent()) {
//            NguoiDung updatedUser = existingUser.get();
//            updatedUser.setHoTen(nguoiDung.getHoTen());
//            updatedUser.setGioiTinh(nguoiDung.getGioiTinh());
//            updatedUser.setNgaySinh(nguoiDung.getNgaySinh());
//            updatedUser.setDiaChi(nguoiDung.getDiaChi());
//            updatedUser.setSdt(nguoiDung.getSdt());
//            updatedUser.setAvatar(nguoiDung.getAvatar());
//            if (nguoiDung.getMatKhau() != null && !nguoiDung.getMatKhau().isEmpty()) {
//                updatedUser.setMatKhau(nguoiDung.getMatKhau());
//            }
//            nguoiDungRepository.save(updatedUser);
//        }
//    }
//}

package com.bookstore.web.service.impl;

import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.repository.NguoiDungRepository;
import com.bookstore.web.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {

	private final NguoiDungRepository nguoiDungRepository;

	@Autowired
	public NguoiDungServiceImpl(NguoiDungRepository nguoiDungRepository) {
		this.nguoiDungRepository = nguoiDungRepository;
	}

	@Override
	public List<NguoiDung> findAll() {
		return nguoiDungRepository.findAll();
	}

	@Override
	public Optional<NguoiDung> findById(Integer id) {
		return nguoiDungRepository.findById(id);
	}

	@Override
	public NguoiDung save(NguoiDung nguoiDung) {
		return nguoiDungRepository.save(nguoiDung);
	}

	@Override
	public void deleteById(Integer id) {
		nguoiDungRepository.deleteById(id);
	}


	@Override
	public long countByCreatedAtAfter(LocalDateTime date) {
		return nguoiDungRepository.countByCreatedAtAfter(date);
	}

	@Override
	public Optional<NguoiDung> findByTaiKhoan(String taiKhoan) {
		return nguoiDungRepository.findByTaiKhoan(taiKhoan);
	}

	@Override
	public NguoiDung login(String taiKhoan, String matKhau) {
		return nguoiDungRepository.findByTaiKhoanAndMatKhau(taiKhoan, matKhau);
	}

	@Override
	public boolean existsByTaiKhoan(String taiKhoan) {
		return nguoiDungRepository.existsByTaiKhoan(taiKhoan);
	}

	@Override
	public void updateUser(NguoiDung nguoiDung) {
		Optional<NguoiDung> existingUser = nguoiDungRepository.findById(nguoiDung.getId());
		if (existingUser.isPresent()) {
			NguoiDung updatedUser = existingUser.get();
			updatedUser.setHoTen(nguoiDung.getHoTen());
			updatedUser.setGioiTinh(nguoiDung.getGioiTinh());
			updatedUser.setNgaySinh(nguoiDung.getNgaySinh());
			updatedUser.setDiaChi(nguoiDung.getDiaChi());
			updatedUser.setSdt(nguoiDung.getSdt());
			updatedUser.setAvatar(nguoiDung.getAvatar());

			if (nguoiDung.getMatKhau() != null && !nguoiDung.getMatKhau().isEmpty()) {
				updatedUser.setMatKhau(nguoiDung.getMatKhau());
			}

			nguoiDungRepository.save(updatedUser);
		}
	}

	@Override
	public List<NguoiDung> findByVaiTro(String vaiTro) {
		return nguoiDungRepository.findByVaiTro(vaiTro);
	}

	@Override
	public long countByVaiTro(String vaiTro) {
		return nguoiDungRepository.countByVaiTro(vaiTro);
	}

	@Override
	public long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
		return nguoiDungRepository.countByNgayTaoBetween(from, to);
	}
	
	@Override
    public long countAllUsers() {
        return nguoiDungRepository.countAllUsers();
    }

    @Override
    public long countRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return nguoiDungRepository.countRegisteredBetween(startDate, endDate);
    }
}
