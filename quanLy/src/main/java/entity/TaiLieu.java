package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity chính cho quản lý tài liệu
 * Tích hợp đầy đủ tính năng từ entity gốc với Spring Boot
 */
@Entity
@Table(name = "tai_lieu")
public class TaiLieu {
    @Id
    @Column(name = "tai_lieu_id", length = 50)
    private String hanghoaID;
    
    @Column(name = "ten_tai_lieu", nullable = false, length = 200)
    private String tenHangHoa;
    
    @Column(name = "so_luong_ton", nullable = false)
    private Integer soLuongHangHoa;
    
    @Column(name = "tac_gia", length = 200)
    private String nhaSanXuat;
    
    @Column(name = "nam_xuat_ban")
    private Integer namSanXuat;
    
    @Column(name = "ngay_nhap")
    private LocalDateTime ngayNhap;
    
    @Column(name = "gia_nhap", precision = 15, scale = 2)
    private BigDecimal giaNhap;
    
    @Column(name = "the_loai", length = 100)
    private String loaiHangHoa;

    // Constructors
    public TaiLieu() {}

    public TaiLieu(String hanghoaID, String tenHangHoa, Integer soLuongHangHoa, LocalDateTime ngayNhap, BigDecimal giaNhap, String loaiHangHoa) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongHangHoa = soLuongHangHoa;
        this.ngayNhap = ngayNhap;
        this.giaNhap = giaNhap;
        this.loaiHangHoa = loaiHangHoa;
    }

    // Constructor đầy đủ với tất cả thuộc tính
    public TaiLieu(String hanghoaID, String tenHangHoa, Integer soLuongHangHoa, String nhaSanXuat, 
                   Integer namSanXuat, LocalDateTime ngayNhap, BigDecimal giaNhap, String loaiHangHoa) {
        this.hanghoaID = hanghoaID;
        this.tenHangHoa = tenHangHoa;
        this.soLuongHangHoa = soLuongHangHoa;
        this.nhaSanXuat = nhaSanXuat;
        this.namSanXuat = namSanXuat;
        this.ngayNhap = ngayNhap;
        this.giaNhap = giaNhap;
        this.loaiHangHoa = loaiHangHoa;
    }

    // Getters và Setters
    public String getHanghoaID() { return hanghoaID; }
    public void setHanghoaID(String hanghoaID) { this.hanghoaID = hanghoaID; }
    
    public String getTenHangHoa() { return tenHangHoa; }
    public void setTenHangHoa(String tenHangHoa) { this.tenHangHoa = tenHangHoa; }
    
    public Integer getSoLuongHangHoa() { return soLuongHangHoa; }
    public void setSoLuongHangHoa(Integer soLuongHangHoa) { this.soLuongHangHoa = soLuongHangHoa; }
    
    public String getNhaSanXuat() { return nhaSanXuat; }
    public void setNhaSanXuat(String nhaSanXuat) { this.nhaSanXuat = nhaSanXuat; }
    
    public Integer getNamSanXuat() { return namSanXuat; }
    public void setNamSanXuat(Integer namSanXuat) { this.namSanXuat = namSanXuat; }
    
    public LocalDateTime getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDateTime ngayNhap) { this.ngayNhap = ngayNhap; }
    
    public BigDecimal getGiaNhap() { return giaNhap; }
    public void setGiaNhap(BigDecimal giaNhap) { this.giaNhap = giaNhap; }
    
    public String getLoaiHangHoa() { return loaiHangHoa; }
    public void setLoaiHangHoa(String loaiHangHoa) { this.loaiHangHoa = loaiHangHoa; }

    // Business Logic Methods từ entity gốc
    
    /**
     * Hiển thị thông tin chi tiết hàng hóa
     */
    public void hienThiThongTin() {
        System.out.println("=== THÔNG TIN TÀI LIỆU ===");
        System.out.println("Mã tài liệu: " + hanghoaID);
        System.out.println("Tên tài liệu: " + tenHangHoa);
        System.out.println("Số lượng: " + soLuongHangHoa);
        System.out.println("Nhà sản xuất: " + (nhaSanXuat != null ? nhaSanXuat : "Chưa cập nhật"));
        System.out.println("Năm sản xuất: " + (namSanXuat > 0 ? namSanXuat : "Chưa cập nhật"));
        System.out.println("Ngày nhập: " + ngayNhap);
        System.out.println("Giá nhập: " + String.format("%.2f VND", giaNhap != null ? giaNhap.doubleValue() : 0.0));
        System.out.println("Loại tài liệu: " + loaiHangHoa);
        System.out.println("=============================");
    }

    /**
     * Kiểm tra hàng hóa có hết hạn hay không (dựa trên năm sản xuất)
     */
    public boolean kiemTraHetHan() {
        if (namSanXuat == null || namSanXuat <= 0) return false;
        int namHienTai = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int tuoiSanPham = namHienTai - namSanXuat;
        
        // Quy định hết hạn dựa trên loại tài liệu
        switch (loaiHangHoa != null ? loaiHangHoa.toLowerCase() : "") {
            case "thực phẩm":
            case "đồ ăn vặt":
                return tuoiSanPham > 2; // 2 năm
            case "điện tử":
                return tuoiSanPham > 5; // 5 năm
            case "quần áo":
                return tuoiSanPham > 10; // 10 năm
            default:
                return tuoiSanPham > 3; // Mặc định 3 năm
        }
    }

    /**
     * Tính giá trị tồn kho
     */
    public double tinhGiaTriTonKho() {
        if (soLuongHangHoa == null || giaNhap == null) return 0.0;
        return soLuongHangHoa * giaNhap.doubleValue();
    }

    /**
     * Kiểm tra hàng hóa có sắp hết không
     */
    public boolean kiemTraSapHet() {
        return soLuongHangHoa != null && soLuongHangHoa <= 5; // Ngưỡng cảnh báo: 5 bản
    }

    /**
     * Cập nhật số lượng hàng hóa (dùng cho nhập/xuất kho)
     */
    public boolean capNhatSoLuong(int soLuongThayDoi) {
        if (soLuongHangHoa == null) soLuongHangHoa = 0;
        int soLuongMoi = this.soLuongHangHoa + soLuongThayDoi;
        if (soLuongMoi < 0) {
            System.out.println("Lỗi: Không thể giảm số lượng xuống dưới 0!");
            return false;
        }
        this.soLuongHangHoa = soLuongMoi;
        return true;
    }

    /**
     * Validate dữ liệu hàng hóa
     */
    public boolean validateData() {
        if (hanghoaID == null || hanghoaID.trim().isEmpty()) {
            System.out.println("Lỗi: Mã tài liệu không được để trống!");
            return false;
        }
        if (tenHangHoa == null || tenHangHoa.trim().isEmpty()) {
            System.out.println("Lỗi: Tên tài liệu không được để trống!");
            return false;
        }
        if (soLuongHangHoa != null && soLuongHangHoa < 0) {
            System.out.println("Lỗi: Số lượng hàng hóa không được âm!");
            return false;
        }
        if (giaNhap != null && giaNhap.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Lỗi: Giá nhập không được âm!");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("TaiLieu{ID='%s', Ten='%s', SoLuong=%d, LoaiTaiLieu='%s', GiaNhap=%.2f}", 
                           hanghoaID, tenHangHoa, soLuongHangHoa, loaiHangHoa, 
                           giaNhap != null ? giaNhap.doubleValue() : 0.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TaiLieu taiLieu = (TaiLieu) obj;
        return hanghoaID != null ? hanghoaID.equals(taiLieu.hanghoaID) : taiLieu.hanghoaID == null;
    }

    @Override
    public int hashCode() {
        return hanghoaID != null ? hanghoaID.hashCode() : 0;
    }
}
