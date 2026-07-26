package repository;

import entity.CauHinhHeThong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CauHinhHeThongRepository extends JpaRepository<CauHinhHeThong, Long> {
    Optional<CauHinhHeThong> findByMa(String ma);
    List<CauHinhHeThong> findByNhomOrderByMaAsc(String nhom);
}
