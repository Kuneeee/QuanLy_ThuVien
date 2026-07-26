package com.example.app;

import entity.DocGia;
import entity.DatPhong;
import entity.MuonTra;
import entity.NhapTaiLieu;
import entity.LichTrongPhong;
import entity.ThongBaoMau;
import entity.ViPhamPhong;
import entity.TaiLieu;
import repository.DatPhongRepository;
import repository.TaiLieuRepository;
import repository.DocGiaRepository;
import repository.MuonTraRepository;
import repository.NhapTaiLieuRepository;
import repository.LichTrongPhongRepository;
import repository.ThongBaoMauRepository;
import repository.ViPhamPhongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TaiLieuRepository taiLieuRepository;

    @Autowired
    private DocGiaRepository docGiaRepository;

    @Autowired
    private MuonTraRepository muonTraRepository;

    @Autowired
    private NhapTaiLieuRepository nhapTaiLieuRepository;

    @Autowired
    private DatPhongRepository datPhongRepository;

    @Autowired
    private LichTrongPhongRepository lichTrongPhongRepository;

    @Autowired
    private ViPhamPhongRepository viPhamPhongRepository;

    @Autowired
    private ThongBaoMauRepository thongBaoMauRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedTaiLieu();
        seedDocGia();
        ensureDocGiaAccounts();
        seedNhapTaiLieu();
        seedMuonTra();
        seedPhong();
        seedThongBao();

        System.out.println("=== DATA INITIALIZER COMPLETE ===");
        System.out.println("TaiLieu count: " + taiLieuRepository.count());
        System.out.println("DocGia count: " + docGiaRepository.count());
        System.out.println("NhapTaiLieu count: " + nhapTaiLieuRepository.count());
        System.out.println("MuonTra count: " + muonTraRepository.count());
        System.out.println("DatPhong count: " + datPhongRepository.count());
        System.out.println("LichTrongPhong count: " + lichTrongPhongRepository.count());
        System.out.println("ViPhamPhong count: " + viPhamPhongRepository.count());
        System.out.println("ThongBaoMau count: " + thongBaoMauRepository.count());
    }

    private void seedTaiLieu() {
        if (taiLieuRepository.count() > 0) {
            return;
        }

        List<TaiLieu> taiLieus = List.of(
                new TaiLieu("TL001", "Lập trình Java căn bản", 18, "Nguyễn Văn A", 2023,
                        LocalDateTime.now().minusDays(18), new BigDecimal("120000"), "Công nghệ thông tin"),
                new TaiLieu("TL002", "Cơ sở dữ liệu hiện đại", 12, "Trần Thị B", 2022,
                        LocalDateTime.now().minusDays(12), new BigDecimal("150000"), "Công nghệ thông tin"),
                new TaiLieu("TL003", "Quản trị thư viện số", 9, "Lê Minh C", 2024,
                        LocalDateTime.now().minusDays(7), new BigDecimal("98000"), "Nghiệp vụ"),
                new TaiLieu("TL004", "Thiết kế giao diện web", 14, "Phạm Thu D", 2024,
                        LocalDateTime.now().minusDays(3), new BigDecimal("135000"), "Thiết kế")
        );

        taiLieus.forEach(taiLieu -> {
            taiLieu.setNhaXuatBan("NXB Giáo dục");
            taiLieu.setViTri("Kệ trung tâm");
        });

        taiLieuRepository.saveAll(taiLieus);
    }

    private void seedDocGia() {
        if (docGiaRepository.count() > 0) {
            return;
        }

        DocGia docGia1 = new DocGia("DG001", "Nguyễn Văn An", "0901234567", "an.nguyen@thuviendh.vn",
                "Hà Nội", "Sinh viên");
        docGia1.setTongDaMua(new BigDecimal("240000"));
        docGia1.setSoDonHang(3);
        docGia1.setTongChiTieu(new BigDecimal("240000"));
        docGia1.setDiemThuong(24);
        docGia1.setNgayThamGia(LocalDate.now().minusMonths(4));
        docGia1.setNgayTao(LocalDateTime.now().minusMonths(4));

        DocGia docGia2 = new DocGia("DG002", "Trần Thị Bích", "0912345678", "bich.tran@thuviendh.vn",
                "Đà Nẵng", "Giáo viên");
        docGia2.setTongDaMua(new BigDecimal("380000"));
        docGia2.setSoDonHang(5);
        docGia2.setTongChiTieu(new BigDecimal("380000"));
        docGia2.setDiemThuong(38);
        docGia2.setNgayThamGia(LocalDate.now().minusMonths(7));
        docGia2.setNgayTao(LocalDateTime.now().minusMonths(7));

        DocGia docGia3 = new DocGia("DG003", "Lê Minh Cường", "0934567890", "cuong.le@thuviendh.vn",
                "TP. Hồ Chí Minh", "Sinh viên");
        docGia3.setTongDaMua(new BigDecimal("120000"));
        docGia3.setSoDonHang(1);
        docGia3.setTongChiTieu(new BigDecimal("120000"));
        docGia3.setDiemThuong(12);
        docGia3.setNgayThamGia(LocalDate.now().minusMonths(1));
        docGia3.setNgayTao(LocalDateTime.now().minusMonths(1));

        docGiaRepository.saveAll(List.of(docGia1, docGia2, docGia3));
    }

    private void ensureDocGiaAccounts() {
        List<DocGia> docGias = docGiaRepository.findAll();
        boolean updated = false;

        for (DocGia docGia : docGias) {
            if (docGia.getTaiKhoan() == null || docGia.getTaiKhoan().isBlank()) {
                docGia.setTaiKhoan(taoTaiKhoanTuTen(docGia.getTenKhachHang(), docGia.getKhachHangId()));
                updated = true;
            }
            if (docGia.getMatKhau() == null || docGia.getMatKhau().isBlank()) {
                docGia.setMatKhau(passwordEncoder.encode("123456"));
                updated = true;
            }
            if (docGia.getQuyenHan() == null || docGia.getQuyenHan().isBlank()) {
                docGia.setQuyenHan(docGia.getLoaiKhachHang() != null ? docGia.getLoaiKhachHang() : "Sinh viên");
                updated = true;
            }
        }

        if (updated) {
            docGiaRepository.saveAll(docGias);
        }
    }

    private String taoTaiKhoanTuTen(String ten, String fallbackId) {
        if (fallbackId != null && !fallbackId.isBlank()) {
            return fallbackId.toLowerCase();
        }
        if (ten == null || ten.isBlank()) {
            return "user";
        }
        String normal = ten.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("đ", "d")
                .replaceAll("[^a-z0-9]+", ".")
                .replaceAll("(^\\.|\\.$)", "");
        return normal.isBlank() ? "user" : normal;
    }

    private void seedNhapTaiLieu() {
        if (nhapTaiLieuRepository.count() > 0) {
            return;
        }

        NhapTaiLieu nhap1 = new NhapTaiLieu();
        nhap1.setMaNhapHang("NH001");
        nhap1.setHanghoaID("TL001");
        nhap1.setTenHangHoa("Lập trình Java căn bản");
        nhap1.setSoLuongNhap(18);
        nhap1.setGiaNhap(new BigDecimal("85000"));
        nhap1.setNgayNhap(LocalDateTime.now().minusDays(18));
        nhap1.setNhaCungCap("NXB Khoa học kỹ thuật");
        nhap1.setNguoiNhap("Quản trị thư viện");
        nhap1.setGhiChu("Nhập bổ sung đầu học kỳ");
        nhap1.setTrangThai("Đã nhập");

        NhapTaiLieu nhap2 = new NhapTaiLieu();
        nhap2.setMaNhapHang("NH002");
        nhap2.setHanghoaID("TL003");
        nhap2.setTenHangHoa("Quản trị thư viện số");
        nhap2.setSoLuongNhap(9);
        nhap2.setGiaNhap(new BigDecimal("76000"));
        nhap2.setNgayNhap(LocalDateTime.now().minusDays(7));
        nhap2.setNhaCungCap("Trung tâm phát hành sách");
        nhap2.setNguoiNhap("Thủ thư trưởng");
        nhap2.setGhiChu("Bổ sung tài liệu chuyên ngành");
        nhap2.setTrangThai("Đã nhập");

        nhapTaiLieuRepository.saveAll(List.of(nhap1, nhap2));
    }

    private void seedMuonTra() {
        if (muonTraRepository.count() > 0) {
            return;
        }

        MuonTra muon1 = new MuonTra();
        muon1.setBanCode("PM001");
        muon1.setHangHoaID("TL001");
        muon1.setTenHangHoa("Lập trình Java căn bản");
        muon1.setTenKhachHang("Nguyễn Văn An");
        muon1.setKhachHang("DG001");
        muon1.setNgayBan(LocalDateTime.now().minusDays(2));
        muon1.setGiaBan(new BigDecimal("120000"));
        muon1.setGiaNhap(new BigDecimal("85000"));
        muon1.setSoLuongBan(1);
        muon1.setTongTien(new BigDecimal("120000"));
        muon1.setGhiChu("Mượn tham khảo cho môn Java cơ bản");

        MuonTra muon2 = new MuonTra();
        muon2.setBanCode("PM002");
        muon2.setHangHoaID("TL003");
        muon2.setTenHangHoa("Quản trị thư viện số");
        muon2.setTenKhachHang("Trần Thị Bích");
        muon2.setKhachHang("DG002");
        muon2.setNgayBan(LocalDateTime.now().minusDays(1));
        muon2.setGiaBan(new BigDecimal("98000"));
        muon2.setGiaNhap(new BigDecimal("76000"));
        muon2.setSoLuongBan(2);
        muon2.setTongTien(new BigDecimal("196000"));
        muon2.setGhiChu("Mượn cho lớp nghiệp vụ thư viện");

        MuonTra muon3 = new MuonTra();
        muon3.setBanCode("PM003");
        muon3.setHangHoaID("TL004");
        muon3.setTenHangHoa("Thiết kế giao diện web");
        muon3.setTenKhachHang("Lê Minh Cường");
        muon3.setKhachHang("DG003");
        muon3.setNgayBan(LocalDateTime.now());
        muon3.setGiaBan(new BigDecimal("135000"));
        muon3.setGiaNhap(new BigDecimal("92000"));
        muon3.setSoLuongBan(1);
        muon3.setTongTien(new BigDecimal("135000"));
        muon3.setGhiChu("Mượn nghiên cứu giao diện");

        muonTraRepository.saveAll(List.of(muon1, muon2, muon3));
    }

    private void seedPhong() {
        if (datPhongRepository.count() == 0) {
            DatPhong datPhong = new DatPhong();
            datPhong.setMaDatPhong("DPH001");
            datPhong.setMaPhong("P101");
            datPhong.setTenPhong("Phòng học nhóm 1");
            datPhong.setTenNguoiDat("Nguyễn Văn An");
            datPhong.setMaNguoiDat("DG001");
            datPhong.setLoaiNguoiDat("Sinh viên");
            datPhong.setMucDich("Học nhóm môn Java");
            datPhong.setSoLuongNguoi(4);
            datPhong.setThoiGianBatDau(LocalDateTime.now().plusDays(1));
            datPhong.setThoiGianKetThuc(LocalDateTime.now().plusDays(1).plusHours(2));
            datPhong.setTrangThai("Đã đặt");
            datPhong.setGhiChu("Ưu tiên nhóm nhỏ");
            datPhongRepository.save(datPhong);
        }

        if (lichTrongPhongRepository.count() == 0) {
            LichTrongPhong lichTrong = new LichTrongPhong();
            lichTrong.setMaPhong("P101");
            lichTrong.setTenPhong("Phòng học nhóm 1");
            lichTrong.setBatDau(LocalDateTime.now().plusDays(2).withHour(8).withMinute(0).withSecond(0).withNano(0));
            lichTrong.setKetThuc(LocalDateTime.now().plusDays(2).withHour(12).withMinute(0).withSecond(0).withNano(0));
            lichTrong.setGhiChu("Lịch trống sáng thứ 2");
            lichTrong.setTrangThai("Trống");
            lichTrongPhongRepository.save(lichTrong);
        }

        if (viPhamPhongRepository.count() == 0) {
            ViPhamPhong viPham = new ViPhamPhong();
            viPham.setSoBienLai("BLP0001");
            viPham.setMaDocGia("DG002");
            viPham.setTenDocGia("Trần Thị Bích");
            viPham.setMaTaiLieu("TL001");
            viPham.setTenTaiLieu("Lập trình Java căn bản");
            viPham.setLoaiViPham("Quá hạn mượn");
            viPham.setSoNgayQuaHan(3);
            viPham.setSoLuong(1);
            viPham.setSoTienPhat(new BigDecimal("15000"));
            viPham.setSoTienDenBu(BigDecimal.ZERO);
            viPham.setTongTien(new BigDecimal("15000"));
            viPham.setGhiChu("Mẫu vi phạm để minh họa");
            viPham.setNgayGhiNhan(LocalDateTime.now().minusDays(1));
            viPhamPhongRepository.save(viPham);
        }
    }

    private void seedThongBao() {
        if (thongBaoMauRepository.count() > 0) {
            return;
        }

        ThongBaoMau baoCao = new ThongBaoMau();
        baoCao.setMaMau("BC001");
        baoCao.setLoaiThongBao("Mẫu báo cáo");
        baoCao.setTieuDe("Báo cáo tổng hợp mượn trả theo học kỳ");
        baoCao.setNoiDung("Tổng hợp số lượng phiếu mượn, phiếu trả, phiếu quá hạn và vi phạm của từng học kỳ.");
        baoCao.setDoiTuong("Quản trị viên");
        baoCao.setKenhGui("Trong hệ thống");
        baoCao.setTrangThai("Đã gửi");
        baoCao.setNgayGui(LocalDateTime.now().minusDays(2));
        baoCao.setNgayTao(LocalDateTime.now().minusDays(2));
        baoCao.setNgayCapNhat(LocalDateTime.now().minusDays(1));

        ThongBaoMau thuVien = new ThongBaoMau();
        thuVien.setMaMau("TB001");
        thuVien.setLoaiThongBao("Thông báo thư viện");
        thuVien.setTieuDe("Cập nhật giờ mở cửa thư viện");
        thuVien.setNoiDung("Thư viện mở cửa từ 7h30 đến 20h00 các ngày trong tuần.");
        thuVien.setDoiTuong("Toàn bộ người dùng");
        thuVien.setKenhGui("Trong hệ thống");
        thuVien.setTrangThai("Đã gửi");
        thuVien.setNgayGui(LocalDateTime.now().minusDays(1));
        thuVien.setNgayTao(LocalDateTime.now().minusDays(1));
        thuVien.setNgayCapNhat(LocalDateTime.now().minusHours(6));

        ThongBaoMau hanTra = new ThongBaoMau();
        hanTra.setMaMau("HT001");
        hanTra.setLoaiThongBao("Thông báo hạn trả sách");
        hanTra.setTieuDe("Nhắc hạn trả sách trong tuần");
        hanTra.setNoiDung("Vui lòng hoàn trả đúng hạn để tránh phát sinh vi phạm và phí phạt.");
        hanTra.setDoiTuong("Người đang mượn");
        hanTra.setKenhGui("Trong hệ thống");
        hanTra.setTrangThai("Nháp");
        hanTra.setNgayTao(LocalDateTime.now().minusHours(12));
        hanTra.setNgayCapNhat(LocalDateTime.now().minusHours(12));

        ThongBaoMau suKien = new ThongBaoMau();
        suKien.setMaMau("SK001");
        suKien.setLoaiThongBao("Thông báo sự kiện");
        suKien.setTieuDe("Giao lưu đọc sách cuối tháng");
        suKien.setNoiDung("Mời sinh viên và giáo viên tham gia buổi giao lưu vào cuối tháng này.");
        suKien.setDoiTuong("Sinh viên và giáo viên");
        suKien.setKenhGui("Trong hệ thống");
        suKien.setTrangThai("Đã gửi");
        suKien.setNgayGui(LocalDateTime.now());
        suKien.setNgayTao(LocalDateTime.now().minusDays(3));
        suKien.setNgayCapNhat(LocalDateTime.now().minusDays(1));

        thongBaoMauRepository.saveAll(List.of(baoCao, thuVien, hanTra, suKien));
    }
}
