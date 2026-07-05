package controller;

import entity.HangHoa;
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
import service.HangHoaService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/hanghoa")
public class HangHoaController {
    
    @Autowired
    private HangHoaService hangHoaService;
    
    // Trang danh sách hàng hóa
    @GetMapping
    public String listHangHoa(Model model) {
        List<HangHoa> hangHoaList = hangHoaService.getAllHangHoa();
        System.out.println("=== HANGHOA CONTROLLER DEBUG ===");
        System.out.println("Total HangHoa found: " + hangHoaList.size());
        for (int i = 0; i < hangHoaList.size(); i++) {
            HangHoa hh = hangHoaList.get(i);
            System.out.println((i+1) + ". " + hh.getHanghoaID() + " - " + hh.getTenHangHoa());
        }
        System.out.println("===============================");
        model.addAttribute("hangHoaList", hangHoaList);
        model.addAttribute("newHangHoa", new HangHoa());
        return "hanghoa/index";
    }
    
    // Trang thêm mới hàng hóa
    @GetMapping("/new")
    public String newHangHoa(Model model) {
        model.addAttribute("hangHoa", new HangHoa());
        return "hanghoa/new";
    }
    
    // Xem chi tiết hàng hóa
    @GetMapping("/{id}")
    public String viewHangHoa(@PathVariable String id, Model model) {
        HangHoa hangHoa = hangHoaService.getHangHoaById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa"));
        model.addAttribute("hangHoa", hangHoa);
        return "hanghoa/detail";
    }
    
    // Tạo mới hàng hóa
    @PostMapping
    public RedirectView createHangHoa(@ModelAttribute HangHoa hangHoa) {
        try {
            System.out.println("Creating HangHoa: " + hangHoa.toString());
            hangHoaService.createHangHoa(hangHoa);
            System.out.println("HangHoa created successfully, redirecting to /hanghoa");
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error creating HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa/new?error=true");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }
    
    // Trang chỉnh sửa hàng hóa
    @GetMapping("/{id}/edit")
    public String editHangHoa(@PathVariable String id, Model model) {
        HangHoa hangHoa = hangHoaService.getHangHoaById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa"));
        model.addAttribute("hangHoa", hangHoa);
        return "hanghoa/edit";
    }
    
    // Cập nhật hàng hóa
    @PostMapping("/{id}")
    public RedirectView updateHangHoa(@PathVariable String id, @ModelAttribute HangHoa hangHoa) {
        try {
            System.out.println("=== UPDATING HANGHOA ===");
            System.out.println("ID: " + id);
            System.out.println("Data received: " + hangHoa.toString());
            
            HangHoa updated = hangHoaService.updateHangHoa(id, hangHoa);
            if (updated != null) {
                System.out.println("HangHoa updated successfully: " + updated.toString());
            } else {
                System.out.println("ERROR: HangHoa update failed - not found");
            }
            
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error updating HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa/" + id + "/edit?error=true");
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
            
            boolean deleted = hangHoaService.deleteHangHoa(id);
            if (deleted) {
                System.out.println("HangHoa deleted successfully: " + id);
            } else {
                System.out.println("ERROR: HangHoa delete failed - not found: " + id);
            }
            
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa");
            redirectView.setContextRelative(true);
            return redirectView;
        } catch (Exception e) {
            System.err.println("Error deleting HangHoa: " + e.getMessage());
            e.printStackTrace();
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl("/hanghoa?error=true");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }
}
