package service;

import entity.MuonTra;
import entity.TaiLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MuonTraRepository;
import repository.DocGiaRepository;
import repository.TaiLieuRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class MuonTraService {
    
    @Autowired
    private MuonTraRepository banRepository;

    @Autowired
    private TaiLieuRepository taiLieuRepository;

    @Autowired
    private DocGiaRepository docGiaRepository;
    
    // CREATE - Tạo mới phiếu bán
    public MuonTra createBan(MuonTra ban) {
        if (ban.getBanCode() == null || ban.getBanCode().isEmpty()) {
            ban.setBanCode(taoMaBanTuDong());
        }
        if (ban.getSoLuongBan() == null || ban.getSoLuongBan() <= 0) {
            ban.setSoLuongBan(1);
        }
        if (ban.getGiaBan() == null) {
            ban.setGiaBan(BigDecimal.ZERO);
        }
        if (ban.getTongTien() == null) {
            ban.setTongTien(ban.getGiaBan().multiply(BigDecimal.valueOf(ban.getSoLuongBan())));
        }
        if (ban.getTrangThai() == null || ban.getTrangThai().isBlank()) {
            ban.setTrangThai("Đang mượn");
        }
        if (ban.getNgayHenTra() == null && ban.getNgayBan() != null) {
            ban.setNgayHenTra(ban.getNgayBan().plusDays(14));
        }
        return banRepository.save(ban);
    }

    public MuonTra taoYeuCauDatTruoc(MuonTra ban) {
        ban.setTrangThai("Đặt trước");
        if (ban.getNgayBan() == null) {
            ban.setNgayBan(LocalDateTime.now());
        }
        if (ban.getSoLuongBan() == null || ban.getSoLuongBan() <= 0) {
            ban.setSoLuongBan(1);
        }
        if (ban.getGiaBan() == null) {
            ban.setGiaBan(BigDecimal.ZERO);
        }
        if (ban.getTongTien() == null) {
            ban.setTongTien(BigDecimal.ZERO);
        }
        if (ban.getNgayHenTra() == null) {
            ban.setNgayHenTra(ban.getNgayBan().plusDays(7));
        }
        return createBan(ban);
    }

    public MuonTra lapPhieuMuon(MuonTra ban) {
        ban.setTrangThai("Đang mượn");
        if (ban.getNgayBan() == null) {
            ban.setNgayBan(LocalDateTime.now());
        }
        if (ban.getSoLuongBan() == null || ban.getSoLuongBan() <= 0) {
            ban.setSoLuongBan(1);
        }
        if (ban.getGiaBan() == null) {
            ban.setGiaBan(BigDecimal.ZERO);
        }
        if (ban.getTongTien() == null) {
            ban.setTongTien(BigDecimal.ZERO);
        }
        if (ban.getNgayHenTra() == null) {
            ban.setNgayHenTra(ban.getNgayBan().plusDays(14));
        }
        return createBan(ban);
    }

    public MuonTra ghiNhanTra(Long id, LocalDateTime ngayTra) {
        return banRepository.findById(id)
                .map(ban -> {
                    ban.setNgayTra(ngayTra != null ? ngayTra : LocalDateTime.now());
                    ban.setTrangThai("Đã trả");
                    return banRepository.save(ban);
                })
                .orElse(null);
    }

    public boolean kiemTraTaiKhoanTonTai(String maDocGia) {
        if (maDocGia == null || maDocGia.isBlank()) {
            return false;
        }
        return docGiaRepository.existsById(maDocGia);
    }

    public boolean kiemTraTinhTrangTaiLieu(String maTaiLieu) {
        if (maTaiLieu == null || maTaiLieu.isBlank()) {
            return false;
        }
        Optional<TaiLieu> taiLieu = taiLieuRepository.findById(maTaiLieu);
        return taiLieu.isPresent() && taiLieu.get().getSoLuongHangHoa() != null && taiLieu.get().getSoLuongHangHoa() > 0;
    }

    public long tinhSoNgayMuon(MuonTra ban) {
        if (ban == null || ban.getNgayBan() == null) {
            return 0;
        }
        LocalDateTime ketThuc = ban.getNgayTra() != null ? ban.getNgayTra() : LocalDateTime.now();
        return Math.max(ChronoUnit.DAYS.between(ban.getNgayBan().toLocalDate(), ketThuc.toLocalDate()), 0);
    }

    public long tinhSoNgayTre(MuonTra ban) {
        if (ban == null || ban.getNgayHenTra() == null) {
            return 0;
        }
        LocalDateTime ketThuc = ban.getNgayTra() != null ? ban.getNgayTra() : LocalDateTime.now();
        return Math.max(ChronoUnit.DAYS.between(ban.getNgayHenTra().toLocalDate(), ketThuc.toLocalDate()), 0);
    }

    public boolean coTheGiaHan(MuonTra ban) {
        return ban != null && ban.getNgayTra() == null;
    }

    public MuonTra giaHanThoiGianMuon(Long id, int soNgayGiaHan) {
        return banRepository.findById(id)
                .map(ban -> {
                    int hopLe = Math.max(soNgayGiaHan, 0);
                    ban.setSoNgayGiaHan((ban.getSoNgayGiaHan() != null ? ban.getSoNgayGiaHan() : 0) + hopLe);
                    if (ban.getNgayHenTra() == null) {
                        ban.setNgayHenTra(LocalDateTime.now().plusDays(hopLe));
                    } else {
                        ban.setNgayHenTra(ban.getNgayHenTra().plusDays(hopLe));
                    }
                    ban.setTrangThai("Đang mượn");
                    return banRepository.save(ban);
                })
                .orElse(null);
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
