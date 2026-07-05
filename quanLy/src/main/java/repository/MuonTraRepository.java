package repository;

import entity.MuonTra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MuonTraRepository extends JpaRepository<MuonTra, Long> {
    
   @Query("SELECT b FROM MuonTra b WHERE " +
           "LOWER(b.tenHangHoa) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.hangHoaID) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%'))")
       List<MuonTra> findBySearchTerm(@Param("search") String search);
    
   @Query("SELECT b FROM MuonTra b WHERE b.hangHoaID = :hanghoaID")
       List<MuonTra> findByHanghoaID(@Param("hanghoaID") String hanghoaID);
    
   @Query("SELECT b FROM MuonTra b WHERE LOWER(b.tenKhachHang) = LOWER(:tenKhachHang)")
       List<MuonTra> findByTenKhachHang(@Param("tenKhachHang") String tenKhachHang);
    
   @Query("SELECT b FROM MuonTra b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
       List<MuonTra> findByNgayBanBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
   @Query("SELECT SUM(b.tongTien) FROM MuonTra b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
    Double getTongDoanhThuTheoThang(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
   @Query("SELECT COUNT(b) FROM MuonTra b WHERE b.ngayBan BETWEEN :startDate AND :endDate")
    Long getSoDonBanTheoThang(@Param("startDate") LocalDateTime startDate, 
                              @Param("endDate") LocalDateTime endDate);
}
