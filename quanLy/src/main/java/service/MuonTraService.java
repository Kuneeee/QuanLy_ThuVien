package service;

import entity.MuonTra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MuonTraRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MuonTraService {
    
    @Autowired
    private MuonTraRepository banRepository;
    
    // CREATE - Tạo mới phiếu bán
    public MuonTra createBan(MuonTra ban) {
        if (ban.getBanCode() == null || ban.getBanCode().isEmpty()) {
            ban.setBanCode(taoMaBanTuDong());
        }
        return banRepository.save(ban);
    }
    
    // READ - Lấy tất cả phiếu bán
    public List<MuonTra> getAllBan() {
        return banRepository.findAll();
    }
    
    // READ - Lấy phiếu bán theo ID
    public Optional<MuonTra> getBanById(Long id) {
        return banRepository.findById(id);
    }
    
    // UPDATE - Cập nhật phiếu bán
    public MuonTra updateBan(Long id, MuonTra banDetails) {
        return banRepository.findById(id)
                .map(ban -> {
                    ban.setBanCode(banDetails.getBanCode());
                    ban.setHangHoaID(banDetails.getHangHoaID());
                    ban.setTenHangHoa(banDetails.getTenHangHoa());
                    ban.setTenKhachHang(banDetails.getTenKhachHang());
                    ban.setKhachHang(banDetails.getKhachHang());
                    ban.setSoLuongBan(banDetails.getSoLuongBan());
                    ban.setGiaBan(banDetails.getGiaBan());
                    ban.setGiaNhap(banDetails.getGiaNhap()); // Missing field!
                    ban.setTongTien(banDetails.getTongTien()); // Missing field!
                    ban.setNgayBan(banDetails.getNgayBan());
                    ban.setGhiChu(banDetails.getGhiChu()); // Missing field!
                    return banRepository.save(ban);
                })
                .orElse(null);
    }
    
    // DELETE - Xóa phiếu bán
    public boolean deleteBan(Long id) {
        if (banRepository.existsById(id)) {
            banRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Tạo mã tự động
    private String taoMaBanTuDong() {
        return banRepository.findAll().stream()
                .filter(b -> b.getBanCode() != null && !b.getBanCode().isEmpty())
                .map(b -> b.getBanCode())
                .filter(id -> id.startsWith("BAN"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(3));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .map(max -> "BAN" + String.format("%03d", max + 1))
                .orElse("BAN001");
    }
    
    // BUSINESS - Tính tổng doanh thu
    public BigDecimal tinhTongDoanhThu() {
        return banRepository.findAll().stream()
                .map(b -> b.getGiaBan().multiply(new BigDecimal(b.getSoLuongBan())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
