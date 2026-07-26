package repository;

import entity.DatPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatPhongRepository extends JpaRepository<DatPhong, Long> {
    @Query("SELECT d FROM DatPhong d WHERE LOWER(d.trangThai) = LOWER(:trangThai) ORDER BY d.thoiGianBatDau ASC")
    List<DatPhong> findByTrangThaiOrderByThoiGianBatDauAsc(@Param("trangThai") String trangThai);
}
