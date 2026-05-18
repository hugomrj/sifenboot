package org.sifenboot.app.admin.referencia_geografica.controller;

import org.sifenboot.app.admin.referencia_geografica.service.DistritoService;
import org.sifenboot.app.admin.referencia_geografica.service.LocalidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app/common/geo") // URL base genérica
public class ReferenciaGeograficaViewController {

    private final DistritoService distritoService;
    private final LocalidadService localidadService;

    public ReferenciaGeograficaViewController(DistritoService distritoService, LocalidadService localidadService) {
        this.distritoService = distritoService;
        this.localidadService = localidadService;
    }

    @GetMapping("/distritos")
    public String getDistritos(
            // Aceptamos el nombre real que envía el formulario
            @RequestParam(value = "departamento.id", required = false) Integer depId,
            Model model) {

        if (depId == null) {
            model.addAttribute("options", List.of());
        } else {
            model.addAttribute("options", distritoService.findByDepartamentoId(depId));
        }
        return "ui/common/fragments :: select-options";
    }

    @GetMapping("/localidades")
    public String getLocalidades(
            // Aceptamos el nombre real que envía el formulario
            @RequestParam(value = "distrito.id", required = false) Integer distId,
            Model model) {

        if (distId == null) {
            model.addAttribute("options", List.of());
        } else {
            model.addAttribute("options", localidadService.findByDistritoId(distId));
        }
        return "ui/common/fragments :: select-options";
    }
}