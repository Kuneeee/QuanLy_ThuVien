package entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sao_luu_du_lieu")
public class SaoLuuDuLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_file", length = 500)
    private String tenFile;

    @Column(name = "kich_thuoc")
    private Long kichThuoc;

    @Column(name = "loai", length = 50)
    private String loai;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    public SaoLuuDuLieu() {}

    public SaoLuuDuLieu(String tenFile, Long kichThuoc, String loai, String trangThai, String ghiChu) {
        this.tenFile = tenFile;
        this.kichThuoc = kichThuoc;
        this.loai = loai;
        this.trangThai = trangThai;
        this.ngayTao = LocalDateTime.now();
        this.ghiChu = ghiChu;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenFile() { return tenFile; }
    public void setTenFile(String tenFile) { this.tenFile = tenFile; }
    public Long getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(Long kichThuoc) { this.kichThuoc = kichThuoc; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
