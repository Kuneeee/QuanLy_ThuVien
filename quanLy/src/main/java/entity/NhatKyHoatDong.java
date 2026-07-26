package entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nhat_ky_hoat_dong")
public class NhatKyHoatDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loai", length = 50, nullable = false)
    private String loai;

    @Column(name = "hanh_dong", length = 500, nullable = false)
    private String hanhDong;

    @Column(name = "nguoi_thuc_hien", length = 100)
    private String nguoiThucHien;

    @Column(name = "dia_chi_ip", length = 50)
    private String diaChiIp;

    @Column(name = "chi_tiet", columnDefinition = "TEXT")
    private String chiTiet;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    public NhatKyHoatDong() {}

    public NhatKyHoatDong(String loai, String hanhDong, String nguoiThucHien, String diaChiIp, String chiTiet, String trangThai) {
        this.loai = loai;
        this.hanhDong = hanhDong;
        this.nguoiThucHien = nguoiThucHien;
        this.diaChiIp = diaChiIp;
        this.chiTiet = chiTiet;
        this.thoiGian = LocalDateTime.now();
        this.trangThai = trangThai;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public String getHanhDong() { return hanhDong; }
    public void setHanhDong(String hanhDong) { this.hanhDong = hanhDong; }
    public String getNguoiThucHien() { return nguoiThucHien; }
    public void setNguoiThucHien(String nguoiThucHien) { this.nguoiThucHien = nguoiThucHien; }
    public String getDiaChiIp() { return diaChiIp; }
    public void setDiaChiIp(String diaChiIp) { this.diaChiIp = diaChiIp; }
    public String getChiTiet() { return chiTiet; }
    public void setChiTiet(String chiTiet) { this.chiTiet = chiTiet; }
    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) { this.thoiGian = thoiGian; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
