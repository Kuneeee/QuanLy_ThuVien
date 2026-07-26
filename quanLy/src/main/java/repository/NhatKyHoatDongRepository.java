package repository;

import entity.NhatKyHoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NhatKyHoatDongRepository extends JpaRepository<NhatKyHoatDong, Long> {
    List<NhatKyHoatDong> findByLoaiOrderByThoiGianDesc(String loai);
    List<NhatKyHoatDong> findByThoiGianBetweenOrderByThoiGianDesc(LocalDateTime from, LocalDateTime to);
    List<NhatKyHoatDong> findByNguoiThucHienOrderByThoiGianDesc(String nguoiThucHien);
    List<NhatKyHoatDong> findAllByOrderByThoiGianDesc();
}
