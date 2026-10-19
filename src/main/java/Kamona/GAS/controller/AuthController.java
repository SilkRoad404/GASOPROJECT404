package Kamona.GAS.controller;

import Kamona.GAS.model.Usuario;
import Kamona.GAS.repository.AlumnoRepository;
import Kamona.GAS.repository.AsistenciaRepository;
import Kamona.GAS.repository.HistorialRepository;
import Kamona.GAS.repository.TrimestresRepository;
import Kamona.GAS.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsistenciaRepository asistenciaRepo;
    @Autowired private HistorialRepository historialRepo;
    @Autowired private TrimestresRepository trimestresRepo;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() { return "acceso-denegado"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        long totalAlumnos = alumnoRepo.count();
        long asistenciasHoy = asistenciaRepo.findByFechaDeIngreso(LocalDate.now()).size();
        String trimestresActivo = trimestresRepo.findByActivoTrue()
            .map(t -> t.getNombre()).orElse("Ninguno");
        Usuario usuarioActual = usuarioService.buscarPorUsername(auth.getName());

        model.addAttribute("totalAlumnos", totalAlumnos);
        model.addAttribute("asistenciasHoy", asistenciasHoy);
        model.addAttribute("trimestresActivo", trimestresActivo);
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuarioActual", usuarioActual);
        return "dashboard";
    }
}