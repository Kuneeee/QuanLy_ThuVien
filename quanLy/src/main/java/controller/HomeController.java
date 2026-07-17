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
    private TaiLieuService taiLieuService;
    
    @Autowired
    private MuonTraService muonTraService;
    
    @Autowired
    private NhapTaiLieuService nhapTaiLieuService;
    
    @Autowired
    private DocGiaService docGiaService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Dashboard với thông tin tổng quan
        model.addAttribute("tongTaiLieu", taiLieuService.getAllHangHoa().size());
        model.addAttribute("tongGiaTriTaiLieu", taiLieuService.tinhTongGiaTriKho());
        
        model.addAttribute("tongPhieuMuonTra", muonTraService.getAllBan().size());
        
        model.addAttribute("tongPhieuNhapTaiLieu", nhapTaiLieuService.getAllNhap().size());
        
        model.addAttribute("tongDocGia", docGiaService.getAllCustomers().size());
        
        return "dashboard";
    }
}
