package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Entity class đại diện cho thông tin nhập hàng
 * Tương thích với NhapController và tránh xung đột với HangHoaController, BanController
 */
@Entity
@Table(name = "nhap_hang")
public class Nhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhap_id")
    private Long nhapId;
    
    // Thuộc tính chính - theo NhapController
    @Column(name = "hanghoa_id", nullable = false, length = 50)
    private String hanghoaID;        // ID hàng hóa - khớp với NhapController
    
    @Column(name = "ten_hang_hoa", nullable = false, length = 200)
    private String tenHangHoa;       // Tên hàng hóa - khớp với NhapController  
    
    @Column(name = "so_luong_nhap", nullable = false)
    private Integer soLuongNhap;        // Số lượng nhập - khớp với NhapController
    
    @Column(name = "gia_nhap", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaNhap;          // Giá nhập - khớp với NhapController
    
    @Column(name = "ngay_nhap", nullable = false)
    private LocalDateTime ngayNhap;         // Ngày nhập - khớp với NhapController
    
    // Thuộc tính bổ sung cho nhập hàng
    @Column(name = "nha_cung_cap", length = 200)
    private String nhaCungCap;       // Nhà cung cấp
    
    @Column(name = "nguoi_nhap", length = 100)
    private String nguoiNhap;        // Người thực hiện nhập
    
    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;           // Ghi chú
    
    @Column(name = "trang_thai", length = 50)
    private String trangThai;        // Trạng thái nhập
    
    @Column(name = "ma_nhap_hang", length = 50)
    private String maNhapHang;       // Mã phiếu nhập (khác với hanghoaID)
    
    // Constructor không tham số
    public Nhap() {
        this.ngayNhap = LocalDateTime.now();
        this.trangThai = "Đã nhập";
        this.ghiChu = "";
        this.nguoiNhap = "";
        this.nhaCungCap = "";
        this.maNhapHang = "";
    }
    
    
    // Constructor đầy đủ cho form
    public Nhap(String hanghoaID, String tenHangHoa, Integer soLuongNhap, BigDecimal giaNhap, 
                LocalDateTime ngayNhap, String nhaCungCap, String nguoiNhap, String ghiChu) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
        this.nhaCungCap = nhaCungCap;
        this.nguoiNhap = nguoiNhap;
        this.ghiChu = ghiChu;
        this.trangThai = "Đã nhập";
        this.maNhapHang = "NH" + hanghoaID;
    }
    
    // Constructor 6 tham số
    public Nhap(String hanghoaID, String tenHangHoa, Integer soLuongNhap, BigDecimal giaNhap, 
                LocalDateTime ngayNhap, String nhaCungCap) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
        this.nhaCungCap = nhaCungCap;
        this.trangThai = "Đã nhập";
        this.nguoiNhap = "";
        this.ghiChu = "";
        this.maNhapHang = "NH" + hanghoaID;
    }
    
    // Constructor legacy - tương thích với lỗi Maven ban đầu
    public Nhap(String maNhap, String maSanPham, Integer soLuong, Integer donGia, String nhaCungCap) {
        this.maNhapHang = maNhap;
        this.hanghoaID = maSanPham;
        this.tenHangHoa = maSanPham;
        this.soLuongNhap = soLuong;
        this.giaNhap = BigDecimal.valueOf(donGia != null ? donGia : 0);
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = LocalDateTime.now();
        this.trangThai = "Đã nhập";
        this.nguoiNhap = "";
        this.ghiChu = "";
    }
    
    // Getter và Setter methods - Theo đúng NhapController
    public Long getNhapId() {
        return nhapId;
    }
    
    public void setNhapId(Long nhapId) {
        this.nhapId = nhapId;
    }
    
    public String getHanghoaID() {
        return hanghoaID;
    }
    
    public void setHanghoaID(String hanghoaID) {
        this.hanghoaID = hanghoaID;
        // Tự động cập nhật mã nhập hàng
        if (this.maNhapHang == null || this.maNhapHang.isEmpty()) {
            this.maNhapHang = "NH" + hanghoaID;
        }
    }
    
    public String getTenHangHoa() {
        return tenHangHoa;
    }
    
    public void setTenHangHoa(String tenHangHoa) {
        this.tenHangHoa = tenHangHoa;
    }
    
    public Integer getSoLuongNhap() {
        return soLuongNhap;
    }
    
    public void setSoLuongNhap(Integer soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }
    
    public BigDecimal getGiaNhap() {
        return giaNhap;
    }
    
    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }
    
    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }
    
    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    
    public String getNhaCungCap() {
        return nhaCungCap;
    }
    
    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    
    public String getNguoiNhap() {
        return nguoiNhap;
    }
    
    public void setNguoiNhap(String nguoiNhap) {
        this.nguoiNhap = nguoiNhap;
    }
    
    public String getGhiChu() {
        return ghiChu;
    }
    
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public String getMaNhapHang() {
        return maNhapHang;
    }
    
    public void setMaNhapHang(String maNhapHang) {
        this.maNhapHang = maNhapHang;
    }
    
    // Phương thức tính toán - Sử dụng trong Controller
    public double getTongTienNhap() {
        if (soLuongNhap == null || giaNhap == null) return 0.0;
        return soLuongNhap * giaNhap.doubleValue();
    }
    
    // Alias cho tính tổng chi phí
    public double getTotalCost() {
        return getTongTienNhap();
    }
    
    // Phương thức kiểm tra hợp lệ
    public boolean isValid() {
        return hanghoaID != null && !hanghoaID.trim().isEmpty() &&
               tenHangHoa != null && !tenHangHoa.trim().isEmpty() &&
               soLuongNhap != null && soLuongNhap > 0 && 
               giaNhap != null && giaNhap.compareTo(BigDecimal.ZERO) > 0 &&
               ngayNhap != null;
    }
    
    // Phương thức format giá tiền (cho hiển thị)
    public String getFormattedGiaNhap() {
        return String.format("%,.0f VND", giaNhap.doubleValue());
    }
    
    // Phương thức format tổng tiền (cho hiển thị)
    public String getFormattedTongTien() {
        return String.format("%,.0f VND", getTongTienNhap());
    }
    
    // Phương thức kiểm tra ngày nhập gần đây (trong 30 ngày)
    public boolean isRecentImport() {
        try {
            LocalDate importDate = ngayNhap.toLocalDate();
            LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
            return importDate.isAfter(thirtyDaysAgo);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Phương thức so sánh với hàng hóa khác (để tránh trùng lặp)
    public boolean isDuplicateWith(String otherHanghoaID, String otherNgayNhap) {
        return this.hanghoaID != null && this.hanghoaID.equals(otherHanghoaID) &&
               this.ngayNhap != null && this.ngayNhap.equals(otherNgayNhap);
    }
    
    // Override toString method
    @Override
    public String toString() {
        return "Nhap{" +
                "maNhapHang='" + maNhapHang + '\'' +
                ", hanghoaID='" + hanghoaID + '\'' +
                ", tenHangHoa='" + tenHangHoa + '\'' +
                ", soLuongNhap=" + soLuongNhap +
                ", giaNhap=" + giaNhap +
                ", ngayNhap='" + ngayNhap + '\'' +
                ", nhaCungCap='" + nhaCungCap + '\'' +
                ", nguoiNhap='" + nguoiNhap + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", tongTienNhap=" + getTongTienNhap() +
                '}';
    }
    
    // Override equals method - Sử dụng maNhapHang làm key chính
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Nhap nhap = (Nhap) obj;
        
        // Ưu tiên so sánh bằng maNhapHang
        if (maNhapHang != null && nhap.maNhapHang != null) {
            return maNhapHang.equals(nhap.maNhapHang);
        }
        
        // Fallback: so sánh bằng hanghoaID
        return hanghoaID != null ? hanghoaID.equals(nhap.hanghoaID) : nhap.hanghoaID == null;
    }
    
    // Override hashCode method
    @Override
    public int hashCode() {
        if (maNhapHang != null) {
            return maNhapHang.hashCode();
        }
        return hanghoaID != null ? hanghoaID.hashCode() : 0;
    }
    
    // Phương thức so sánh theo ngày (cho sắp xếp)
    public int compareByDate(Nhap other) {
        if (this.ngayNhap == null || other.ngayNhap == null) {
            return 0;
        }
        return this.ngayNhap.compareTo(other.ngayNhap);
    }
    
    // Phương thức so sánh theo giá (cho sắp xếp)
    public int compareByPrice(Nhap other) {
        return this.giaNhap.compareTo(other.giaNhap);
    }
    
    // Phương thức so sánh theo số lượng (cho sắp xếp)
    public int compareByQuantity(Nhap other) {
        return Integer.compare(this.soLuongNhap, other.soLuongNhap);
    }
}