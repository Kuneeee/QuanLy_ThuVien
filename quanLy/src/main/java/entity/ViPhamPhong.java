package entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vi_pham_phong")
public class ViPhamPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vi_pham_id")
    private Long id;

    @Column(name = "so_bien_lai", length = 50)
    private String soBienLai;

    @Column(name = "ma_doc_gia", nullable = false, length = 50)
    private String maDocGia;

    @Column(name = "ten_doc_gia", nullable = false, length = 200)
    private String tenDocGia;

    @Column(name = "ma_tai_lieu", length = 50)
    private String maTaiLieu;

    @Column(name = "ten_tai_lieu", length = 200)
    private String tenTaiLieu;

    @Column(name = "loai_vi_pham", nullable = false, length = 50)
    private String loaiViPham;

    @Column(name = "so_ngay_qua_han")
    private Integer soNgayQuaHan;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_tien_den_bu", precision = 15, scale = 2)
    private BigDecimal soTienDenBu;

    @Column(name = "so_tien_phat", precision = 15, scale = 2)
    private BigDecimal soTienPhat;

    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;

    @Column(name = "ngay_ghi_nhan")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayGhiNhan;

    public ViPhamPhong() {
        this.soNgayQuaHan = 0;
        this.soLuong = 1;
        this.soTienDenBu = BigDecimal.ZERO;
        this.soTienPhat = BigDecimal.ZERO;
        this.tongTien = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSoBienLai() { return soBienLai; }
    public void setSoBienLai(String soBienLai) { this.soBienLai = soBienLai; }

    public String getMaDocGia() { return maDocGia; }
    public void setMaDocGia(String maDocGia) { this.maDocGia = maDocGia; }

    public String getTenDocGia() { return tenDocGia; }
    public void setTenDocGia(String tenDocGia) { this.tenDocGia = tenDocGia; }

    public String getMaTaiLieu() { return maTaiLieu; }
    public void setMaTaiLieu(String maTaiLieu) { this.maTaiLieu = maTaiLieu; }

    public String getTenTaiLieu() { return tenTaiLieu; }
    public void setTenTaiLieu(String tenTaiLieu) { this.tenTaiLieu = tenTaiLieu; }

    public String getLoaiViPham() { return loaiViPham; }
    public void setLoaiViPham(String loaiViPham) { this.loaiViPham = loaiViPham; }

    public Integer getSoNgayQuaHan() { return soNgayQuaHan; }
    public void setSoNgayQuaHan(Integer soNgayQuaHan) { this.soNgayQuaHan = soNgayQuaHan; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public BigDecimal getSoTienDenBu() { return soTienDenBu; }
    public void setSoTienDenBu(BigDecimal soTienDenBu) { this.soTienDenBu = soTienDenBu; }

    public BigDecimal getSoTienPhat() { return soTienPhat; }
    public void setSoTienPhat(BigDecimal soTienPhat) { this.soTienPhat = soTienPhat; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public LocalDateTime getNgayGhiNhan() { return ngayGhiNhan; }
    public void setNgayGhiNhan(LocalDateTime ngayGhiNhan) { this.ngayGhiNhan = ngayGhiNhan; }
}
