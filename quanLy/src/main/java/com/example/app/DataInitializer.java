package com.example.app;

import entity.DocGia;
import entity.MuonTra;
import entity.NhapTaiLieu;
import entity.TaiLieu;
import repository.TaiLieuRepository;
import repository.DocGiaRepository;
import repository.MuonTraRepository;
import repository.NhapTaiLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedTaiLieu();
        seedDocGia();
        seedNhapTaiLieu();
        seedMuonTra();

        System.out.println("=== DATA INITIALIZER COMPLETE ===");
        System.out.println("TaiLieu count: " + taiLieuRepository.count());
        System.out.println("DocGia count: " + docGiaRepository.count());
        System.out.println("NhapTaiLieu count: " + nhapTaiLieuRepository.count());
        System.out.println("MuonTra count: " + muonTraRepository.count());
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
}
