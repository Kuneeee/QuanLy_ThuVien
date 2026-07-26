package repository;

import entity.SaoLuuDuLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaoLuuDuLieuRepository extends JpaRepository<SaoLuuDuLieu, Long> {
    List<SaoLuuDuLieu> findAllByOrderByNgayTaoDesc();
    List<SaoLuuDuLieu> findByLoaiOrderByNgayTaoDesc(String loai);
}
