package controller;

import entity.ThongBaoMau;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.ThongBaoThongKeService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/thongBao")
public class ThongBaoThongKeController {

    @Autowired
    private ThongBaoThongKeService thongBaoThongKeService;

    @GetMapping
    public String index(@RequestParam(value = "ky", required = false, defaultValue = "CA_NAM") String ky,
                        @RequestParam(value = "namHoc", required = false, defaultValue = "") String namHoc,
                        Model model) {
        loadDashboard(model, new ThongBaoMau(), ky, namHoc, null);
        return "thongBao/index";
    }

    @GetMapping("/chinh-sua/{id}")
    public String chinhSua(@PathVariable Long id,
                           @RequestParam(value = "ky", required = false, defaultValue = "CA_NAM") String ky,
                           @RequestParam(value = "namHoc", required = false, defaultValue = "") String namHoc,
                           Model model) {
        ThongBaoMau mau = thongBaoThongKeService.getMauById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mẫu thông báo"));
        loadDashboard(model, mau, ky, namHoc, null);
        return "thongBao/index";
    }

    @PostMapping("/mau")
    public String luuMau(ThongBaoMau mau,
                         @RequestParam(value = "ky", required = false, defaultValue = "CA_NAM") String ky,
                         @RequestParam(value = "namHoc", required = false, defaultValue = "") String namHoc,
                         Model model) {
        try {
            thongBaoThongKeService.saveMau(mau);
            return "redirect:/thongBao?success=1&ky=" + ky + "&namHoc=" + thongBaoThongKeService.resolveNamHoc(namHoc);
        } catch (Exception e) {
            loadDashboard(model, mau, ky, namHoc, e.getMessage());
            return "thongBao/index";
        }
    }

    @PostMapping("/xoa/{id}")
    public String xoaMau(@PathVariable Long id,
                         @RequestParam(value = "ky", required = false, defaultValue = "CA_NAM") String ky,
                         @RequestParam(value = "namHoc", required = false, defaultValue = "") String namHoc) {
        thongBaoThongKeService.deleteMau(id);
        return "redirect:/thongBao?success=1&ky=" + ky + "&namHoc=" + thongBaoThongKeService.resolveNamHoc(namHoc);
    }

    @GetMapping("/xuat-bao-cao")
    public ResponseEntity<ByteArrayResource> xuatBaoCao(
            @RequestParam(value = "ky", required = false, defaultValue = "CA_NAM") String ky,
            @RequestParam(value = "namHoc", required = false, defaultValue = "") String namHoc) {
        String csv = thongBaoThongKeService.xuatBaoCaoCsv(ky, namHoc);
        ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDisposition(ContentDisposition.attachment().filename("bao-cao-thong-ke.csv").build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private void loadDashboard(Model model, ThongBaoMau mauForm, String ky, String namHoc, String error) {
        List<ThongBaoMau> mauList = thongBaoThongKeService.getAllMau();
        List<entity.MuonTra> muonTraList = thongBaoThongKeService.getMuonTraTheoKy(ky, namHoc);
        List<entity.ViPhamPhong> viPhamList = thongBaoThongKeService.getViPhamTheoKy(ky, namHoc);

        model.addAttribute("mauList", mauList);
        model.addAttribute("mauForm", mauForm);
        model.addAttribute("ky", ky);
        model.addAttribute("namHoc", thongBaoThongKeService.resolveNamHoc(namHoc));
        model.addAttribute("kyLabel", thongBaoThongKeService.resolveKyLabel(ky));
        model.addAttribute("canManage", isAdmin());
        model.addAttribute("tongMauThongBao", mauList.size());
        model.addAttribute("tongMauBaoCao", mauList.stream().filter(item -> isLoai(item, "báo cáo")).count());
        model.addAttribute("tongThongBaoThuVien", mauList.stream().filter(item -> isLoai(item, "thư viện")).count());
        model.addAttribute("tongThongBaoHanTra", mauList.stream().filter(item -> isLoai(item, "hạn trả")).count());
        model.addAttribute("tongThongBaoSuKien", mauList.stream().filter(item -> isLoai(item, "sự kiện")).count());
        model.addAttribute("muonTraList", muonTraList);
        model.addAttribute("viPhamList", viPhamList);
        model.addAttribute("thongKeTrangThai", thongBaoThongKeService.thongKeTrangThaiMuonTra(muonTraList));
        model.addAttribute("thongKeViPhamNguoiDung", thongBaoThongKeService.thongKeViPhamNguoiDung(viPhamList));
        model.addAttribute("tongMuonDangMuon", thongBaoThongKeService.demMuonDangMuon(muonTraList));
        model.addAttribute("tongMuonDaTra", thongBaoThongKeService.demMuonDaTra(muonTraList));
        model.addAttribute("tongMuonQuaHan", thongBaoThongKeService.demMuonQuaHan(muonTraList));
        model.addAttribute("tongViPham", thongBaoThongKeService.demViPham(viPhamList));
        model.addAttribute("error", error);
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private boolean isLoai(ThongBaoMau mau, String keyword) {
        return mau.getLoaiThongBao() != null && mau.getLoaiThongBao().toLowerCase().contains(keyword);
    }
}