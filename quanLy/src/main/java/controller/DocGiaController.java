package controller;

import entity.DocGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.DocGiaService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/docGia")
public class DocGiaController {
    
    @Autowired
    private DocGiaService docGiaService;
    
    // Trang danh sách khách hàng
    @GetMapping
    public String listCustomers(Model model) {
        System.out.println("=== GET /docGia START ===");
        List<DocGia> customers = docGiaService.getAllCustomers();
        System.out.println("Found " + customers.size() + " customers");
        model.addAttribute("khachHangList", customers);
        model.addAttribute("customers", customers);
        model.addAttribute("newCustomer", new DocGia());
        System.out.println("Returning docGia/index template");
        System.out.println("=== GET /docGia END ===");
        return "docGia/index";
    }
    
    // Trang thêm mới khách hàng
    @GetMapping("/new")
    public String newCustomer(Model model) {
        model.addAttribute("khachHang", new DocGia());
        return "docGia/new";
    }
    
    // Xem chi tiết khách hàng
    @GetMapping("/{id}")
    public String viewDocGia(@PathVariable String id, Model model) {
        DocGia khachHang = docGiaService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        model.addAttribute("khachHang", khachHang);
        return "docGia/detail";
    }
    
    // Tạo mới khách hàng
    @PostMapping
    public String createDocGia(@ModelAttribute DocGia khachHang) {
        System.out.println("=== POST /docGia START ===");
        System.out.println("Received customer: " + khachHang.getTenKhachHang());
        
        try {
            DocGia created = docGiaService.createCustomer(khachHang);
            System.out.println("Customer created with ID: " + created.getKhachHangId());
            System.out.println("Redirecting to /docGia");
            return "redirect:/docGia";
        } catch (Exception e) {
            System.out.println("ERROR creating customer: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /docGia END ===");
        }
    }
    
    // Trang chỉnh sửa khách hàng
    @GetMapping("/{id}/edit")
    public String editDocGia(@PathVariable String id, Model model) {
        DocGia khachHang = docGiaService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        model.addAttribute("khachHang", khachHang);
        return "docGia/edit";
    }
    
    // Cập nhật khách hàng
    @PostMapping("/{id}")
    public String updateDocGia(@PathVariable String id, @ModelAttribute DocGia khachHang) {
        docGiaService.updateCustomer(id, khachHang);
        return "redirect:/docGia";
    }
    
    // Xóa khách hàng
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable String id) {
        docGiaService.deleteCustomer(id);
        return "redirect:/docGia";
    }
}
