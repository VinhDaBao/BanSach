package com.bookstore.web.service;

import java.util.List;
import com.bookstore.web.entity.TraLoi;

public interface TraLoiService {
    void saveReply(Integer maDG, Integer maTK, String noiDung);
    void updateReply(Integer id, String noiDungMoi);
    void deleteReply(Integer id);
    List<TraLoi> findByDanhGiaId(Integer danhGiaId);
}

