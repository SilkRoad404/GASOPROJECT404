package Kamona.GAS.repository;

import Kamona.GAS.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByNombresContainingOrApellidosContaining(String nombres, String apellidos);
}