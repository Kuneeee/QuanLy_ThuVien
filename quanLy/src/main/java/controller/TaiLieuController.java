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
import repository.MuonTraRepository;
import repository.TaiLieuRepository;
import service.TaiLieuService;

import java.util.List;

@Controller
@RequestMapping("/taiLieu")
public class TaiLieuController {
    
    @Autowired
    private TaiLieuService taiLieuService;

    @Autowired
    private TaiLieuRepository taiLieuRepository;

    @Autowired
    private MuonTraRepository muonTraRepository;
    
    // Trang danh sách hàng hóa
    @GetMapping
    public String listTaiLieu(Model model) {
        List<TaiLieu> hangHoaList = taiLieuService.getAllHangHoa();
        model.addAttribute("hangHoaList", hangHoaList);
        model.addAttribute("newHangHoa", new TaiLieu());
        return "taiLieu/index";
    }

    @GetMapping("/search")
    public String searchTaiLieu(@RequestParam(required = false) String tenSach,
                                @RequestParam(required = false) String tacGia,
                                @RequestParam(required = false) String maTaiLieu,
                                @RequestParam(required = false) String nhaXuatBan,
                                @RequestParam(required = false) String nganhHoc,
                                Model model) {
        List<TaiLieu> searchResults = taiLieuRepository.searchTaiLieu(tenSach, tacGia, maTaiLieu, nhaXuatBan, nganhHoc);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("currentTenSach", tenSach);
        model.addAttribute("currentTacGia", tacGia);
        model.addAttribute("currentMaTaiLieu", maTaiLieu);
        model.addAttribute("currentNhaXuatBan", nhaXuatBan);
        model.addAttribute("currentNganhHoc", nganhHoc);
        model.addAttribute("nganhHocOptions", taiLieuRepository.findAllLoaiHangHoa());
        return "taiLieu/search";
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
        model.addAttribute("lichSuMuon", muonTraRepository.findByHanghoaIDOrderByNgayBanDesc(id));
        return "taiLieu/detail";
    }
    
    // Tạo mới hàng hóa
    @PostMapping
    public String createTaiLieu(@ModelAttribute TaiLieu hangHoa, Model model) {
        try {
            taiLieuService.createHangHoa(hangHoa);
            return "redirect:/taiLieu";
        } catch (Exception e) {
            System.err.println("Error creating HangHoa: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("hangHoa", hangHoa);
            model.addAttribute("error", "Không thể lưu tài liệu. Kiểm tra lại dữ liệu hoặc trạng thái cơ sở dữ liệu trên run-neon.");
            return "taiLieu/new";
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
    public String updateTaiLieu(@PathVariable String id, @ModelAttribute TaiLieu hangHoa, Model model) {
        try {
            TaiLieu updated = taiLieuService.updateHangHoa(id, hangHoa);
            if (updated != null) {
                return "redirect:/taiLieu";
            }
            model.addAttribute("hangHoa", hangHoa);
            model.addAttribute("error", "Không thể cập nhật tài liệu vì bản ghi không tồn tại.");
            return "taiLieu/edit";
        } catch (Exception e) {
            System.err.println("Error updating HangHoa: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("hangHoa", hangHoa);
            model.addAttribute("error", "Không thể cập nhật tài liệu. Kiểm tra lại dữ liệu hoặc trạng thái cơ sở dữ liệu trên run-neon.");
            return "taiLieu/edit";
        }
    }
    
    // Xóa hàng hóa
    @PostMapping("/{id}/delete")
    public String deleteHangHoa(@PathVariable String id) {
        try {
            boolean deleted = taiLieuService.deleteHangHoa(id);
            if (deleted) {
                return "redirect:/taiLieu";
            }
            return "redirect:/taiLieu?error=true";
        } catch (Exception e) {
            System.err.println("Error deleting HangHoa: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/taiLieu?error=true";
        }
    }
}
