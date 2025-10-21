package com.bookstore.web.controller;

import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
@Controller
public class ProfileController {

	@Autowired
    private NguoiDungService userService;

    @GetMapping("/user/profile")
    public String showProfile(Model model) {
        Optional<NguoiDung> userOpt = userService.findById(1); // Người dùng mẫu
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "user/profile";
        }
        model.addAttribute("user", new NguoiDung());
        model.addAttribute("error", "Không tìm thấy người dùng");
        return "user/profile";
    }

    @PostMapping("/user/profile")
    public String updateProfile(@ModelAttribute("user") NguoiDung user,
                               @RequestParam("avatarFile") MultipartFile avatarFile,
                               @RequestParam(value = "matKhau", required = false) String matKhau,
                               Model model) throws IOException {
        // Kiểm tra xem maTK có hợp lệ không
        if (user.getId() == null || userService.findById(user.getId()).isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Không tìm thấy người dùng để cập nhật");
            return "user/profile";
        }

        // Xử lý upload avatar
        if (!avatarFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/avatar/";
                Files.createDirectories(Paths.get(uploadDir));
                String fileName = user.getId() + "_" + avatarFile.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.write(path, avatarFile.getBytes());
                user.setAvatar("avatar/" + fileName);
            } catch (IOException e) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Lỗi khi tải lên avatar: " + e.getMessage());
                return "user/profile";
            }
        }

        // Cập nhật mật khẩu nếu có
        if (matKhau != null && !matKhau.isEmpty()) {
            user.setMatKhau(matKhau);
        }

        try {
            userService.updateUser(user);
            return "/user/profile";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Lỗi khi cập nhật thông tin: " + e.getMessage());
            return "user/profile";
        }
    }
}
