package controller;

import entity.NhapTaiLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import service.NhapTaiLieuService;

import java.util.List;

@Controller
@RequestMapping("/nhapTaiLieu")
public class NhapTaiLieuController {
    
    @Autowired
    private NhapTaiLieuService nhapTaiLieuService;
    
    // Trang danh sách phiếu nhập
    @GetMapping
    public String listNhap(Model model) {
        List<NhapTaiLieu> nhapList = nhapTaiLieuService.getAllNhap();
        model.addAttribute("nhapList", nhapList);
        model.addAttribute("newNhap", new NhapTaiLieu());
        return "nhapTaiLieu/index";
    }
    
    // Trang thêm mới phiếu nhập
    @GetMapping("/new")
    public String newNhap(Model model) {
        model.addAttribute("nhap", new NhapTaiLieu());
        return "nhapTaiLieu/new";
    }
    
    // Trang tìm kiếm phiếu nhập
    @GetMapping("/search")
    public String searchNhap(Model model) {
        List<NhapTaiLieu> nhapList = nhapTaiLieuService.getAllNhap();
        model.addAttribute("nhapList", nhapList);
        return "nhapTaiLieu/search";
    }
    
    // Xem chi tiết phiếu nhập
    @GetMapping("/{id}")
    public String viewNhapTaiLieu(@PathVariable Long id, Model model) {
        NhapTaiLieu nhap = nhapTaiLieuService.getNhapById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));
        model.addAttribute("nhap", nhap);
        return "nhapTaiLieu/detail";
    }
    
    // Tạo mới phiếu nhập
    @PostMapping
    public String createNhapTaiLieu(@ModelAttribute NhapTaiLieu nhap) {
        nhapTaiLieuService.createNhap(nhap);
        return "redirect:/nhapTaiLieu";
    }
    
    // Trang chỉnh sửa phiếu nhập
    @GetMapping("/{id}/edit")
    public String editNhap(@PathVariable Long id, Model model) {
        NhapTaiLieu nhap = nhapTaiLieuService.getNhapById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));
        model.addAttribute("nhap", nhap);
        return "nhapTaiLieu/edit";
    }
    
    // Cập nhật phiếu nhập
    @PostMapping("/{id}")
    public String updateNhapTaiLieu(@PathVariable Long id, @ModelAttribute NhapTaiLieu nhap) {
        nhapTaiLieuService.updateNhap(id, nhap);
        return "redirect:/nhapTaiLieu";
    }
    
    // Xóa phiếu nhập
    @PostMapping("/{id}/delete")
    public String deleteNhap(@PathVariable Long id) {
        nhapTaiLieuService.deleteNhap(id);
        return "redirect:/nhapTaiLieu";
    }
}
