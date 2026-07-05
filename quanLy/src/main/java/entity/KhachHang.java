package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "khach_hang")
public class KhachHang {
    
    @Id
    @Column(name = "khach_hang_id", length = 50)
    private String khachHangId;
    
    @Column(name = "ten_khach_hang", nullable = false, length = 200)
    private String tenKhachHang;
    
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;
    
    @Column(name = "loai_khach_hang", length = 50)
    private String loaiKhachHang;
    
    @Column(name = "tong_da_mua", precision = 15, scale = 2)
    private BigDecimal tongDaMua;
    
    @Column(name = "so_don_hang")
    private Integer soDonHang;
    
    @Column(name = "tong_chi_tieu", precision = 15, scale = 2)
    private BigDecimal tongChiTieu;
    
    @Column(name = "diem_thuong")
    private Integer diemThuong;
    
    @Column(name = "ngay_tham_gia")
    private LocalDate ngayThamGia;
    
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;
    
    // Constructors
    public KhachHang() {}
    
    public KhachHang(String khachHangId, String tenKhachHang, String soDienThoai, String email,
                         String diaChi, String loaiKhachHang) {
        this.khachHangId = khachHangId;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.loaiKhachHang = loaiKhachHang;
        this.tongDaMua = BigDecimal.ZERO;
        this.soDonHang = 0;
        this.tongChiTieu = BigDecimal.ZERO;
        this.diemThuong = 0;
        this.ngayThamGia = LocalDate.now();
        this.ngayTao = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getKhachHangId() { return khachHangId; }
    public void setKhachHangId(String khachHangId) { this.khachHangId = khachHangId; }
    
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getLoaiKhachHang() { return loaiKhachHang; }
    public void setLoaiKhachHang(String loaiKhachHang) { this.loaiKhachHang = loaiKhachHang; }
    
    public BigDecimal getTongDaMua() { return tongDaMua; }
    public void setTongDaMua(BigDecimal tongDaMua) { this.tongDaMua = tongDaMua; }
    
    public Integer getSoDonHang() { return soDonHang; }
    public void setSoDonHang(Integer soDonHang) { this.soDonHang = soDonHang; }
    
    public BigDecimal getTongChiTieu() { return tongChiTieu; }
    public void setTongChiTieu(BigDecimal tongChiTieu) { this.tongChiTieu = tongChiTieu; }
    
    public Integer getDiemThuong() { return diemThuong; }
    public void setDiemThuong(Integer diemThuong) { this.diemThuong = diemThuong; }
    
    public LocalDate getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(LocalDate ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}
