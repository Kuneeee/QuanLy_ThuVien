package repository;

import entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepositoryJPA extends JpaRepository<KhachHang, String> {
    
    // Find customers by name containing (case insensitive)
    List<KhachHang> findByTenKhachHangContainingIgnoreCase(String tenKhachHang);
    
    // Find customers by phone number
    List<KhachHang> findBySoDienThoai(String soDienThoai);
    
    // Find customers by email
    List<KhachHang> findByEmail(String email);
    
    // Find customers by type
    List<KhachHang> findByLoaiKhachHang(String loaiKhachHang);
    
    // Search customers by multiple criteria
    @Query("SELECT c FROM KhachHang c WHERE " +
           "(:search IS NULL OR " +
           "LOWER(c.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.soDienThoai LIKE CONCAT('%', :search, '%') OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<KhachHang> searchCustomers(@Param("search") String search);
    
    // Count customers by type
    @Query("SELECT COUNT(c) FROM KhachHang c WHERE c.loaiKhachHang = :type")
    long countByLoaiKhachHang(@Param("type") String type);
}
