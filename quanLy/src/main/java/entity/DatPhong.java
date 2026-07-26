package entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "dat_phong")
public class DatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_phong_id")
    private Long id;

    @Column(name = "ma_dat_phong", length = 50)
    private String maDatPhong;

    @Column(name = "ma_phong", nullable = false, length = 50)
    private String maPhong;

    @Column(name = "ten_phong", nullable = false, length = 200)
    private String tenPhong;

    @Column(name = "ten_nguoi_dat", nullable = false, length = 200)
    private String tenNguoiDat;

    @Column(name = "ma_nguoi_dat", length = 50)
    private String maNguoiDat;

    @Column(name = "loai_nguoi_dat", length = 50)
    private String loaiNguoiDat;

    @Column(name = "muc_dich", length = 300)
    private String mucDich;

    @Column(name = "so_luong_nguoi")
    private Integer soLuongNguoi;

    @Column(name = "thoi_gian_bat_dau")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    @Column(name = "so_lan_gia_han")
    private Integer soLanGiaHan;

    public DatPhong() {
        this.trangThai = "Đã đặt";
        this.soLanGiaHan = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }

    public String getMaNguoiDat() { return maNguoiDat; }
    public void setMaNguoiDat(String maNguoiDat) { this.maNguoiDat = maNguoiDat; }

    public String getLoaiNguoiDat() { return loaiNguoiDat; }
    public void setLoaiNguoiDat(String loaiNguoiDat) { this.loaiNguoiDat = loaiNguoiDat; }

    public String getMucDich() { return mucDich; }
    public void setMucDich(String mucDich) { this.mucDich = mucDich; }

    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }

    public LocalDateTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public LocalDateTime getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public Integer getSoLanGiaHan() { return soLanGiaHan; }
    public void setSoLanGiaHan(Integer soLanGiaHan) { this.soLanGiaHan = soLanGiaHan; }
}
