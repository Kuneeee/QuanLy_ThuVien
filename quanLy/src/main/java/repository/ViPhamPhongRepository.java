package repository;

import entity.ViPhamPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViPhamPhongRepository extends JpaRepository<ViPhamPhong, Long> {
    @Query("SELECT v FROM ViPhamPhong v WHERE LOWER(v.loaiViPham) = LOWER(:loaiViPham) ORDER BY v.ngayGhiNhan DESC")
    List<ViPhamPhong> findByLoaiViPhamOrderByNgayGhiNhanDesc(@Param("loaiViPham") String loaiViPham);
}
