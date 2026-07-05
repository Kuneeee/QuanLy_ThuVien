package repository;

import entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    
    @Query("SELECT b FROM Ban b WHERE " +
           "LOWER(b.tenHangHoa) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.hangHoaID) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Ban> findBySearchTerm(@Param("search") String search);
    
    @Query("SELECT b FROM Ban b WHERE b.hangHoaID = :hanghoaID")
    List<Ban> findByHanghoaID(@Param("hanghoaID") String hanghoaID);
    
    @Query("SELECT b FROM Ban b WHERE LOWER(b.tenKhachHang) = LOWER(:tenKhachHang)")
    List<Ban> findByTenKhachHang(@Param("tenKhachHang") String tenKhachHang);
    
    @Query("SELECT b FROM Ban b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
    List<Ban> findByNgayBanBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(b.tongTien) FROM Ban b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
    Double getTongDoanhThuTheoThang(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Ban b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
    Long getSoDonBanTheoThang(@Param("startDate") LocalDateTime startDate, 
                              @Param("endDate") LocalDateTime endDate);
}
