package config;

import entity.PhienDangNhap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import service.HeThongService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private HeThongService heThongService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        String ip = request.getRemoteAddr();
        LocalDateTime expiry = LocalDateTime.now().plusHours(8);

        PhienDangNhap phien = new PhienDangNhap(username, ip, expiry);
        heThongService.savePhienDangNhap(phien);
        heThongService.ghiNhatKy("LOGIN", "Đăng nhập hệ thống", username, ip, null);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> ("ROLE_SYSTEM_ADMIN").equals(authority.getAuthority()));
        response.sendRedirect(isAdmin ? "/heThong" : "/muonTra");
    }
}
