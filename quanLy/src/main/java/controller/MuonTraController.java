package controller;

import entity.MuonTra;
import entity.DocGia;
import entity.TaiLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import service.MuonTraService;
import service.DocGiaService;
import repository.TaiLieuRepository;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/muonTra")
public class MuonTraController {
    
    @Autowired
    private MuonTraService muonTraService;

    @Autowired
    private DocGiaService docGiaService;

    @Autowired
    private TaiLieuRepository taiLieuRepository;
    
    // Trang danh sách phiếu bán
    @GetMapping
    public String listBan(Model model) {
        List<MuonTra> banList = muonTraService.getAllBan();
        model.addAttribute("banList", banList);
        model.addAttribute("canManageLoans", canManageLoans());
        model.addAttribute("datTruocList", locTheoTrangThai(banList, "Đặt trước"));
        model.addAttribute("muonList", locTheoTrangThai(banList, "Đang mượn"));
        model.addAttribute("traSachList", locTheoTrangThai(banList, "Đã trả"));
        model.addAttribute("lichSuList", banList);
        model.addAttribute("datTruocForm", new MuonTra());
        model.addAttribute("muonForm", new MuonTra());
        model.addAttribute("traForm", new MuonTra());
        model.addAttribute("giaHanSoNgay", 7);
        return "muonTra/index";
    }
    
    // Trang thêm mới phiếu bán
    @GetMapping("/new")
    public String newBan(Model model) {
        MuonTra ban = new MuonTra();
        // Set default date to current time
        ban.setNgayBan(LocalDateTime.now());
        model.addAttribute("ban", ban);
        return "muonTra/new";
    }
    
    // Trang tìm kiếm/lịch sử mượn
    @GetMapping("/search")
    public String searchBan(@RequestParam(value = "maTaiLieu", required = false) String maTaiLieu,
                            Model model) {
        List<MuonTra> banList = muonTraService.getAllBan();
        model.addAttribute("banList", banList);
        model.addAttribute("maTaiLieu", maTaiLieu);
        if (maTaiLieu != null && !maTaiLieu.isBlank()) {
            model.addAttribute("historyList", muonTraService.getAllBan().stream()
                    .filter(item -> maTaiLieu.equalsIgnoreCase(item.getHangHoaID()))
                    .toList());
        } else {
            model.addAttribute("historyList", banList);
        }
        return "muonTra/search";
    }

    @PostMapping("/dat-truoc")
    public String taoYeuCauDatTruoc(@ModelAttribute("datTruocForm") MuonTra form) {
        try {
            if (form.getNgayBan() == null) {
                form.setNgayBan(LocalDateTime.now());
            }
            muonTraService.taoYeuCauDatTruoc(form);
            return "redirect:/muonTra?success=dat-truoc";
        } catch (Exception e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/muonTra?error=dat-truoc-failed";
        }
    }

    @PostMapping("/lap-phieu-muon")
    public String lapPhieuMuon(@ModelAttribute("muonForm") MuonTra form,
                               @RequestParam(value = "kiemTraTaiKhoan", required = false) String kiemTraTaiKhoan) {
        if (kiemTraTaiKhoan != null && !kiemTraTaiKhoan.isBlank()) {
            boolean taiKhoanHopLe = docGiaService.getCustomerById(kiemTraTaiKhoan).isPresent();
            boolean taiLieuHopLe = muonTraService.kiemTraTinhTrangTaiLieu(form.getHangHoaID());
            if (!taiKhoanHopLe || !taiLieuHopLe) {
                return "redirect:/muonTra?error=kiem-tra-that-bai";
            }
            DocGia docGia = docGiaService.getCustomerById(kiemTraTaiKhoan).orElse(null);
            if (docGia != null && (form.getTenKhachHang() == null || form.getTenKhachHang().isBlank())) {
                form.setTenKhachHang(docGia.getTenKhachHang());
            }
            form.setKhachHang(kiemTraTaiKhoan);
        }
        muonTraService.lapPhieuMuon(form);
        return "redirect:/muonTra?success=lap-phieu-muon";
    }

    @PostMapping("/{id}/tra-sach")
    public String ghiNhanTraSach(@PathVariable Long id,
                                 @RequestParam(value = "ngayTra", required = false) String ngayTraStr) {
        LocalDateTime ngayTra = null;
        if (ngayTraStr != null && !ngayTraStr.isBlank()) {
            ngayTra = LocalDateTime.parse(ngayTraStr);
        }
        muonTraService.ghiNhanTra(id, ngayTra);
        return "redirect:/muonTra/" + id;
    }

