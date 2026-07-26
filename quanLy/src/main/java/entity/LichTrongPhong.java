package entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_trong_phong")
public class LichTrongPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lich_trong_id")
    private Long id;

    @Column(name = "ma_phong", nullable = false, length = 50)
    private String maPhong;

    @Column(name = "ten_phong", nullable = false, length = 200)
    private String tenPhong;

    @Column(name = "bat_dau", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime batDau;

    @Column(name = "ket_thuc", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ketThuc;

    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    public LichTrongPhong() {
        this.trangThai = "Trống";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public LocalDateTime getBatDau() { return batDau; }
    public void setBatDau(LocalDateTime batDau) { this.batDau = batDau; }

    public LocalDateTime getKetThuc() { return ketThuc; }
    public void setKetThuc(LocalDateTime ketThuc) { this.ketThuc = ketThuc; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
