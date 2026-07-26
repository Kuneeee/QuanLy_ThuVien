package service;

import entity.DocGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.DocGiaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class LibraryUserDetailsService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private DocGiaRepository docGiaRepository;

    private static final Map<String, DemoAccount> DEMO_ACCOUNTS = Map.of(
            "admin", new DemoAccount("admin", "admin123", "SYSTEM_ADMIN"),
            "thuthu", new DemoAccount("thuthu", "thuthu123", "ADMIN"),
            "giaovien", new DemoAccount("giaovien", "giaovien123", "TEACHER"),
            "sinhvien", new DemoAccount("sinhvien", "sinhvien123", "STUDENT")
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DemoAccount demoAccount = DEMO_ACCOUNTS.get(username);
        if (demoAccount != null) {
            return User.withUsername(demoAccount.username())
                    .password(passwordEncoder.encode(demoAccount.password()))
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + demoAccount.role())))
                    .build();
        }

        Optional<DocGia> docGiaOptional = docGiaRepository.findByTaiKhoan(username);
        if (docGiaOptional.isEmpty()) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
        }

        DocGia docGia = docGiaOptional.get();
        String role = resolveRole(docGia);
        return User.withUsername(docGia.getTaiKhoan())
                .password(docGia.getMatKhau())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
                .build();
    }

    private String resolveRole(DocGia docGia) {
        String quyenHan = docGia.getQuyenHan();
        if (quyenHan == null || quyenHan.isBlank()) {
            quyenHan = docGia.getLoaiKhachHang();
        }
        if (quyenHan == null) {
            return "STUDENT";
        }

        String normalized = quyenHan.toLowerCase();
        if (normalized.contains("thủ") || normalized.contains("thu") || normalized.contains("librarian")) {
            return "ADMIN";
        }
        if (normalized.contains("giáo") || normalized.contains("giao")) {
            return "TEACHER";
        }
        return "STUDENT";
    }

    private record DemoAccount(String username, String password, String role) {
    }
}