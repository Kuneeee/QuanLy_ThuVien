package controller;

import entity.MuonTra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.MuonTraService;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Controller
@RequestMapping("/muonTra")
public class MuonTraController {
    
    @Autowired
    private MuonTraService muonTraService;
    
    // Trang danh sách phiếu bán
    @GetMapping
    public String listBan(Model model) {
        System.out.println("=== GET /muonTra START ===");
        List<MuonTra> banList = muonTraService.getAllBan();
        System.out.println("Found " + banList.size() + " ban records");
        model.addAttribute("banList", banList);
        model.addAttribute("newBan", new MuonTra());
        System.out.println("Returning muonTra/index template");
        System.out.println("=== GET /muonTra END ===");
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
    
    // Trang tìm kiếm phiếu bán
    @GetMapping("/search")
    public String searchBan(Model model) {
        List<MuonTra> banList = muonTraService.getAllBan();
        model.addAttribute("banList", banList);
        return "muonTra/search";
    }
    
    // Xem chi tiết phiếu bán
    @GetMapping("/{id}")
    public String viewMuonTra(@PathVariable Long id, Model model) {
        MuonTra ban = muonTraService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bán"));
        model.addAttribute("ban", ban);
        return "muonTra/detail";
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
