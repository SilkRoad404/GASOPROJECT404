package Kamona.GAS.service;

import Kamona.GAS.model.Alumno;
import Kamona.GAS.model.Asistencia;
import Kamona.GAS.repository.AlumnoRepository;
import Kamona.GAS.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired private AsistenciaRepository asistenciaRepo;
    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private JavaMailSender mailSender;

    private static final String DESTINATARIO = "josueacostagarcia@gmail.com";

    public String generarReporteTexto() {
        List<Asistencia> asistencias = asistenciaRepo.findAll();
        List<Alumno> alumnos = alumnoRepo.findAll();

        Map<Long, List<Asistencia>> porAlumno = asistencias.stream()
            .collect(Collectors.groupingBy(Asistencia::getId_alumno));

        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DE ASISTENCIA - GASOPROJECT404\n");
        sb.append("Total alumnos: ").append(alumnos.size()).append("\n");
        sb.append("Total registros de asistencia: ").append(asistencias.size()).append("\n\n");

        long presentes = asistencias.stream().filter(a -> "P".equals(a.getEstado())).count();
        long faltas = asistencias.stream().filter(a -> "F".equals(a.getEstado())).count();
        long permisos = asistencias.stream().filter(a -> "SP".equals(a.getEstado())).count();

        sb.append("Presentes: ").append(presentes).append("\n");
        sb.append("Faltas: ").append(faltas).append("\n");
        sb.append("Con permiso: ").append(permisos).append("\n\n");

        sb.append("--- Detalle por alumno ---\n");
        for (Alumno al : alumnos) {
            List<Asistencia> regs = porAlumno.getOrDefault(al.getId_alumno(), List.of());
            long p = regs.stream().filter(a -> "P".equals(a.getEstado())).count();
            long f = regs.stream().filter(a -> "F".equals(a.getEstado())).count();
            sb.append(al.getNombres()).append(" ").append(al.getApellidos())
              .append(" (").append(al.getGrado()).append(" ").append(al.getSeccion()).append(") - ")
              .append("P:").append(p).append(" F:").append(f).append("\n");
        }
        return sb.toString();
    }

    public void enviarReportePorCorreo() {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(DESTINATARIO);
        mensaje.setSubject("Reporte de asistencia - GASOPROJECT404");
        mensaje.setText(generarReporteTexto());
        mailSender.send(mensaje);
    }
}