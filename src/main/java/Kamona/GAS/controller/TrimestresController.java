package Kamona.GAS.controller;

import Kamona.GAS.model.Trimestre;
import Kamona.GAS.repository.AsistenciaRepository;
import Kamona.GAS.repository.TrimestresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trimestres")
public class TrimestresController {

    @Autowired
    private TrimestresRepository repo;

    @Autowired
    private AsistenciaRepository asistenciaRepo;

    @GetMapping
    public String listar(Model model) {
        List<Trimestre> trimestres = repo.findAll();
        Map<Long, Long> conteos = new LinkedHashMap<>();
        for (Trimestre t : trimestres) {
        long count = asistenciaRepo.countByTrimestre(t.getId_trimestre());
            conteos.put(t.getId_trimestre(), count);
        }
        model.addAttribute("trimestres", trimestres);
        model.addAttribute("conteos", conteos);
        return "trimestres";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("trimestre", new Trimestre());
        return "trimestre-form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Trimestre trimestre) {
        repo.save(trimestre);
        return "redirect:/trimestres";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("trimestre", repo.findById(id).orElseThrow());
        return "trimestre-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/trimestres";
    }

    @Transactional
    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id) {
        repo.desactivarTodos();
        Trimestre t = repo.findById(id).orElseThrow();
        t.setActivo(true);
        repo.save(t);
        return "redirect:/trimestres";
    }
}