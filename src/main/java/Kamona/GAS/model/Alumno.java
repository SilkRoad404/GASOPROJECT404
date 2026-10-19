package Kamona.GAS.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alumnos")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_alumno;
    private String nombres;
    private String apellidos;
    private String grado;
    private String seccion;
}