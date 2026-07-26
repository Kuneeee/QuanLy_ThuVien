package controller;

import entity.DatPhong;
import entity.MuonTra;
import entity.LichTrongPhong;
import entity.ViPhamPhong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.MuonTraService;
import service.PhongService;

import java.util.List;

@Controller
@RequestMapping("/phong")
public class PhongController {

    @Autowired
    private PhongService phongService;

    @Autowired
    private MuonTraService muonTraService;

    @GetMapping
    public String index(Model model) {
        loadDashboard(model, new DatPhong(), new LichTrongPhong(), new ViPhamPhong(), null);
        return "phong/index";
    }

    @PostMapping("/dat-phong")
    public String datPhong(DatPhong datPhong, Model model) {
        try {
            phongService.datPhong(datPhong);
            return "redirect:/phong?success=dat-phong";
        } catch (Exception e) {
            loadDashboard(model, datPhong, new LichTrongPhong(), new ViPhamPhong(), "Không thể tạo yêu cầu đặt phòng. Vui lòng kiểm tra lại dữ liệu.");
            return "phong/index";
        }
    }

    @PostMapping("/lich-trong")
    public String themLichTrong(LichTrongPhong lichTrongPhong, Model model) {
        try {
            phongService.themLichTrong(lichTrongPhong);
            return "redirect:/phong?success=them-lich-trong";
        } catch (Exception e) {
            loadDashboard(model, new DatPhong(), lichTrongPhong, new ViPhamPhong(), e.getMessage());
            return "phong/index";
        }
    }

    @PostMapping("/huy/{id}")
    public String huyDatPhong(@PathVariable Long id) {
        phongService.huyDatPhong(id);
        return "redirect:/phong?success=huy-dat-phong";
    }

    @PostMapping("/gia-han/{id}")
    public String giaHanDatPhong(@PathVariable Long id, @RequestParam(value = "soPhutGiaHan", defaultValue = "60") int soPhutGiaHan) {
        phongService.giaHanDatPhong(id, soPhutGiaHan);
        return "redirect:/phong?success=gia-han";
    }

    @PostMapping("/vi-pham")
    public String ghiNhanViPham(ViPhamPhong viPhamPhong, Model model) {
        try {
            phongService.ghiNhanViPham(viPhamPhong);
            return "redirect:/phong?success=vi-pham";
        } catch (Exception e) {
            loadDashboard(model, new DatPhong(), new LichTrongPhong(), viPhamPhong, e.getMessage());
            return "phong/index";
        }
    }

    @GetMapping("/vi-pham/tu-muon-tra/{id}")
    public String taoViPhamTuMuonTra(@PathVariable Long id, Model model) {
        MuonTra ban = muonTraService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));
        long soNgayTre = muonTraService.tinhSoNgayTre(ban);
        if (soNgayTre <= 0) {
            return "redirect:/muonTra/" + id + "?error=khong-phai-qua-han";
        }

        ViPhamPhong viPhamPhong = phongService.taoViPhamTuMuonTra(ban);
        loadDashboard(model, new DatPhong(), new LichTrongPhong(), viPhamPhong, null);
        model.addAttribute("prefillMessage", "Đã chuyển dữ liệu từ phiếu mượn quá hạn sang form vi phạm.");
        return "phong/index";
    }

    @GetMapping("/bien-lai/{id}")
    public String bienLai(@PathVariable Long id, Model model) {
        ViPhamPhong viPhamPhong = phongService.getViPhamById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biên lai"));
        model.addAttribute("viPham", viPhamPhong);
        model.addAttribute("canManagePenalty", canManagePenalty());
        return "phong/bien-lai";
    }

    @PostMapping("/vi-pham/xoa/{id}")
    public String xoaViPham(@PathVariable Long id) {
        phongService.xoaViPham(id);
        return "redirect:/phong?success=xoa-vi-pham";
    }

    private void loadDashboard(Model model, DatPhong datPhong, LichTrongPhong lichTrongPhong, ViPhamPhong viPhamPhong, String error) {
        List<DatPhong> datPhongList = phongService.getAllDatPhong();
        List<LichTrongPhong> lichTrongList = phongService.getAllLichTrong();
        List<ViPhamPhong> viPhamList = phongService.getAllViPham();
        model.addAttribute("datPhongList", datPhongList);
        model.addAttribute("lichTrongList", lichTrongList);
        model.addAttribute("viPhamList", viPhamList);
        model.addAttribute("datPhongForm", datPhong);
        model.addAttribute("lichTrongForm", lichTrongPhong);
        model.addAttribute("viPhamForm", viPhamPhong);
        model.addAttribute("canManagePenalty", canManagePenalty());
        model.addAttribute("canManageSchedule", isAdmin());
        model.addAttribute("error", error);
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean canManagePenalty() {
        return isAdmin();
    }
}
