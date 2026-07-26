package entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cau_hinh_he_thong")
public class CauHinhHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma", length = 100, nullable = false, unique = true)
    private String ma;

    @Column(name = "gia_tri", columnDefinition = "TEXT")
    private String giaTri;

    @Column(name = "mo_ta", length = 500)
    private String moTa;

    @Column(name = "nhom", length = 100)
    private String nhom;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    public CauHinhHeThong() {}

    public CauHinhHeThong(String ma, String giaTri, String moTa, String nhom) {
        this.ma = ma;
        this.giaTri = giaTri;
        this.moTa = moTa;
        this.nhom = nhom;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMa() { return ma; }
    public void setMa(String ma) { this.ma = ma; }
    public String getGiaTri() { return giaTri; }
    public void setGiaTri(String giaTri) { this.giaTri = giaTri; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getNhom() { return nhom; }
    public void setNhom(String nhom) { this.nhom = nhom; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}
