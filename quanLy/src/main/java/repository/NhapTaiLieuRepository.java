package repository;

import entity.NhapTaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NhapTaiLieuRepository extends JpaRepository<NhapTaiLieu, Long> {
    
   @Query("SELECT n FROM NhapTaiLieu n WHERE " +
           "LOWER(n.tenHangHoa) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(n.hanghoaID) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(n.nhaCungCap) LIKE LOWER(CONCAT('%', :search, '%'))")
       List<NhapTaiLieu> findBySearchTerm(@Param("search") String search);
    
   @Query("SELECT n FROM NhapTaiLieu n WHERE n.hanghoaID = :hanghoaID")
       List<NhapTaiLieu> findByHanghoaID(@Param("hanghoaID") String hanghoaID);
    
   @Query("SELECT n FROM NhapTaiLieu n WHERE n.ngayNhap BETWEEN :startDate AND :endDate")
       List<NhapTaiLieu> findByNgayNhapBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
   @Query("SELECT LOWER(n.nhaCungCap) FROM NhapTaiLieu n WHERE n.nhaCungCap IS NOT NULL GROUP BY LOWER(n.nhaCungCap)")
    List<String> findAllNhaCungCap();
    
   @Query("SELECT SUM(n.soLuongNhap * n.giaNhap) FROM NhapTaiLieu n WHERE n.ngayNhap BETWEEN :startDate AND :endDate")
    Double getTongTienNhapTheoThang(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}
