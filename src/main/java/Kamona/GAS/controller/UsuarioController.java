package Kamona.GAS.controller;

import Kamona.GAS.model.Usuario;
import Kamona.GAS.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(Model model, Authentication auth) {
        Usuario actual = usuarioService.buscarPorUsername(auth.getName());
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuarioActual", actual);
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Authentication auth, RedirectAttributes ra) {
        Usuario actual = usuarioService.buscarPorUsername(auth.getName());
        if (!actual.getId().equals(1L)) {
            ra.addFlashAttribute("error", "Solo el administrador principal puede agregar usuarios");
            return "redirect:/dashboard";
        }
        return "usuario-confirmar";
    }

    @GetMapping("/confirmar-editar/{id}")
    public String confirmarEditar(@PathVariable Long id, Authentication auth,
                                  Model model, RedirectAttributes ra) {
        Usuario target = usuarioService.buscarPorId(id);
        Usuario actual = usuarioService.buscarPorUsername(auth.getName());
        if (target.getEsEditable() == null || !target.getEsEditable()) {
            if (!actual.getId().equals(1L)) {
                ra.addFlashAttribute("error", "No tienes permiso para editar este usuario");
                return "redirect:/dashboard";
            }
        }
        model.addAttribute("editarId", id);
        return "usuario-confirmar";
    }

    @PostMapping("/verificar")
    public String verificar(@RequestParam String adminPassword,
                            @RequestParam(required = false) Long editarId,
                            Authentication auth,
                            RedirectAttributes ra) {
        Usuario admin = usuarioService.buscarPorUsername(auth.getName());
        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            ra.addFlashAttribute("error", "Contrasena incorrecta");
            if (editarId != null) {
                return "redirect:/usuarios/confirmar-editar/" + editarId;
            }
            return "redirect:/usuarios/nuevo";
        }
        if (editarId != null) {
            return "redirect:/usuarios/editar/" + editarId;
        }
        return "redirect:/usuarios/form";
    }

    @GetMapping("/form")
    public String formNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "usuario-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
                          @RequestParam(required = false) String passwordNuevo,
                          RedirectAttributes ra) {
        try {
            if (usuario.getId() != null) {
                Usuario existente = usuarioService.buscarPorId(usuario.getId());
                existente.setUsername(usuario.getUsername());
                existente.setRol(usuario.getRol());
                if (passwordNuevo != null && !passwordNuevo.isBlank()) {
                    existente.setPassword(passwordEncoder.encode(passwordNuevo));
                }
                usuarioService.guardarDirecto(existente);
            } else {
                if (passwordNuevo == null || passwordNuevo.isBlank()) {
                    ra.addFlashAttribute("error", "La contrasena es obligatoria");
                    return "redirect:/usuarios/form";
                }
                usuario.setPassword(passwordEncoder.encode(passwordNuevo));
                usuario.setEsEditable(true);
                usuarioService.guardarDirecto(usuario);
            }
            ra.addFlashAttribute("mensaje", "Usuario guardado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "El username ya existe, elige otro");
            return "redirect:/usuarios/form";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        Usuario target = usuarioService.buscarPorId(id);
        Usuario actual = usuarioService.buscarPorUsername(auth.getName());
        if (target.getEsEditable() == null || !target.getEsEditable()) {
            if (!actual.getId().equals(1L)) {
                ra.addFlashAttribute("error", "No tienes permiso para eliminar este usuario");
                return "redirect:/dashboard";
            }
        }
        usuarioService.eliminar(id);
        ra.addFlashAttribute("mensaje", "Usuario eliminado");
        return "redirect:/dashboard";
    }

    @GetMapping("/toggle-editable/{id}")
    public String toggleEditable(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        Usuario actual = usuarioService.buscarPorUsername(auth.getName());
        if (!actual.getId().equals(1L)) {
            ra.addFlashAttribute("error", "Solo el administrador principal puede cambiar esto");
            return "redirect:/dashboard";
        }
        Usuario target = usuarioService.buscarPorId(id);
        if (target.getId().equals(1L)) {
            ra.addFlashAttribute("error", "No puedes modificar el admin principal");
            return "redirect:/dashboard";
        }
        target.setEsEditable(!Boolean.TRUE.equals(target.getEsEditable()));
        usuarioService.guardarDirecto(target);
        ra.addFlashAttribute("mensaje", "Permisos actualizados");
        return "redirect:/dashboard";
    }
}