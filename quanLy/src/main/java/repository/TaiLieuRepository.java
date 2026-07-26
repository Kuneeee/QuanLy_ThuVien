package repository;

import entity.TaiLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiLieuRepository extends JpaRepository<TaiLieu, String> {
    
    @Query("SELECT h FROM TaiLieu h WHERE " +
           "LOWER(h.tenHangHoa) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.hanghoaID) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.nhaSanXuat) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<TaiLieu> findBySearchTerm(@Param("search") String search);
    
    @Query("SELECT h FROM TaiLieu h WHERE LOWER(h.loaiHangHoa) = LOWER(:loai)")
    List<TaiLieu> findByLoaiHangHoa(@Param("loai") String loai);

        @Query("SELECT h FROM TaiLieu h WHERE " +
            "(:tenSach IS NULL OR :tenSach = '' OR LOWER(h.tenHangHoa) LIKE LOWER(CONCAT('%', :tenSach, '%'))) AND " +
            "(:tacGia IS NULL OR :tacGia = '' OR LOWER(COALESCE(h.nhaSanXuat, '')) LIKE LOWER(CONCAT('%', :tacGia, '%'))) AND " +
            "(:maTaiLieu IS NULL OR :maTaiLieu = '' OR LOWER(h.hanghoaID) LIKE LOWER(CONCAT('%', :maTaiLieu, '%'))) AND " +
            "(:nhaXuatBan IS NULL OR :nhaXuatBan = '' OR LOWER(COALESCE(h.nhaXuatBan, '')) LIKE LOWER(CONCAT('%', :nhaXuatBan, '%'))) AND " +
            "(:nganhHoc IS NULL OR :nganhHoc = '' OR LOWER(COALESCE(h.loaiHangHoa, '')) = LOWER(:nganhHoc))")
        List<TaiLieu> searchTaiLieu(@Param("tenSach") String tenSach,
                        @Param("tacGia") String tacGia,
                        @Param("maTaiLieu") String maTaiLieu,
                        @Param("nhaXuatBan") String nhaXuatBan,
                        @Param("nganhHoc") String nganhHoc);
    
    @Query("SELECT h FROM TaiLieu h WHERE h.soLuongHangHoa <= :threshold")
    List<TaiLieu> findHangHoaSapHet(@Param("threshold") Integer threshold);
    
    @Query("SELECT DISTINCT h.loaiHangHoa FROM TaiLieu h WHERE h.loaiHangHoa IS NOT NULL")
    List<String> findAllLoaiHangHoa();
    
    @Query("SELECT h FROM TaiLieu h WHERE h.namSanXuat = :nam")
    List<TaiLieu> findByNamSanXuat(@Param("nam") Integer nam);
}
