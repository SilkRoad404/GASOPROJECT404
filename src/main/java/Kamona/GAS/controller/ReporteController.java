package Kamona.GAS.controller;

import Kamona.GAS.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired private ReporteService reporteService;

    @GetMapping
    public String ver(Model model) {
        model.addAttribute("contenido", reporteService.generarReporteTexto());
        return "reportes";
    }

    @GetMapping("/enviar")
    public String enviar(RedirectAttributes ra) {
        try {
            reporteService.enviarReportePorCorreo();
            ra.addFlashAttribute("mensaje", "Reporte enviado a josueacostagarcia@gmail.com");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al enviar: " + e.getMessage());
        }
        return "redirect:/reportes";
    }
}
