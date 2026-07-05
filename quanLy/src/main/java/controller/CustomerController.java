package controller;

import entity.KhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.CustomerService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    // Trang danh sách khách hàng
    @GetMapping
    public String listCustomers(Model model) {
        System.out.println("=== GET /customers START ===");
        List<KhachHang> customers = customerService.getAllCustomers();
        System.out.println("Found " + customers.size() + " customers");
        model.addAttribute("khachHangList", customers);
        model.addAttribute("customers", customers);
        model.addAttribute("newCustomer", new KhachHang());
        System.out.println("Returning customers/index template");
        System.out.println("=== GET /customers END ===");
        return "customers/index";
    }
    
    // Trang thêm mới khách hàng
    @GetMapping("/new")
    public String newCustomer(Model model) {
        model.addAttribute("khachHang", new KhachHang());
        return "customers/new";
    }
    
    // Xem chi tiết khách hàng
    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable String id, Model model) {
        KhachHang khachHang = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        model.addAttribute("khachHang", khachHang);
        return "customers/detail";
    }
    
    // Tạo mới khách hàng
    @PostMapping
    public String createCustomer(@ModelAttribute KhachHang khachHang) {
        System.out.println("=== POST /customers START ===");
        System.out.println("Received customer: " + khachHang.getTenKhachHang());
        
        try {
            KhachHang created = customerService.createCustomer(khachHang);
            System.out.println("Customer created with ID: " + created.getKhachHangId());
            System.out.println("Redirecting to /customers");
            return "redirect:/customers";
        } catch (Exception e) {
            System.out.println("ERROR creating customer: " + e.getMessage());
            e.printStackTrace();
            return "error/500";
        } finally {
            System.out.println("=== POST /customers END ===");
        }
    }
    
    // Trang chỉnh sửa khách hàng
    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable String id, Model model) {
        KhachHang khachHang = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        model.addAttribute("khachHang", khachHang);
        return "customers/edit";
    }
    
    // Cập nhật khách hàng
    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable String id, @ModelAttribute KhachHang khachHang) {
        customerService.updateCustomer(id, khachHang);
        return "redirect:/customers";
    }
    
    // Xóa khách hàng
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
