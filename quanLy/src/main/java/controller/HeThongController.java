package controller;

import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.HeThongService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/heThong")
public class HeThongController {

    @Autowired
    private HeThongService heThongService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("tongPhienActive", heThongService.getPhienDangNhapActive().size());
        model.addAttribute("tongNhatKy", heThongService.getAllNhatKy().size());
        model.addAttribute("tongSaoLuu", heThongService.getAllSaoLuu().size());
        model.addAttribute("tongCauHinh", heThongService.getAllCauHinh().size());
        return "heThong/index";
    }

    // ===== 1. Bảo mật =====

    @GetMapping("/bao-mat")
    public String baoMat(Model model) {
        model.addAttribute("phienList", heThongService.getAllPhienDangNhap());
        model.addAttribute("phienActiveList", heThongService.getPhienDangNhapActive());
        return "heThong/baoMat";
    }

    @PostMapping("/bao-mat/huy-phien/{id}")
    public String huyPhien(@PathVariable Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        heThongService.huyPhienDangNhap(id);
        heThongService.ghiNhatKy("BAO_MAT", "Hủy phiên đăng nhập #" + id,
                username, request.getRemoteAddr(), null);
        return "redirect:/heThong/bao-mat?success=1";
    }

    @PostMapping("/bao-mat/doi-mat-khau-admin")
    public String doiMatKhauAdmin(@RequestParam("username") String username,
                                   @RequestParam("matKhauMoi") String matKhauMoi,
                                   HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        heThongService.ghiNhatKy("BAO_MAT", "Đặt lại mật khẩu cho " + username,
                auth.getName(), request.getRemoteAddr(), null);
        return "redirect:/heThong/bao-mat?success=1";
    }

    // ===== 2. Nhật ký hoạt động =====

    @GetMapping("/nhat-ky")
    public String nhatKy(@RequestParam(value = "loai", required = false) String loai, Model model) {
        List<NhatKyHoatDong> nhatKyList;
        if (loai != null && !loai.isEmpty()) {
            nhatKyList = heThongService.getNhatKyByLoai(loai);
        } else {
            nhatKyList = heThongService.getAllNhatKy();
        }
        model.addAttribute("nhatKyList", nhatKyList);
        model.addAttribute("selectedLoai", loai);
        return "heThong/nhatKy";
    }

    // ===== 3. Sao lưu & phục hồi =====

    @GetMapping("/sao-luu")
    public String saoLuu(Model model) {
        model.addAttribute("saoLuuList", heThongService.getAllSaoLuu());
        return "heThong/saoLuu";
    }

    @PostMapping("/sao-luu/tao-moi")
    public String taoSaoLuu(@RequestParam(value = "loai", defaultValue = "MANUAL") String loai,
                            HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String fileName = heThongService.taoSaoLuu(loai);
            heThongService.ghiNhatKy("SAO_LUU", "Tạo bản sao lưu: " + fileName,
                    auth.getName(), request.getRemoteAddr(), null);
            return "redirect:/heThong/sao-luu?success=1";
        } catch (Exception e) {
            return "redirect:/heThong/sao-luu?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/sao-luu/xoa/{id}")
    public String xoaSaoLuu(@PathVariable Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        heThongService.xoaSaoLuu(id);
        heThongService.ghiNhatKy("SAO_LUU", "Xóa bản sao lưu #" + id,
                auth.getName(), request.getRemoteAddr(), null);
        return "redirect:/heThong/sao-luu?success=1";
    }

    @PostMapping("/sao-luu/khoi-phuc/{id}")
    public String khoiPhuc(@PathVariable Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            heThongService.khoiPhucDuLieu(id);
            heThongService.ghiNhatKy("SAO_LUU", "Khôi phục dữ liệu từ bản sao lưu #" + id,
                    auth.getName(), request.getRemoteAddr(), null);
            return "redirect:/heThong/sao-luu?success=1";
        } catch (Exception e) {
            return "redirect:/heThong/sao-luu?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

    // ===== 4. Cấu hình hệ thống =====

    @GetMapping("/cau-hinh")
    public String cauHinh(Model model) {
        model.addAttribute("cauHinhList", heThongService.getAllCauHinh());
        model.addAttribute("cauHinhGmailList", heThongService.getCauHinhByNhom("GMAIL"));
        model.addAttribute("cauHinhKhacList", heThongService.getCauHinhByNhom("KHAC"));
        return "heThong/cauHinh";
    }

    @PostMapping("/cau-hinh/luu")
    public String luuCauHinh(@RequestParam("id") Long id,
                             @RequestParam("giaTri") String giaTri,
                             HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        heThongService.getCauHinhById(id).ifPresentOrElse(cfg -> {
            String oldValue = cfg.getGiaTri();
            cfg.setGiaTri(giaTri);
            heThongService.saveCauHinh(cfg);
            heThongService.ghiNhatKy("CAU_HINH", "Cập nhật cấu hình " + cfg.getMa() + ": " + oldValue + " -> " + giaTri,
                    auth.getName(), request.getRemoteAddr(), null);
        }, () -> {
            throw new RuntimeException("Không tìm thấy cấu hình");
        });
        return "redirect:/heThong/cau-hinh?success=1";
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_SYSTEM_ADMIN".equals(a.getAuthority()));
    }
}
