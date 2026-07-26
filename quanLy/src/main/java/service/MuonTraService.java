package service;

import entity.MuonTra;
import entity.TaiLieu;
import entity.ViPhamPhong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MuonTraRepository;
import repository.DocGiaRepository;
import repository.TaiLieuRepository;
import repository.ViPhamPhongRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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

    @Autowired
    private ViPhamPhongRepository viPhamPhongRepository;
    
    // CREATE - Tạo mới phiếu bán
    public MuonTra createBan(MuonTra ban) {
        boSungGiaTuTaiLieuNeuCan(ban);
        if (ban.getBanCode() == null || ban.getBanCode().isEmpty()) {
            ban.setBanCode(taoMaBanTuDong(ban.getTrangThai()));
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
        MuonTra phieuDaLuu = createBan(ban);
        xoaPhieuDatTruocDaDung(phieuDaLuu);
        return phieuDaLuu;
    }

    public MuonTra lapPhieuMuon(MuonTra ban) {
        ban.setTrangThai("Đang mượn");
        boSungGiaTuTaiLieuNeuCan(ban);
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
                    LocalDateTime ngayTraThucTe = ngayTra != null ? ngayTra : LocalDateTime.now();
                    long soNgayTre = tinhSoNgayTre(ban, ngayTraThucTe);
                    ban.setNgayTra(ngayTraThucTe);
                    ban.setTrangThai("Đã trả");
                    MuonTra daLuu = banRepository.save(ban);
                    if (soNgayTre > 0) {
                        taoViPhamQuaHan(daLuu, soNgayTre, ngayTraThucTe);
                    }
                    return daLuu;
                })
                .orElse(null);
    }

    public MuonTra ghiNhanTra(String maPhieu, LocalDateTime ngayTra) {
        if (maPhieu == null || maPhieu.isBlank()) {
            return null;
        }

        try {
            Long id = Long.valueOf(maPhieu.trim());
            MuonTra theoId = ghiNhanTra(id, ngayTra);
            if (theoId != null) {
                return theoId;
            }
        } catch (NumberFormatException ignored) {
        }

        return banRepository.findFirstByBanCodeIgnoreCase(maPhieu.trim())
                .map(ban -> {
                    LocalDateTime ngayTraThucTe = ngayTra != null ? ngayTra : LocalDateTime.now();
                    long soNgayTre = tinhSoNgayTre(ban, ngayTraThucTe);
                    ban.setNgayTra(ngayTraThucTe);
                    ban.setTrangThai("Đã trả");
                    MuonTra daLuu = banRepository.save(ban);
                    if (soNgayTre > 0) {
                        taoViPhamQuaHan(daLuu, soNgayTre, ngayTraThucTe);
                    }
                    return daLuu;
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

    private long tinhSoNgayTre(MuonTra ban, LocalDateTime ngayTra) {
        if (ban == null || ban.getNgayHenTra() == null || ngayTra == null) {
            return 0;
        }
        return Math.max(ChronoUnit.DAYS.between(ban.getNgayHenTra().toLocalDate(), ngayTra.toLocalDate()), 0);
    }

    private void taoViPhamQuaHan(MuonTra ban, long soNgayTre, LocalDateTime ngayTra) {
        if (ban == null || soNgayTre <= 0) {
            return;
        }

        ViPhamPhong viPham = new ViPhamPhong();
        viPham.setSoBienLai(null);
        viPham.setMaDocGia(ban.getKhachHang() != null ? ban.getKhachHang() : "");
        viPham.setTenDocGia(ban.getTenKhachHang() != null ? ban.getTenKhachHang() : "");
        viPham.setMaTaiLieu(ban.getHangHoaID());
        viPham.setTenTaiLieu(ban.getTenHangHoa());
        viPham.setLoaiViPham("Quá hạn mượn");
        viPham.setSoNgayQuaHan((int) soNgayTre);
        viPham.setSoLuong(ban.getSoLuongBan() != null ? ban.getSoLuongBan() : 1);
        viPham.setSoTienDenBu(BigDecimal.ZERO);
        viPham.setSoTienPhat(BigDecimal.valueOf(soNgayTre).multiply(BigDecimal.valueOf(15000)));
        viPham.setTongTien(viPham.getSoTienDenBu().add(viPham.getSoTienPhat()));
        viPham.setGhiChu("Tự động ghi nhận khi trả sách quá hạn từ phiếu mượn " + (ban.getBanCode() != null ? ban.getBanCode() : ban.getBanId()));
        viPham.setNgayGhiNhan(ngayTra);
        viPhamPhongRepository.save(viPham);
    }

    private void boSungGiaTuTaiLieuNeuCan(MuonTra ban) {
        if (ban == null || ban.getHangHoaID() == null || ban.getHangHoaID().isBlank()) {
            return;
        }

        Optional<TaiLieu> taiLieu = taiLieuRepository.findById(ban.getHangHoaID());
        if (taiLieu.isEmpty() || taiLieu.get().getGiaNhap() == null) {
            return;
        }

        BigDecimal giaTuTaiLieu = taiLieu.get().getGiaNhap();
        if (ban.getGiaNhap() == null || ban.getGiaNhap().compareTo(BigDecimal.ZERO) <= 0) {
            ban.setGiaNhap(giaTuTaiLieu);
        }
        if (ban.getGiaBan() == null || ban.getGiaBan().compareTo(BigDecimal.ZERO) <= 0) {
            ban.setGiaBan(giaTuTaiLieu);
        }
        if (ban.getSoLuongBan() != null && ban.getSoLuongBan() > 0) {
            ban.setTongTien(ban.getGiaBan().multiply(BigDecimal.valueOf(ban.getSoLuongBan())));
        }
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
                    boSungGiaTuTaiLieuNeuCan(ban);
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
    private String taoMaBanTuDong(String trangThai) {
        String prefix = "Đặt trước".equalsIgnoreCase(trangThai) ? "DP" : "PM";
        return banRepository.findAll().stream()
                .filter(b -> b.getBanCode() != null && !b.getBanCode().isEmpty())
                .map(MuonTra::getBanCode)
                .filter(id -> id.startsWith(prefix))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(prefix.length()));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .map(max -> prefix + String.format("%03d", max + 1))
                .orElse(prefix + "001");
    }

    private void xoaPhieuDatTruocDaDung(MuonTra ban) {
        if (ban == null || ban.getHangHoaID() == null || ban.getHangHoaID().isBlank()) {
            return;
        }

        String maDocGia = ban.getKhachHang();
        if (maDocGia == null || maDocGia.isBlank()) {
            maDocGia = ban.getTenKhachHang();
        }
        final String maDocGiaCanSoKhop = maDocGia;

        List<MuonTra> phieuDatTruoc = banRepository.findAll().stream()
                .filter(item -> item.getTrangThai() != null && item.getTrangThai().equalsIgnoreCase("Đặt trước"))
                .filter(item -> item.getHangHoaID() != null && item.getHangHoaID().equalsIgnoreCase(ban.getHangHoaID()))
                .filter(item -> {
                    if (maDocGiaCanSoKhop == null || maDocGiaCanSoKhop.isBlank()) {
                        return true;
                    }
                    return (item.getKhachHang() != null && item.getKhachHang().equalsIgnoreCase(maDocGiaCanSoKhop))
                            || (item.getTenKhachHang() != null && item.getTenKhachHang().equalsIgnoreCase(maDocGiaCanSoKhop));
                })
                .sorted(Comparator.comparing(MuonTra::getNgayBan, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();

        if (!phieuDatTruoc.isEmpty()) {
            banRepository.delete(phieuDatTruoc.get(0));
        }
    }
    
    // BUSINESS - Tính tổng doanh thu
    public BigDecimal tinhTongDoanhThu() {
        return banRepository.findAll().stream()
                .map(b -> b.getGiaBan().multiply(new BigDecimal(b.getSoLuongBan())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
