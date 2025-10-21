package com.bookstore.web.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.entity.TraLoi;
import com.bookstore.web.repository.DanhGiaRepository;
import com.bookstore.web.repository.NguoiDungRepository;
import com.bookstore.web.repository.TraLoiRepository;
import com.bookstore.web.service.TraLoiService;

@Service
public class TraLoiServiceImpl implements TraLoiService {

	@Autowired
	private TraLoiRepository traLoiRepository;

	@Autowired
	private DanhGiaRepository danhGiaRepository;

	@Autowired
	private NguoiDungRepository nguoiDungRepository;

	@Override
	public void saveReply(Integer maDG, Integer maTK, String noiDung) {
		// Lấy đối tượng đánh giá
		DanhGia danhGia = danhGiaRepository.findById(maDG)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá có mã: " + maDG));

		// Tạo đối tượng trả lời
		TraLoi reply = new TraLoi();
		reply.setDanhGia(danhGia);
		reply.setNoiDung(noiDung);

		// Nếu có người dùng (đăng nhập) thì set, không thì bỏ qua
		if (maTK != null) {
			nguoiDungRepository.findById(maTK).ifPresent(reply::setNguoiDung);
		} else {
			reply.setNguoiDung(null); // hoặc để mặc định nếu nullable
		}

		// Lưu vào DB
		traLoiRepository.save(reply);
	}

	@Override
	public void updateReply(Integer id, String noiDungMoi) {
		TraLoi reply = traLoiRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi có id: " + id));
		reply.setNoiDung(noiDungMoi);
		reply.setNgayTao(LocalDateTime.now()); // cập nhật thời gian mới
		traLoiRepository.save(reply);
	}

	@Override
	public void deleteReply(Integer id) {
		if (!traLoiRepository.existsById(id)) {
			throw new RuntimeException("Không tìm thấy phản hồi có id: " + id);
		}
		traLoiRepository.deleteById(id);
	}

	 @Override
	    public List<TraLoi> findByDanhGiaId(Integer danhGiaId) {
	        return traLoiRepository.findByDanhGia_Id(danhGiaId);
	    }

}
