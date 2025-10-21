//package com.bookstore.web.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.bookstore.web.entity.NguoiDung;
//import com.bookstore.web.service.NguoiDungService;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    private NguoiDungService nguoiDungService;
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "auth/login"; 
//    }
//    
//    @PostMapping("/login")
//    public String doLogin(
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            HttpSession session,
//            Model model) {
//
//        NguoiDung user = nguoiDungService.login(username, password);
//
//        if (user != null) {
//            String role = user.getVaiTro();  
//            session.setAttribute("loggedUser", user);  
//            
//            if ("USER".equals(role)) {
//                return "redirect:/products";  
//            } else if ("ADMIN".equals(role)) {
//                return "redirect:/admin/dashboard"; 
//            } else {
//                model.addAttribute("error", "Vai trò không được phép truy cập!");
//                return "/login";  
//            }
//        } else {
//            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
//            return "auth/login";  
//        }
//    }
//    /* không chạy dc*/
//    @GetMapping("/user/login")
//    public String userLogin(Model model, HttpSession session)
//    {
//        NguoiDung user = (NguoiDung) session.getAttribute("loggedUser");
//        NguoiDung nuser = nguoiDungService.findById(user.getId()).orElse(null);
//        if (user == null) {                   
//            user = new NguoiDung();          
//        }
//        model.addAttribute("user",user);
//		return "auth/login";
//    }
//    
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {	
//        session.invalidate(); 
//        return "redirect:/products";
//    }
//    
//    @GetMapping("/register")
//    public String showRegisterPage(Model model) {
//        model.addAttribute("user", new NguoiDung());  
//        return "auth/register";  
//    }
//    
//    @PostMapping("/register")
//    public String doRegister(@ModelAttribute("user") @Valid NguoiDung user, 
//                             BindingResult result, 
//                             @RequestParam("confirmMatKhau") String confirmMatKhau,
//                             Model model, 
//                             RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "auth/register"; 
//        }
//        if (!user.getMatKhau().equals(confirmMatKhau)) {
//            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
//            return "auth/register";
//        }
//        
//        if (nguoiDungService.existsByTaiKhoan(user.getTaiKhoan())) {
//            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
//            return "auth/register";
//        }
//        user.setVaiTro("USER");      
//        nguoiDungService.save(user); 
//        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
//        return "redirect:/login";
//    }
//    
//    @GetMapping("/check-username")
//    @ResponseBody
//    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("taiKhoan") String taiKhoan) {
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("exists", nguoiDungService.existsByTaiKhoan(taiKhoan));
//        return ResponseEntity.ok(response);  
//    }
//
//    @GetMapping("/forgot-password")
//    public String showForgotPasswordPage(Model model) {
//        model.addAttribute("user", new NguoiDung());  
//        return "auth/forgot-password";
//    }
//
//    @PostMapping("/forgot-password")
//    public String forgotPassword(@ModelAttribute("user") NguoiDung user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
//        if (result.hasErrors()) {
//            return "auth/forgot-password";
//        }
//        NguoiDung foundUser = nguoiDungService.findByTaiKhoan(user.getTaiKhoan()).orElse(null);
//        if (foundUser == null) {
//            model.addAttribute("error", "Tên đăng nhập không tồn tại!");
//            return "auth/forgot-password";
//        }
//        foundUser.setMatKhau(user.getMatKhau()); 
//        nguoiDungService.save(foundUser);
//        redirectAttributes.addFlashAttribute("successMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
//        return "redirect:/login";
//    }
//}

package com.bookstore.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.web.entity.NguoiDung;
import com.bookstore.web.service.NguoiDungService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {

	@Autowired
	private NguoiDungService nguoiDungService;

	@GetMapping("/login")
	public String showLoginPage() {
		return "auth/login";
	}

	@PostMapping("/login")
	public String doLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session, Model model) {

		NguoiDung user = nguoiDungService.login(username, password);

		if (user != null) {
			session.setAttribute("loggedUser", user);
			session.setAttribute("userId", user.getId());
			// ✅ Kiểm tra vai trò (String hoặc Entity)
			String role = user.getVaiTro() instanceof String ? user.getVaiTro().toString()
					: user.getVaiTro(); // nếu là entity thì lấy tên vai trò

			if (role != null && role.equalsIgnoreCase("admin")) {
				return "redirect:/admin/dashboard";
			} else if (role != null && role.equalsIgnoreCase("user")) {
				return "redirect:/products";
			} else {
				model.addAttribute("error", "Vai trò không được phép truy cập!");
				return "auth/login";
			}
		} else {
			model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
			return "auth/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/products";
	}

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new NguoiDung());
		return "auth/register";
	}

	@PostMapping("/register")
	public String doRegister(@ModelAttribute("user") @Valid NguoiDung user, BindingResult result,
			@RequestParam("confirmMatKhau") String confirmMatKhau, Model model, RedirectAttributes redirectAttributes) {

		if (result.hasErrors())
			return "auth/register";

		if (!user.getMatKhau().equals(confirmMatKhau)) {
			model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
			return "auth/register";
		}

		if (nguoiDungService.existsByTaiKhoan(user.getTaiKhoan())) {
			model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
			return "auth/register";
		}

		user.setVaiTro("USER");
		nguoiDungService.save(user);

		redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
		return "redirect:/login";
	}

	@GetMapping("/check-username")
	@ResponseBody
	public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("taiKhoan") String taiKhoan) {
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", nguoiDungService.existsByTaiKhoan(taiKhoan));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/forgot-password")
	public String showForgotPasswordPage(Model model) {
		model.addAttribute("user", new NguoiDung());
		return "auth/forgot-password";
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@ModelAttribute("user") NguoiDung user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors())
			return "auth/forgot-password";

		NguoiDung foundUser = nguoiDungService.findByTaiKhoan(user.getTaiKhoan()).orElse(null);
		if (foundUser == null) {
			model.addAttribute("error", "Tên đăng nhập không tồn tại!");
			return "auth/forgot-password";
		}

		foundUser.setMatKhau(user.getMatKhau());
		nguoiDungService.save(foundUser);

		redirectAttributes.addFlashAttribute("successMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
		return "redirect:/login";
	}
}
