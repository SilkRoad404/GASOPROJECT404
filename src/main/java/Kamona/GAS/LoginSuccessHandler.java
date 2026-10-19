package Kamona.GAS;

import Kamona.GAS.model.Historial;
import Kamona.GAS.repository.HistorialRepository;
import Kamona.GAS.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private HistorialRepository historialRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String username = authentication.getName();

        Long idUsuario = usuarioRepo.findByUsername(username)
            .map(u -> u.getId())
            .orElse(null);

        Historial h = new Historial();
        h.setId_usuario(idUsuario);
        h.setFecha(LocalDateTime.now());
        h.setAccion("LOGIN");
        h.setTabla_afectada("usuarios");
        h.setDescripcion("Inicio de sesion: Usuario #" + idUsuario);
        historialRepo.save(h);

        response.sendRedirect("/dashboard");
    }
}