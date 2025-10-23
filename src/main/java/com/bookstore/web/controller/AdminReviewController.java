package com.bookstore.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bookstore.web.entity.DanhGia;
import com.bookstore.web.service.DanhGiaService;
import com.bookstore.web.service.TraLoiService;

@Controller
@RequestMapping("/admin/danhgia")
public class AdminReviewController {

    @Autowired
    private DanhGiaService danhGiaService;

    @Autowired
    private TraLoiService traLoiService;
    @GetMapping
    public String viewAllReviews(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 5; // Số đánh giá mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<DanhGia> reviewPage = danhGiaService.findAllByOrderByNgayTaoDesc(pageable);
        model.addAttribute("allReviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        return "admin/danhgia/review-all";
    }

//Gửi phản hồi mới
    @PostMapping("/reply/{maDG}")
    @ResponseBody
    public ResponseEntity<?> replyToReview(@PathVariable Integer maDG, @RequestParam("noiDung") String noiDung) {
        try {
            traLoiService.saveReply(maDG, null, noiDung);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi gửi phản hồi"));
        }
    }

//cập nhật phản hồi
    @PostMapping("/reply/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateReply(@PathVariable("id") Integer id,
                                         @RequestParam("noiDungMoi") String noiDungMoi) {
        try {
            traLoiService.updateReply(id, noiDungMoi);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi cập nhật phản hồi"));
        }
    }


    @PostMapping("/reply/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteReply(@PathVariable("id") Integer id) {
        try {
            traLoiService.deleteReply(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Lỗi khi xóa phản hồi"));
        }
    }
}
