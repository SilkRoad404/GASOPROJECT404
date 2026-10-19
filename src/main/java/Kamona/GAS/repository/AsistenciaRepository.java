package Kamona.GAS.repository;

import Kamona.GAS.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByFechaDeIngreso(LocalDate fecha);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.id_trimestre = :id")
    long countByTrimestre(@Param("id") Long id);

    @Query("SELECT a FROM Asistencia a WHERE (:trimestre IS NULL OR a.id_trimestre = :trimestre) AND (:alumno IS NULL OR a.id_alumno = :alumno)")
    List<Asistencia> filtrar(@Param("trimestre") Long trimestre, @Param("alumno") Long alumno);
}