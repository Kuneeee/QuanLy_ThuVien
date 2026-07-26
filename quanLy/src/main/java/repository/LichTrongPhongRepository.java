package repository;

import entity.LichTrongPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichTrongPhongRepository extends JpaRepository<LichTrongPhong, Long> {
    @Query("SELECT l FROM LichTrongPhong l WHERE LOWER(l.trangThai) = LOWER(:trangThai) ORDER BY l.batDau ASC")
    List<LichTrongPhong> findByTrangThaiOrderByBatDauAsc(@Param("trangThai") String trangThai);
}
