package controller;

import entity.Ban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.BanService;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Controller
@RequestMapping("/ban")
public class BanController {
    
    @Autowired
    private BanService banService;
    
    // Trang danh sách phiếu bán
    @GetMapping
    public String listBan(Model model) {
        System.out.println("=== GET /ban START ===");
        List<Ban> banList = banService.getAllBan();
        System.out.println("Found " + banList.size() + " ban records");
        model.addAttribute("banList", banList);
        model.addAttribute("newBan", new Ban());
        System.out.println("Returning ban/index template");
        System.out.println("=== GET /ban END ===");
        return "ban/index";
    }
    
    // Trang thêm mới phiếu bán
    @GetMapping("/new")
    public String newBan(Model model) {
        Ban ban = new Ban();
        // Set default date to current time
        ban.setNgayBan(LocalDateTime.now());
        model.addAttribute("ban", ban);
        return "ban/new";
    }
    
    // Trang tìm kiếm phiếu bán
    @GetMapping("/search")
    public String searchBan(Model model) {
        List<Ban> banList = banService.getAllBan();
        model.addAttribute("banList", banList);
        return "ban/search";
    }
    
    // Xem chi tiết phiếu bán
    @GetMapping("/{id}")
    public String viewBan(@PathVariable Long id, Model model) {
        Ban ban = banService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bán"));
        model.addAttribute("ban", ban);
        return "ban/detail";
    }
    
    // Tạo mới phiếu bán
    @PostMapping
    public String createBan(@ModelAttribute Ban ban, 
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
            
            banService.createBan(ban);
            System.out.println("Ban successfully saved!");
            return "redirect:/ban";
            
        } catch (Exception e) {
            System.err.println("Error creating ban: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/ban?error=save_failed";
        } finally {
            System.out.println("=== CREATE BAN DEBUG END ===");
        }
    }
    
    // Trang chỉnh sửa phiếu bán
    @GetMapping("/{id}/edit")
    public String editBan(@PathVariable Long id, Model model) {
        Ban ban = banService.getBanById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu bán"));
        model.addAttribute("ban", ban);
        return "ban/edit";
    }
    
    // Cập nhật đơn bán hàng
    @PostMapping("/{id}")
    public String updateBan(@PathVariable Long id, @ModelAttribute Ban ban,
                           @RequestParam(value = "ngayBan", required = false) String ngayBanStr) {
        System.out.println("=== POST /ban/" + id + " START ===");
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
            
            Ban updated = banService.updateBan(id, ban);
            if (updated != null) {
                System.out.println("Ban updated successfully with ID: " + updated.getBanId());
                System.out.println("Redirecting to /ban");
                return "redirect:/ban";
            } else {
                System.out.println("ERROR: Ban not found with ID: " + id);
                return "error/404";
            }
        } catch (Exception e) {
            System.out.println("ERROR updating ban: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /ban/" + id + " END ===");
        }
    }    // Xóa phiếu bán
    @PostMapping("/{id}/delete")
    public String deleteBan(@PathVariable Long id) {
        System.out.println("=== POST /ban/" + id + "/delete START ===");
        
        try {
            boolean deleted = banService.deleteBan(id);
            if (deleted) {
                System.out.println("Ban deleted successfully with ID: " + id);
                System.out.println("Redirecting to /ban");
                return "redirect:/ban";
            } else {
                System.out.println("ERROR: Ban not found with ID: " + id);
                return "error/404";
            }
        } catch (Exception e) {
            System.out.println("ERROR deleting ban: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /ban/" + id + "/delete END ===");
        }
    }
}
