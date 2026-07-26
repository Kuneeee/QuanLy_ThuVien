package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.DocGiaRepository;
import entity.DocGia;

import java.util.Optional;

@Controller
public class PasswordController {

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocGiaRepository docGiaRepository;

    @GetMapping("/doi-mat-khau")
    public String changePasswordForm() {
        return "auth/change-password";
    }

    @PostMapping("/doi-mat-khau")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getName() == null || "anonymousUser".equals(authentication.getName())) {
            model.addAttribute("error", "Bạn cần đăng nhập trước khi đổi mật khẩu.");
            return "auth/change-password";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        if (isAdmin) {
            model.addAttribute("error", "Tài khoản admin dùng mật khẩu mẫu cố định. Hãy liên hệ quản trị hệ thống nếu cần thay đổi.");
            return "auth/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            return "auth/change-password";
        }

        try {
            String username = authentication.getName();
            if (userDetailsManager.userExists(username)) {
                UserDetails existingUser = userDetailsManager.loadUserByUsername(username);
                if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                    model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
                    return "auth/change-password";
                }

                UserDetails updatedUser = User.withUsername(existingUser.getUsername())
                        .password(passwordEncoder.encode(newPassword))
                        .authorities(existingUser.getAuthorities())
                        .build();
                userDetailsManager.updateUser(updatedUser);
            } else {
                Optional<DocGia> docGiaOptional = docGiaRepository.findByTaiKhoan(username);
                if (docGiaOptional.isEmpty()) {
                    model.addAttribute("error", "Không tìm thấy tài khoản đang đăng nhập.");
                    return "auth/change-password";
                }

                DocGia docGia = docGiaOptional.get();
                if (!passwordEncoder.matches(currentPassword, docGia.getMatKhau())) {
                    model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
                    return "auth/change-password";
                }

                docGia.setMatKhau(passwordEncoder.encode(newPassword));
                docGiaRepository.save(docGia);
            }
            model.addAttribute("success", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại nếu cần.");
        } catch (Exception ex) {
            model.addAttribute("error", "Không thể đổi mật khẩu. Kiểm tra lại mật khẩu hiện tại.");
        }
        return "auth/change-password";
    }

    @GetMapping("/quen-mat-khau")
    public String forgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/quen-mat-khau")
    public String forgotPassword(
            @RequestParam String username,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            return "auth/forgot-password";
        }

        try {
            UserDetails existingUser = userDetailsManager.loadUserByUsername(username);
            boolean allowedRole = existingUser.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_TEACHER".equals(authority.getAuthority()) || "ROLE_STUDENT".equals(authority.getAuthority()));
            if (!allowedRole) {
                model.addAttribute("error", "Chức năng quên mật khẩu hiện chỉ áp dụng cho tài khoản giáo viên và sinh viên.");
                return "auth/forgot-password";
            }

                UserDetails updatedUser = User.withUsername(existingUser.getUsername())
                    .password(passwordEncoder.encode(newPassword))
                    .authorities(existingUser.getAuthorities())
                    .build();
            userDetailsManager.updateUser(updatedUser);
            model.addAttribute("success", "Đặt lại mật khẩu thành công. Hãy đăng nhập lại bằng mật khẩu mới.");
        } catch (Exception ex) {
            model.addAttribute("error", "Không tìm thấy tài khoản hoặc không thể đặt lại mật khẩu.");
        }
        return "auth/forgot-password";
    }
}