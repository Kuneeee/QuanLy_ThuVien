package repository;

import entity.ThongBaoMau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThongBaoMauRepository extends JpaRepository<ThongBaoMau, Long> {
}