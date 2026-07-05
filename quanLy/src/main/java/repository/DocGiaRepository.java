package repository;

import entity.DocGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocGiaRepository extends JpaRepository<DocGia, String> {
    
    // Find customers by name containing (case insensitive)
    List<DocGia> findByTenKhachHangContainingIgnoreCase(String tenKhachHang);
    
    // Find customers by phone number
    List<DocGia> findBySoDienThoai(String soDienThoai);
    
    // Find customers by email
    List<DocGia> findByEmail(String email);
    
    // Find customers by type
    List<DocGia> findByLoaiKhachHang(String loaiKhachHang);
    
    // Find VIP customers (high value customers)
    @Query("SELECT k FROM DocGia k WHERE k.loaiKhachHang = 'VIP' OR k.tongChiTieu > 1000000")
    List<DocGia> findVipCustomers();
    
    // Search customers by multiple criteria
        @Query("SELECT k FROM DocGia k WHERE " +
           "(:search IS NULL OR " +
           "LOWER(k.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "k.soDienThoai LIKE CONCAT('%', :search, '%') OR " +
           "LOWER(k.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<DocGia> searchCustomers(@Param("search") String search);
    
    // Count customers by type
    @Query("SELECT COUNT(k) FROM DocGia k WHERE k.loaiKhachHang = :loai")
    long countByLoaiKhachHang(@Param("loai") String loai);
    
    // Find top customers by total spent
    @Query("SELECT k FROM DocGia k ORDER BY k.tongChiTieu DESC")
    List<DocGia> findTopCustomersBySpent();
}
