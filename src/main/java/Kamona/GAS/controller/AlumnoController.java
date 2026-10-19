package Kamona.GAS.controller;

import Kamona.GAS.model.Alumno;
import Kamona.GAS.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoRepository repo;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alumnos", repo.findAll());
        return "alumnos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("alumno", new Alumno());
        return "alumno-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Alumno alumno) {
        repo.save(alumno);
        return "redirect:/alumnos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("alumno", repo.findById(id).orElseThrow());
        return "alumno-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/alumnos";
    }
}