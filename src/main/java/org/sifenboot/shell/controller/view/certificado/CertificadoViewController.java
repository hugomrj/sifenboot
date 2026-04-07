package org.sifenboot.shell.controller.view.certificado;


import org.sifenboot.shell.model.Certificado;
import org.sifenboot.shell.service.CertificadoService;
import org.sifenboot.shell.service.EmisorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/app/certificados")
public class CertificadoViewController {

    private final EmisorService emisorService;
    private final CertificadoService certificadoService;

    public CertificadoViewController(EmisorService emisorService, CertificadoService certificadoService) {
        this.emisorService = emisorService;
        this.certificadoService = certificadoService;
    }

    /**
     * Carga la vista principal del formulario de certificados.
     * Como usamos 'file:./web/' en el yml, buscará en web/ui/certificado/form.html
     */
    @GetMapping
    public String viewCertificados(Model model) {
        // 1. Cargamos todos los emisores para el ComboBox
        model.addAttribute("emisores", emisorService.findAll());

        // 2. Pasamos un objeto vacío para el binding de Thymeleaf (th:object)
        model.addAttribute("certificado", new Certificado());

        // 3. Título de la página para el layout
        model.addAttribute("titulo", "Gestión de Certificados Digitales");

        return "ui/certificado/form";
    }
}