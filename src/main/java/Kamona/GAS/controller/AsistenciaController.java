package Kamona.GAS.controller;

import Kamona.GAS.model.Asistencia;
import Kamona.GAS.model.Alumno;
import Kamona.GAS.repository.AlumnoRepository;
import Kamona.GAS.repository.AsistenciaRepository;
import Kamona.GAS.repository.TrimestresRepository;
import Kamona.GAS.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired private AsistenciaRepository repo;
    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private TrimestresRepository trimestresRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    @GetMapping
    public String listar(@RequestParam(required = false) Long trimestre,
                         @RequestParam(required = false) String seccion,
                         @RequestParam(required = false) String grado,
                         Model model) {

        List<Alumno> todosAlumnos = alumnoRepo.findAll();

        // Filtrar alumnos por sección y/o grado
        List<Alumno> alumnosFiltrados = todosAlumnos.stream()
            .filter(a -> (seccion == null || seccion.isEmpty() || a.getSeccion().equals(seccion)))
            .filter(a -> (grado == null || grado.isEmpty() || a.getGrado().equals(grado)))
            .collect(Collectors.toList());

        List<Long> idsAlumnos = alumnosFiltrados.stream()
            .map(Alumno::getId_alumno)
            .collect(Collectors.toList());

        // Obtener asistencias filtradas
        List<Asistencia> asistencias = repo.filtrar(trimestre, null)
            .stream()
            .filter(a -> idsAlumnos.contains(a.getId_alumno()))
            .collect(Collectors.toList());

        // Mapa id_alumno -> nombre completo
        java.util.Map<Long, String> nombresAlumnos = new java.util.HashMap<>();
        todosAlumnos.forEach(a ->
            nombresAlumnos.put(a.getId_alumno(), a.getNombres() + " " + a.getApellidos())
        );

        // Mapa id_alumno -> seccion
        java.util.Map<Long, String> seccionAlumnos = new java.util.HashMap<>();
        todosAlumnos.forEach(a ->
            seccionAlumnos.put(a.getId_alumno(), a.getSeccion())
        );

        // Mapa id_alumno -> grado
        java.util.Map<Long, String> gradoAlumnos = new java.util.HashMap<>();
        todosAlumnos.forEach(a ->
            gradoAlumnos.put(a.getId_alumno(), a.getGrado())
        );

        // Mapa id_trimestre -> nombre
        java.util.Map<Long, String> nombresTrimestres = new java.util.HashMap<>();
        trimestresRepo.findAll().forEach(t ->
            nombresTrimestres.put(t.getId_trimestre(), t.getNombre())
        );

        // Listas únicas para los filtros
        List<String> secciones = todosAlumnos.stream()
            .map(Alumno::getSeccion).distinct().sorted().collect(Collectors.toList());

        List<String> grados = todosAlumnos.stream()
            .map(Alumno::getGrado).distinct().sorted().collect(Collectors.toList());

        model.addAttribute("asistencias", asistencias);
        model.addAttribute("alumnos", todosAlumnos);
        model.addAttribute("trimestres", trimestresRepo.findAll());
        model.addAttribute("nombresAlumnos", nombresAlumnos);
        model.addAttribute("seccionAlumnos", seccionAlumnos);
        model.addAttribute("gradoAlumnos", gradoAlumnos);
        model.addAttribute("nombresTrimestres", nombresTrimestres);
        model.addAttribute("trimestreSeleccionado", trimestre);
        model.addAttribute("seccionSeleccionada", seccion);
        model.addAttribute("gradoSeleccionado", grado);
        model.addAttribute("secciones", secciones);
        model.addAttribute("grados", grados);

        return "asistencias";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("alumnos", alumnoRepo.findAll());
        model.addAttribute("trimestres", trimestresRepo.findAll());
        return "asistencia-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Asistencia asistencia, Authentication auth) {
        String username = auth.getName();
        usuarioRepo.findByUsername(username).ifPresent(u -> asistencia.setCreado_por(u.getId()));
        repo.save(asistencia);
        return "redirect:/asistencias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("asistencia", repo.findById(id).orElseThrow());
        model.addAttribute("alumnos", alumnoRepo.findAll());
        model.addAttribute("trimestres", trimestresRepo.findAll());
        return "asistencia-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/asistencias";
    }
}