package service;

import entity.TaiLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TaiLieuRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class TaiLieuService {
    
    @Autowired
    private TaiLieuRepository hangHoaRepository;
    
    // CREATE - Tạo mới hàng hóa
    public TaiLieu createHangHoa(TaiLieu hangHoa) {
        if (hangHoa.getHanghoaID() == null || hangHoa.getHanghoaID().isEmpty()) {
            hangHoa.setHanghoaID(taoMaHangHoaTuDong());
        }
        if (hangHoa.getTenHangHoa() == null || hangHoa.getTenHangHoa().isBlank()) {
            hangHoa.setTenHangHoa("Tài liệu mới");
        }
        if (hangHoa.getSoLuongHangHoa() == null) {
            hangHoa.setSoLuongHangHoa(1);
        }
        if (hangHoa.getGiaNhap() == null) {
            hangHoa.setGiaNhap(BigDecimal.ZERO);
        }
        if (hangHoa.getLoaiHangHoa() == null || hangHoa.getLoaiHangHoa().isBlank()) {
            hangHoa.setLoaiHangHoa("Tài liệu khác");
        }
        if (hangHoa.getViTri() == null || hangHoa.getViTri().isBlank()) {
            hangHoa.setViTri("Kệ tài liệu");
        }
        if (hangHoa.getNhaSanXuat() == null || hangHoa.getNhaSanXuat().isBlank()) {
            hangHoa.setNhaSanXuat("Chưa cập nhật");
        }
        if (hangHoa.getNhaXuatBan() == null || hangHoa.getNhaXuatBan().isBlank()) {
            hangHoa.setNhaXuatBan("Chưa cập nhật");
        }
        if (hangHoa.getNamSanXuat() == null) {
            hangHoa.setNamSanXuat(Year.now().getValue());
        }
        // Set ngày nhập hiện tại nếu chưa có
        if (hangHoa.getNgayNhap() == null) {
            hangHoa.setNgayNhap(LocalDateTime.now());
        }
        return hangHoaRepository.save(hangHoa);
    }
    
    // READ - Lấy tất cả hàng hóa
    public List<TaiLieu> getAllHangHoa() {
        return hangHoaRepository.findAll();
    }
    
    // READ - Lấy hàng hóa theo ID
    public Optional<TaiLieu> getHangHoaById(String id) {
        return hangHoaRepository.findById(id);
    }
    
    // UPDATE - Cập nhật hàng hóa
    public TaiLieu updateHangHoa(String id, TaiLieu hangHoaDetails) {
        return hangHoaRepository.findById(id)
                .map(hangHoa -> {
                    if (hangHoaDetails.getTenHangHoa() != null && !hangHoaDetails.getTenHangHoa().isBlank()) {
                        hangHoa.setTenHangHoa(hangHoaDetails.getTenHangHoa());
                    }
                    if (hangHoaDetails.getLoaiHangHoa() != null && !hangHoaDetails.getLoaiHangHoa().isBlank()) {
                        hangHoa.setLoaiHangHoa(hangHoaDetails.getLoaiHangHoa());
                    }
                    if (hangHoaDetails.getSoLuongHangHoa() != null) {
                        hangHoa.setSoLuongHangHoa(hangHoaDetails.getSoLuongHangHoa());
                    }
                    if (hangHoaDetails.getGiaNhap() != null) {
                        hangHoa.setGiaNhap(hangHoaDetails.getGiaNhap());
                    }
                    if (hangHoaDetails.getNhaSanXuat() != null && !hangHoaDetails.getNhaSanXuat().isBlank()) {
                        hangHoa.setNhaSanXuat(hangHoaDetails.getNhaSanXuat());
                    }
                    if (hangHoaDetails.getNhaXuatBan() != null && !hangHoaDetails.getNhaXuatBan().isBlank()) {
                        hangHoa.setNhaXuatBan(hangHoaDetails.getNhaXuatBan());
                    }
                    if (hangHoaDetails.getViTri() != null && !hangHoaDetails.getViTri().isBlank()) {
                        hangHoa.setViTri(hangHoaDetails.getViTri());
                    }
                    if (hangHoaDetails.getNamSanXuat() != null) {
                        hangHoa.setNamSanXuat(hangHoaDetails.getNamSanXuat());
                    }
                    if (hangHoaDetails.getNgayNhap() != null) {
                        hangHoa.setNgayNhap(hangHoaDetails.getNgayNhap());
                    }
                    return hangHoaRepository.save(hangHoa);
                })
                .orElse(null);
    }
    
    // DELETE - Xóa hàng hóa
    public boolean deleteHangHoa(String id) {
        if (hangHoaRepository.existsById(id)) {
            hangHoaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Tạo mã tự động
    private String taoMaHangHoaTuDong() {
        return hangHoaRepository.findAll().stream()
                .filter(h -> h.getHanghoaID() != null && !h.getHanghoaID().isEmpty())
                .map(h -> h.getHanghoaID())
                .filter(id -> id.startsWith("HH"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(2));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .map(max -> "HH" + String.format("%03d", max + 1))
                .orElse("HH001");
    }
    
    // BUSINESS - Tính tổng giá trị kho
    public BigDecimal tinhTongGiaTriKho() {
        return hangHoaRepository.findAll().stream()
                .map(h -> h.getGiaNhap().multiply(new BigDecimal(h.getSoLuongHangHoa())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
