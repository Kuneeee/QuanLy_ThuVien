package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.DocGiaService;
import service.MuonTraService;
import service.NhapTaiLieuService;
import service.TaiLieuService;

@Controller
public class HomeController {

    @Autowired
    private TaiLieuService hangHoaService;
    
    @Autowired
    private MuonTraService banService;
    
    @Autowired
    private NhapTaiLieuService nhapService;
    
    @Autowired
    private DocGiaService customerService;

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

    @GetMapping("/use-cases")
    public String useCases() {
        return "use-cases";
    }
}
