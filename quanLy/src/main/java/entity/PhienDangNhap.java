package entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "phien_dang_nhap")
public class PhienDangNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dang_nhap", length = 100, nullable = false)
    private String tenDangNhap;

    @Column(name = "dia_chi_ip", length = 50)
    private String diaChiIp;

    @Column(name = "thoi_gian_dang_nhap")
    private LocalDateTime thoiGianDangNhap;

    @Column(name = "thoi_gian_het_han")
    private LocalDateTime thoiGianHetHan;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    public PhienDangNhap() {}

    public PhienDangNhap(String tenDangNhap, String diaChiIp, LocalDateTime thoiGianHetHan) {
        this.tenDangNhap = tenDangNhap;
        this.diaChiIp = diaChiIp;
        this.thoiGianDangNhap = LocalDateTime.now();
        this.thoiGianHetHan = thoiGianHetHan;
        this.trangThai = "ACTIVE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getDiaChiIp() { return diaChiIp; }
    public void setDiaChiIp(String diaChiIp) { this.diaChiIp = diaChiIp; }
    public LocalDateTime getThoiGianDangNhap() { return thoiGianDangNhap; }
    public void setThoiGianDangNhap(LocalDateTime thoiGianDangNhap) { this.thoiGianDangNhap = thoiGianDangNhap; }
    public LocalDateTime getThoiGianHetHan() { return thoiGianHetHan; }
    public void setThoiGianHetHan(LocalDateTime thoiGianHetHan) { this.thoiGianHetHan = thoiGianHetHan; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
