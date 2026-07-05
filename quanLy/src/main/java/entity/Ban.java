package entity;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "ban_hang")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Long banId;
    
    @Column(name = "ban_code", length = 50)
    private String banCode;
    
    @Column(name = "hanghoa_id", nullable = false, length = 50)
    private String hangHoaID;
    
    @Column(name = "ten_hang_hoa", nullable = false, length = 200)
    private String tenHangHoa;
    
    @Column(name = "ten_khach_hang", length = 200)
    private String tenKhachHang;
    
    @Column(name = "khach_hang", length = 200)
    private String khachHang;
    
    @Column(name = "ngay_ban", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayBan;
    
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;
    
    @Column(name = "gia_ban", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaBan;
    
    @Column(name = "so_luong_ban", nullable = false)
    private Integer soLuongBan;
    
    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;
    
    @Column(name = "gia_nhap", precision = 15, scale = 2)
    private BigDecimal giaNhap;

    public Ban() {}

    public Ban(String banCode, String hangHoaID, String tenHangHoa, String tenKhachHang, LocalDateTime ngayBan, BigDecimal giaBan, Integer soLuongBan) {
        this.banCode = banCode;
        this.hangHoaID = hangHoaID;
        this.tenHangHoa = tenHangHoa;
        this.tenKhachHang = tenKhachHang;
        this.khachHang = tenKhachHang;
        this.ngayBan = ngayBan;
        this.giaBan = giaBan;
        this.soLuongBan = soLuongBan;
        this.tongTien = giaBan.multiply(BigDecimal.valueOf(soLuongBan));
        this.ghiChu = "";
    }

    public Ban(String banCode, String hangHoaID, String tenHangHoa, String khachHang, LocalDateTime ngayBan, BigDecimal giaBan, Integer soLuongBan, String ghiChu) {
        this.banCode = banCode;
        this.hangHoaID = hangHoaID;
        this.tenHangHoa = tenHangHoa;
        this.khachHang = khachHang;
        this.tenKhachHang = khachHang;
        this.ngayBan = ngayBan;
        this.giaBan = giaBan;
        this.soLuongBan = soLuongBan;
        this.tongTien = giaBan.multiply(BigDecimal.valueOf(soLuongBan));
        this.ghiChu = ghiChu;
    }

    // Getters and Setters
    public Long getBanId() { return banId; }
    public void setBanId(Long banId) { this.banId = banId; }
    
    public String getBanCode() { return banCode; }
    public void setBanCode(String banCode) { this.banCode = banCode; }
    
    public String getHangHoaID() { return hangHoaID; }
    public void setHangHoaID(String hangHoaID) { this.hangHoaID = hangHoaID; }
    
    public String getTenHangHoa() { return tenHangHoa; }
    public void setTenHangHoa(String tenHangHoa) { this.tenHangHoa = tenHangHoa; }
    
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { 
        this.tenKhachHang = tenKhachHang;
        this.khachHang = tenKhachHang;
    }
    
    public String getKhachHang() { return khachHang; }
    public void setKhachHang(String khachHang) { 
        this.khachHang = khachHang;
        this.tenKhachHang = khachHang;
    }
    
    public LocalDateTime getNgayBan() { return ngayBan; }
    public void setNgayBan(LocalDateTime ngayBan) { this.ngayBan = ngayBan; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public BigDecimal getGiaBan() { return giaBan; }
    public void setGiaBan(BigDecimal giaBan) { this.giaBan = giaBan; }
    
    public Integer getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(Integer soLuongBan) { this.soLuongBan = soLuongBan; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public BigDecimal getGiaNhap() { return giaNhap; }
    public void setGiaNhap(BigDecimal giaNhap) { this.giaNhap = giaNhap; }

    // Business Logic Methods
    public BigDecimal tinhTongTienBan() {
        if (giaBan == null || soLuongBan == null) return BigDecimal.ZERO;
        this.tongTien = this.giaBan.multiply(BigDecimal.valueOf(this.soLuongBan));
        return this.tongTien;
    }

    public double tinhLoiNhuan() {
        if (this.giaNhap != null && this.giaNhap.compareTo(BigDecimal.ZERO) > 0 && 
            this.giaBan != null && this.soLuongBan != null) {
            return this.giaBan.subtract(this.giaNhap).multiply(BigDecimal.valueOf(this.soLuongBan)).doubleValue();
        }
        return 0;
    }

    public boolean kiemTraDonBanHang() {
        // Kiểm tra tính hợp lệ của đơn bán hàng
        if (this.banCode == null || this.banCode.trim().isEmpty()) {
            return false;
        }
        if (this.hangHoaID == null || this.hangHoaID.trim().isEmpty()) {
            return false;
        }
        if (this.khachHang == null || this.khachHang.trim().isEmpty()) {
            return false;
        }
        if (this.soLuongBan == null || this.soLuongBan <= 0) {
            return false;
        }
        if (this.giaBan == null || this.giaBan.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (this.ngayBan == null) {
            return false;
        }
        return true;
    }

    public boolean kiemTraNgayBan() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ngay = LocalDate.parse(this.ngayBan.toString().substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate hienTai = LocalDate.now();
            return !ngay.isAfter(hienTai);
        } catch (Exception e) {
            return false;
        }
    }

    public String layThongTinBan() {
        return String.format("ID: %s | Hàng hóa: %s | Khách hàng: %s | Ngày: %s | Số lượng: %d | Giá: %.2f | Tổng tiên: %.2f",
                this.banCode, this.tenHangHoa, this.khachHang, this.ngayBan, this.soLuongBan, this.giaBan.doubleValue(), this.tongTien.doubleValue());
    }

    @Override
    public String toString() {
        return "Ban{" +
                "banId=" + banId +
                ", banCode='" + banCode + '\'' +
                ", hangHoaID='" + hangHoaID + '\'' +
                ", tenHangHoa='" + tenHangHoa + '\'' +
                ", khachHang='" + khachHang + '\'' +
                ", ngayBan='" + ngayBan + '\'' +
                ", giaBan=" + giaBan +
                ", giaNhap=" + giaNhap +
                ", soLuongBan=" + soLuongBan +
                ", tongTien=" + tongTien +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
