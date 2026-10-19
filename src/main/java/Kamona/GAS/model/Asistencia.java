package Kamona.GAS.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "asistencias")
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_asistencia;
    private Long id_alumno;
    private Long id_trimestre;
    private String estado;
    private String observacion;
    private Long creado_por;

    @Column(name = "fecha_de_ingreso")
    private LocalDate fechaDeIngreso;
}