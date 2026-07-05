package repository;

import entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, String> {
    
    // Find customers by name containing (case insensitive)
    List<KhachHang> findByTenKhachHangContainingIgnoreCase(String tenKhachHang);
    
    // Find customers by phone number
    List<KhachHang> findBySoDienThoai(String soDienThoai);
    
    // Find customers by email
    List<KhachHang> findByEmail(String email);
    
    // Find customers by type
    List<KhachHang> findByLoaiKhachHang(String loaiKhachHang);
    
    // Find VIP customers (high value customers)
    @Query("SELECT k FROM KhachHang k WHERE k.loaiKhachHang = 'VIP' OR k.tongChiTieu > 1000000")
    List<KhachHang> findVipCustomers();
    
    // Search customers by multiple criteria
    @Query("SELECT k FROM KhachHang k WHERE " +
           "(:search IS NULL OR " +
           "LOWER(k.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "k.soDienThoai LIKE CONCAT('%', :search, '%') OR " +
           "LOWER(k.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<KhachHang> searchCustomers(@Param("search") String search);
    
    // Count customers by type
    @Query("SELECT COUNT(k) FROM KhachHang k WHERE k.loaiKhachHang = :loai")
    long countByLoaiKhachHang(@Param("loai") String loai);
    
    // Find top customers by total spent
    @Query("SELECT k FROM KhachHang k ORDER BY k.tongChiTieu DESC")
    List<KhachHang> findTopCustomersBySpent();
}
