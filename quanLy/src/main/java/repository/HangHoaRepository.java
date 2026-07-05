package repository;

import entity.HangHoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, String> {
    
    @Query("SELECT h FROM HangHoa h WHERE " +
           "LOWER(h.tenHangHoa) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.hanghoaID) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.nhaSanXuat) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<HangHoa> findBySearchTerm(@Param("search") String search);
    
    @Query("SELECT h FROM HangHoa h WHERE LOWER(h.loaiHangHoa) = LOWER(:loai)")
    List<HangHoa> findByLoaiHangHoa(@Param("loai") String loai);
    
    @Query("SELECT h FROM HangHoa h WHERE h.soLuongHangHoa <= :threshold")
    List<HangHoa> findHangHoaSapHet(@Param("threshold") Integer threshold);
    
    @Query("SELECT DISTINCT h.loaiHangHoa FROM HangHoa h WHERE h.loaiHangHoa IS NOT NULL")
    List<String> findAllLoaiHangHoa();
    
    @Query("SELECT h FROM HangHoa h WHERE h.namSanXuat = :nam")
    List<HangHoa> findByNamSanXuat(@Param("nam") Integer nam);
}
