package Kamona.GAS.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "trimestres")
public class Trimestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_trimestre;
    private String nombre;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private Boolean activo;
}