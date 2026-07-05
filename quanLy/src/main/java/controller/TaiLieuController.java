package controller;

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
import org.springframework.web.servlet.view.RedirectView;
import service.TaiLieuService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/taiLieu")
public class TaiLieuController {
    
    @Autowired
    private TaiLieuService taiLieuService;
    
    // Trang danh sách hàng hóa
    @GetMapping
    public String listTaiLieu(Model model) {
        List<TaiLieu> hangHoaList = taiLieuService.getAllHangHoa();
        System.out.println("=== HANGHOA CONTROLLER DEBUG ===");
        System.out.println("Total HangHoa found: " + hangHoaList.size());
        for (int i = 0; i < hangHoaList.size(); i++) {
            TaiLieu hh = hangHoaList.get(i);
            System.out.println((i+1) + ". " + hh.getHanghoaID() + " - " + hh.getTenHangHoa());
        }
        System.out.println("===============================");
        model.addAttribute("hangHoaList", hangHoaList);
        model.addAttribute("newHangHoa", new TaiLieu());
        return "taiLieu/index";
    }
    
    // Trang thêm mới hàng hóa
    @GetMapping("/new")
    public String newTaiLieu(Model model) {
        model.addAttribute("hangHoa", new TaiLieu());
        return "taiLieu/new";
    }
    
    // Xem chi tiết hàng hóa
    @GetMapping("/{id}")
    public String viewTaiLieu(@PathVariable String id, Model model) {
        TaiLieu hangHoa = taiLieuService.getHangHoaById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa"));
        model.addAttribute("hangHoa", hangHoa);
        return "taiLieu/detail";
    }
    
    // Tạo mới hàng hóa
    @PostMapping
    public RedirectView createTaiLieu(@ModelAttribute TaiLieu hangHoa) {
        try {
            System.out.println("Creating HangHoa: " + hangHoa.toString());
            taiLieuService.createHangHoa(hangHoa);
            System.out.println("TaiLieu created successfully, redirecting to /taiLieu");
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error creating HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu/new?error=true");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }
    
    // Trang chỉnh sửa hàng hóa
    @GetMapping("/{id}/edit")
    public String editTaiLieu(@PathVariable String id, Model model) {
        TaiLieu hangHoa = taiLieuService.getHangHoaById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa"));
        model.addAttribute("hangHoa", hangHoa);
        return "taiLieu/edit";
    }
    
    // Cập nhật hàng hóa
    @PostMapping("/{id}")
    public RedirectView updateTaiLieu(@PathVariable String id, @ModelAttribute TaiLieu hangHoa) {
        try {
            System.out.println("=== UPDATING HANGHOA ===");
            System.out.println("ID: " + id);
            System.out.println("Data received: " + hangHoa.toString());
            
            TaiLieu updated = taiLieuService.updateHangHoa(id, hangHoa);
            if (updated != null) {
                System.out.println("HangHoa updated successfully: " + updated.toString());
            } else {
                System.out.println("ERROR: HangHoa update failed - not found");
            }
            
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error updating HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu/" + id + "/edit?error=true");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }
    
    // Xóa hàng hóa
    @PostMapping("/{id}/delete")
    public RedirectView deleteHangHoa(@PathVariable String id) {
        try {
            System.out.println("=== DELETING HANGHOA ===");
            System.out.println("ID to delete: " + id);
            
            boolean deleted = taiLieuService.deleteHangHoa(id);
            if (deleted) {
                System.out.println("HangHoa deleted successfully: " + id);
            } else {
                System.out.println("ERROR: HangHoa delete failed - not found: " + id);
            }
            
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error deleting HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/taiLieu?error=true");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }
}
