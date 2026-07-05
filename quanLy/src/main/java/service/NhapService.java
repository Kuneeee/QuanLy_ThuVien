package service;

import entity.Nhap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.NhapRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NhapService {
    
    @Autowired
    private NhapRepository nhapRepository;
    
    // CREATE - Tạo mới phiếu nhập
    public Nhap createNhap(Nhap nhap) {
        if (nhap.getMaNhapHang() == null || nhap.getMaNhapHang().isEmpty()) {
            nhap.setMaNhapHang(taoMaNhapTuDong());
        }
        return nhapRepository.save(nhap);
    }
    
    // READ - Lấy tất cả phiếu nhập
    public List<Nhap> getAllNhap() {
        return nhapRepository.findAll();
    }
    
    // READ - Lấy phiếu nhập theo ID
    public Optional<Nhap> getNhapById(Long id) {
        return nhapRepository.findById(id);
    }
    
    // UPDATE - Cập nhật phiếu nhập
    public Nhap updateNhap(Long id, Nhap nhapDetails) {
        return nhapRepository.findById(id)
                .map(nhap -> {
                    nhap.setHanghoaID(nhapDetails.getHanghoaID());
                    nhap.setTenHangHoa(nhapDetails.getTenHangHoa());
                    nhap.setSoLuongNhap(nhapDetails.getSoLuongNhap());
                    nhap.setGiaNhap(nhapDetails.getGiaNhap());
                    nhap.setNgayNhap(nhapDetails.getNgayNhap());
                    return nhapRepository.save(nhap);
                })
                .orElse(null);
    }
    
    // DELETE - Xóa phiếu nhập
    public boolean deleteNhap(Long id) {
        if (nhapRepository.existsById(id)) {
            nhapRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Tạo mã tự động
    private String taoMaNhapTuDong() {
        return nhapRepository.findAll().stream()
                .filter(n -> n.getMaNhapHang() != null && !n.getMaNhapHang().isEmpty())
                .map(n -> n.getMaNhapHang())
                .filter(id -> id.startsWith("NHAP"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(4));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .map(max -> "NHAP" + String.format("%03d", max + 1))
                .orElse("NHAP001");
    }
    
    // BUSINESS - Tính tổng giá trị nhập
    public BigDecimal tinhTongGiaTriNhap() {
        return nhapRepository.findAll().stream()
                .map(n -> n.getGiaNhap().multiply(new BigDecimal(n.getSoLuongNhap())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
