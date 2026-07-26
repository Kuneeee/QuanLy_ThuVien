package service;

import entity.DatPhong;
import entity.LichTrongPhong;
import entity.TaiLieu;
import entity.ViPhamPhong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DatPhongRepository;
import repository.LichTrongPhongRepository;
import repository.TaiLieuRepository;
import repository.ViPhamPhongRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PhongService {

    @Autowired
    private DatPhongRepository datPhongRepository;

    @Autowired
    private LichTrongPhongRepository lichTrongPhongRepository;

    @Autowired
    private ViPhamPhongRepository viPhamPhongRepository;

    @Autowired
    private TaiLieuRepository taiLieuRepository;

    public List<DatPhong> getAllDatPhong() {
        return datPhongRepository.findAll();
    }

    public List<LichTrongPhong> getAllLichTrong() {
        return lichTrongPhongRepository.findAll();
    }

    public List<ViPhamPhong> getAllViPham() {
        return viPhamPhongRepository.findAll();
    }

    public DatPhong datPhong(DatPhong datPhong) {
        validateDatPhong(datPhong);
        if (datPhong.getMaDatPhong() == null || datPhong.getMaDatPhong().isBlank()) {
            datPhong.setMaDatPhong(taoMaDatPhongTuDong());
        }
        if (datPhong.getTrangThai() == null || datPhong.getTrangThai().isBlank()) {
            datPhong.setTrangThai("Đã đặt");
        }
        if (datPhong.getSoLanGiaHan() == null) {
            datPhong.setSoLanGiaHan(0);
        }
        if (datPhong.getThoiGianBatDau() == null) {
            datPhong.setThoiGianBatDau(LocalDateTime.now());
        }
        if (datPhong.getThoiGianKetThuc() == null) {
            datPhong.setThoiGianKetThuc(datPhong.getThoiGianBatDau().plusHours(2));
        }
        return datPhongRepository.save(datPhong);
    }

    public DatPhong huyDatPhong(Long id) {
        return datPhongRepository.findById(id).map(datPhong -> {
            datPhong.setTrangThai("Đã hủy");
            return datPhongRepository.save(datPhong);
        }).orElse(null);
    }

    public DatPhong giaHanDatPhong(Long id, int soPhutGiaHan) {
        return datPhongRepository.findById(id).map(datPhong -> {
            int phutHopLe = Math.max(soPhutGiaHan, 15);
            datPhong.setSoLanGiaHan((datPhong.getSoLanGiaHan() != null ? datPhong.getSoLanGiaHan() : 0) + 1);
            if (datPhong.getThoiGianKetThuc() == null) {
                datPhong.setThoiGianKetThuc(LocalDateTime.now().plusMinutes(phutHopLe));
            } else {
                datPhong.setThoiGianKetThuc(datPhong.getThoiGianKetThuc().plusMinutes(phutHopLe));
            }
            datPhong.setTrangThai("Gia hạn");
            return datPhongRepository.save(datPhong);
        }).orElse(null);
    }

    public LichTrongPhong themLichTrong(LichTrongPhong lichTrongPhong) {
        if (lichTrongPhong.getBatDau() == null || lichTrongPhong.getKetThuc() == null) {
            throw new IllegalArgumentException("Thời gian lịch trống không được để trống");
        }
        if (lichTrongPhong.getMaPhong() == null || lichTrongPhong.getMaPhong().isBlank()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        if (lichTrongPhong.getTrangThai() == null || lichTrongPhong.getTrangThai().isBlank()) {
            lichTrongPhong.setTrangThai("Trống");
        }
        return lichTrongPhongRepository.save(lichTrongPhong);
    }

    public ViPhamPhong ghiNhanViPham(ViPhamPhong viPhamPhong) {
        validateViPham(viPhamPhong);
        if (viPhamPhong.getSoBienLai() == null || viPhamPhong.getSoBienLai().isBlank()) {
            viPhamPhong.setSoBienLai(taoSoBienLaiTuDong());
        }
        if (viPhamPhong.getNgayGhiNhan() == null) {
            viPhamPhong.setNgayGhiNhan(LocalDateTime.now());
        }
        tinhChiPhi(viPhamPhong);
        return viPhamPhongRepository.save(viPhamPhong);
    }

    public Optional<ViPhamPhong> getViPhamById(Long id) {
        return viPhamPhongRepository.findById(id);
    }

    public BigDecimal tinhChiPhi(ViPhamPhong viPhamPhong) {
        BigDecimal denBu = BigDecimal.ZERO;
        BigDecimal phat = BigDecimal.ZERO;

        String loaiViPham = viPhamPhong.getLoaiViPham() != null ? viPhamPhong.getLoaiViPham().trim().toLowerCase() : "";
        if ("quá hạn mượn".equals(loaiViPham) || "qua han muon".equals(loaiViPham)) {
            if (viPhamPhong.getSoNgayQuaHan() == null || viPhamPhong.getSoNgayQuaHan() < 0) {
                throw new IllegalArgumentException("Số ngày quá hạn phải lớn hơn hoặc bằng 0");
            }
            int soNgay = viPhamPhong.getSoNgayQuaHan();
            phat = BigDecimal.valueOf(soNgay).multiply(BigDecimal.valueOf(5000));
        } else if ("mất".equals(loaiViPham) || "mat".equals(loaiViPham) || "hư hỏng".equals(loaiViPham) || "hu hong".equals(loaiViPham)) {
            if (viPhamPhong.getMaTaiLieu() == null || viPhamPhong.getMaTaiLieu().isBlank()) {
                throw new IllegalArgumentException("Mã tài liệu là bắt buộc khi ghi nhận mất hoặc hư hỏng");
            }
            BigDecimal giaTriTaiLieu = BigDecimal.ZERO;
            Optional<TaiLieu> taiLieu = taiLieuRepository.findById(viPhamPhong.getMaTaiLieu());
            if (taiLieu.isEmpty() || taiLieu.get().getGiaNhap() == null) {
                throw new IllegalArgumentException("Không tìm thấy tài liệu hoặc chưa có giá nhập để tính đền bù");
            }
            giaTriTaiLieu = taiLieu.get().getGiaNhap();
            if ("mất".equals(loaiViPham) || "mat".equals(loaiViPham)) {
                denBu = giaTriTaiLieu.multiply(BigDecimal.valueOf(1.5));
            } else {
                denBu = giaTriTaiLieu.multiply(BigDecimal.valueOf(0.5));
            }
        }

        if (viPhamPhong.getSoTienDenBu() == null) {
            viPhamPhong.setSoTienDenBu(denBu);
        }
        if (viPhamPhong.getSoTienPhat() == null) {
            viPhamPhong.setSoTienPhat(phat);
        }
        viPhamPhong.setTongTien(viPhamPhong.getSoTienDenBu().add(viPhamPhong.getSoTienPhat()));
        return viPhamPhong.getTongTien();
    }

    public String taoSoBienLaiTuDong() {
        long count = viPhamPhongRepository.count();
        return "BLP" + String.format("%04d", count + 1);
    }

    public List<ViPhamPhong> findAllByLoaiViPham(String loaiViPham) {
        return viPhamPhongRepository.findByLoaiViPhamOrderByNgayGhiNhanDesc(loaiViPham);
    }

    private void validateDatPhong(DatPhong datPhong) {
        if (datPhong.getMaPhong() == null || datPhong.getMaPhong().isBlank()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        if (datPhong.getTenPhong() == null || datPhong.getTenPhong().isBlank()) {
            throw new IllegalArgumentException("Tên phòng không được để trống");
        }
        if (datPhong.getTenNguoiDat() == null || datPhong.getTenNguoiDat().isBlank()) {
            throw new IllegalArgumentException("Tên người đặt không được để trống");
        }
    }

    private void validateViPham(ViPhamPhong viPhamPhong) {
        if (viPhamPhong.getMaDocGia() == null || viPhamPhong.getMaDocGia().isBlank()) {
            throw new IllegalArgumentException("Mã độc giả không được để trống");
        }
        if (viPhamPhong.getTenDocGia() == null || viPhamPhong.getTenDocGia().isBlank()) {
            throw new IllegalArgumentException("Tên độc giả không được để trống");
        }
        if (viPhamPhong.getLoaiViPham() == null || viPhamPhong.getLoaiViPham().isBlank()) {
            throw new IllegalArgumentException("Loại vi phạm không được để trống");
        }
    }

    private String taoMaDatPhongTuDong() {
        return "DP" + String.format("%04d", datPhongRepository.count() + 1);
    }
}
