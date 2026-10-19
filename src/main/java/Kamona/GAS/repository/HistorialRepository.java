package Kamona.GAS.repository;

import Kamona.GAS.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
    List<Historial> findTop5ByOrderByFechaDesc();
}