    @PostMapping("/tra-sach")
    public String ghiNhanTraSachNhanh(@RequestParam String id,
                                      @RequestParam(value = "ngayTra", required = false) String ngayTraStr) {
        LocalDateTime ngayTra = null;
        if (ngayTraStr != null && !ngayTraStr.isBlank()) {
            ngayTra = LocalDateTime.parse(ngayTraStr);
        }
        MuonTra phieuTra = muonTraService.ghiNhanTra(id, ngayTra);
        if (phieuTra == null || phieuTra.getBanId() == null) {
            return "redirect:/muonTra?error=tra-sach-failed";
        }
        return "redirect:/muonTra/" + phieuTra.getBanId();
    }

    @PostMapping("/{id}/gia-han")
    public String giaHanMuon(@PathVariable Long id, @RequestParam(value = "soNgayGiaHan", defaultValue = "7") int soNgayGiaHan) {
        muonTraService.giaHanThoiGianMuon(id, soNgayGiaHan);
        return "redirect:/muonTra/" + id;
    }
    
    // Xem chi tiết phiếu bán
    @GetMapping("/{id}")
    public String viewMuonTra(@PathVariable Long id, Model model) {
        MuonTra ban = muonTraService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bán"));
        TaiLieu taiLieu = ban.getHangHoaID() != null ? taiLieuRepository.findById(ban.getHangHoaID()).orElse(null) : null;
        BigDecimal giaNhapHienThi = ban.getGiaNhap();
        if ((giaNhapHienThi == null || giaNhapHienThi.compareTo(BigDecimal.ZERO) <= 0) && taiLieu != null && taiLieu.getGiaNhap() != null) {
            giaNhapHienThi = taiLieu.getGiaNhap();
        }
        BigDecimal giaBanHienThi = ban.getGiaBan();
        if (giaBanHienThi == null || giaBanHienThi.compareTo(BigDecimal.ZERO) <= 0) {
            giaBanHienThi = giaNhapHienThi;
        }
        BigDecimal tongTienHienThi = ban.getTongTien();
        if ((tongTienHienThi == null || tongTienHienThi.compareTo(BigDecimal.ZERO) <= 0)
                && giaBanHienThi != null && ban.getSoLuongBan() != null) {
            tongTienHienThi = giaBanHienThi.multiply(BigDecimal.valueOf(ban.getSoLuongBan()));
        }
        model.addAttribute("ban", ban);
        model.addAttribute("giaNhapHienThi", giaNhapHienThi);
        model.addAttribute("giaBanHienThi", giaBanHienThi);
        model.addAttribute("tongTienHienThi", tongTienHienThi);
        model.addAttribute("canManagePenalty", canManageLoans());
        model.addAttribute("soNgayMuon", muonTraService.tinhSoNgayMuon(ban));
        model.addAttribute("soNgayTre", muonTraService.tinhSoNgayTre(ban));
        model.addAttribute("lichSuMuon", muonTraService.getAllBan().stream()
            .filter(item -> item.getHangHoaID() != null && item.getHangHoaID().equalsIgnoreCase(ban.getHangHoaID()))
            .toList());
        return "muonTra/detail";
    }

    private List<MuonTra> locTheoTrangThai(List<MuonTra> danhSach, String trangThai) {
        return danhSach.stream()
                .filter(item -> item.getTrangThai() != null && item.getTrangThai().equalsIgnoreCase(trangThai))
                .collect(Collectors.toList());
    }

    private boolean canManageLoans() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
    
