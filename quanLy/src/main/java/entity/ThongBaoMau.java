package entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao_mau")
public class ThongBaoMau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thong_bao_id")
    private Long id;

    @Column(name = "ma_mau", length = 50)
    private String maMau;

    @Column(name = "loai_thong_bao", nullable = false, length = 100)
    private String loaiThongBao;

    @Column(name = "tieu_de", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "doi_tuong", length = 100)
    private String doiTuong;

    @Column(name = "kenh_gui", length = 100)
    private String kenhGui;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "ngay_gui")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayGui;

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayCapNhat;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaMau() { return maMau; }
    public void setMaMau(String maMau) { this.maMau = maMau; }

    public String getLoaiThongBao() { return loaiThongBao; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getDoiTuong() { return doiTuong; }
    public void setDoiTuong(String doiTuong) { this.doiTuong = doiTuong; }

    public String getKenhGui() { return kenhGui; }
    public void setKenhGui(String kenhGui) { this.kenhGui = kenhGui; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LocalDateTime getNgayGui() { return ngayGui; }
    public void setNgayGui(LocalDateTime ngayGui) { this.ngayGui = ngayGui; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}