package Kamona.GAS.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historial")
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_historial;
    private Long id_usuario;
    private LocalDateTime fecha;
    private String accion;
    private String tabla_afectada;
    private String descripcion;
}