    // Tạo mới phiếu bán
    @PostMapping
    public String createMuonTra(@ModelAttribute MuonTra ban, 
                           @RequestParam(value = "ngayBan", required = false) String ngayBanStr) {
        try {
            System.out.println("=== CREATE BAN DEBUG START ===");
            System.out.println("Ban object received: " + ban);
            System.out.println("NgayBan string: " + ngayBanStr);
            
            // Handle date parsing manually if needed
            if (ban.getNgayBan() == null && ngayBanStr != null && !ngayBanStr.isEmpty()) {
                try {
                    // Try to parse the date string manually
                    LocalDateTime parsedDate = LocalDateTime.parse(ngayBanStr + ":00"); // Add seconds if missing
                    ban.setNgayBan(parsedDate);
                    System.out.println("Manually parsed date: " + parsedDate);
                } catch (Exception e) {
                    System.out.println("Date parsing failed, using current time: " + e.getMessage());
                    ban.setNgayBan(LocalDateTime.now());
                }
            }
            
            // Set required fields if they are null
            if (ban.getNgayBan() == null) {
                ban.setNgayBan(LocalDateTime.now());
                System.out.println("Set ngayBan to current time: " + ban.getNgayBan());
            }
            
            // Calculate tong tien if not set
            if (ban.getTongTien() == null && ban.getGiaBan() != null && ban.getSoLuongBan() != null) {
                ban.setTongTien(ban.getGiaBan().multiply(BigDecimal.valueOf(ban.getSoLuongBan())));
                System.out.println("Calculated tongTien: " + ban.getTongTien());
            }

            if (ban.getTrangThai() == null || ban.getTrangThai().isBlank()) {
                ban.setTrangThai("Đang mượn");
            }
            if (ban.getNgayHenTra() == null && ban.getNgayBan() != null) {
                ban.setNgayHenTra(ban.getNgayBan().plusDays(14));
            }
            
            // Debug gia nhap for CREATE
            System.out.println("GiaNhap from form: " + ban.getGiaNhap());
            
            System.out.println("Final Ban object before save: " + ban);
            
            muonTraService.createBan(ban);
            System.out.println("Ban successfully saved!");
            return "redirect:/muonTra";
            
        } catch (Exception e) {
            System.err.println("Error creating ban: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/muonTra?error=save_failed";
        } finally {
            System.out.println("=== CREATE BAN DEBUG END ===");
        }
    }
    
    // Trang chỉnh sửa phiếu bán
    @GetMapping("/{id}/edit")
    public String editMuonTra(@PathVariable Long id, Model model) {
        MuonTra ban = muonTraService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bán"));
        model.addAttribute("ban", ban);
        return "muonTra/edit";
    }
    
    // Cập nhật đơn bán hàng
    @PostMapping("/{id}")
    public String updateMuonTra(@PathVariable Long id, @ModelAttribute MuonTra ban,
                           @RequestParam(value = "ngayBan", required = false) String ngayBanStr) {
        System.out.println("=== POST /muonTra/" + id + " START ===");
        System.out.println("Received ban: " + ban.getBanCode());
        System.out.println("NgayBan from form: " + ngayBanStr);
        System.out.println("NgayBan from ban object: " + ban.getNgayBan());
        
        try {
            // Handle date parsing manually if needed
            if (ban.getNgayBan() == null && ngayBanStr != null && !ngayBanStr.isEmpty()) {
                try {
                    // Try to parse the date string manually
                    LocalDateTime parsedDate = LocalDateTime.parse(ngayBanStr + ":00"); // Add seconds if missing
                    ban.setNgayBan(parsedDate);
                    System.out.println("Manually parsed date: " + parsedDate);
                } catch (Exception e) {
                    System.out.println("Date parsing failed, using current time: " + e.getMessage());
                    ban.setNgayBan(LocalDateTime.now());
                }
            }
            
            // Set required fields if they are null
            if (ban.getNgayBan() == null) {
                ban.setNgayBan(LocalDateTime.now());
                System.out.println("Set ngayBan to current time: " + ban.getNgayBan());
            }
            
            // Always recalculate tong tien based on gia ban and so luong
            if (ban.getGiaBan() != null && ban.getSoLuongBan() != null) {
                BigDecimal calculatedTongTien = ban.getGiaBan().multiply(BigDecimal.valueOf(ban.getSoLuongBan()));
                ban.setTongTien(calculatedTongTien);
                System.out.println("Auto-calculated tongTien: " + ban.getGiaBan() + " × " + ban.getSoLuongBan() + " = " + calculatedTongTien);
            }
            
            // Debug gia nhap
            System.out.println("GiaNhap from form: " + ban.getGiaNhap());
            
            System.out.println("Final Ban object before update: " + ban);
            
            MuonTra updated = muonTraService.updateBan(id, ban);
            if (updated != null) {
                System.out.println("Ban updated successfully with ID: " + updated.getBanId());
                System.out.println("Redirecting to /muonTra");
                return "redirect:/muonTra";
            } else {
                System.out.println("ERROR: Ban not found with ID: " + id);
                return "error/404";
            }
        } catch (Exception e) {
            System.out.println("ERROR updating ban: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /muonTra/" + id + " END ===");
        }
    }    // Xóa phiếu bán
    @PostMapping("/{id}/delete")
    public String deleteBan(@PathVariable Long id) {
        System.out.println("=== POST /muonTra/" + id + "/delete START ===");
        
        try {
            boolean deleted = muonTraService.deleteBan(id);
            if (deleted) {
                System.out.println("Ban deleted successfully with ID: " + id);
                System.out.println("Redirecting to /muonTra");
                return "redirect:/muonTra";
            } else {
                System.out.println("ERROR: Ban not found with ID: " + id);
                return "error/404";
            }
        } catch (Exception e) {
            System.out.println("ERROR deleting ban: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /muonTra/" + id + "/delete END ===");
        }
    }
}
