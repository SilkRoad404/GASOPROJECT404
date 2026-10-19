package Kamona.GAS.repository;

import Kamona.GAS.model.Trimestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrimestresRepository extends JpaRepository<Trimestre, Long> {
    Optional<Trimestre> findByActivoTrue();

    @Modifying
    @Query("UPDATE Trimestre t SET t.activo = false")
    void desactivarTodos();
}