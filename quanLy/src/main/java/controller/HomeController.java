package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.BanService;
import service.CustomerService;
import service.HangHoaService;
import service.NhapService;

@Controller
public class HomeController {

    @Autowired
    private HangHoaService hangHoaService;
    
    @Autowired
    private BanService banService;
    
    @Autowired
    private NhapService nhapService;
    
    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String index(Model model) {
        // Thống kê cơ bản
        model.addAttribute("soLuongHangHoa", hangHoaService.getAllHangHoa().size());
        model.addAttribute("soLuongBan", banService.getAllBan().size());
        model.addAttribute("soLuongNhap", nhapService.getAllNhap().size());
        model.addAttribute("soLuongKhachHang", customerService.getAllCustomers().size());
        
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Dashboard với thông tin tổng quan
        model.addAttribute("tongHangHoa", hangHoaService.getAllHangHoa().size());
        model.addAttribute("tongGiaTriKho", hangHoaService.tinhTongGiaTriKho());
        
        model.addAttribute("tongPhieuBan", banService.getAllBan().size());
        model.addAttribute("tongDoanhThu", banService.tinhTongDoanhThu());
        
        model.addAttribute("tongPhieuNhap", nhapService.getAllNhap().size());
        model.addAttribute("tongGiaTriNhap", nhapService.tinhTongGiaTriNhap());
        
        model.addAttribute("tongKhachHang", customerService.getAllCustomers().size());
        
        return "dashboard";
    }
}
