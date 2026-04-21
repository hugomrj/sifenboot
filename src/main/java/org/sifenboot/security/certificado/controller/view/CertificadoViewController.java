package org.sifenboot.security.certificado.controller.view;

import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.sifenboot.app.emisor.service.EmisorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/app/certificados")
public class CertificadoViewController {

    private final CertificadoService certificadoService;
    private final EmisorService emisorService;

    public CertificadoViewController(CertificadoService certificadoService, EmisorService emisorService) {
        this.certificadoService = certificadoService;
        this.emisorService = emisorService;
    }

    // Coincide con hx-get="/app/certificados"
    @GetMapping
    public String index(Model model) {
        model.addAttribute("emisores", emisorService.findAll());
        model.addAttribute("titulo", "Certificado Digital");
        return "ui/certificado/form";
    }


    // Carga la página principal (el contenedor)
    @GetMapping("/view/form")
    public String viewForm(Model model) {
        model.addAttribute("emisores", emisorService.findAll());
        model.addAttribute("titulo", "Gestión de Certificado Digital");

        // Inicializamos objetos vacíos para evitar que Thymeleaf falle al procesar el fragmento oculto
        model.addAttribute("certificado", new Certificado());
        model.addAttribute("isNew", true);

        return "ui/certificado/form";
    }

    @GetMapping("/load")
    public String loadByEmisor(@RequestParam("emisorId") Long emisorId, Model model) {
        if (emisorId == null) {
            model.addAttribute("isNew", true); // Agregar esto
            return "ui/certificado/form :: form-fields";
        }

        Optional<Certificado> certificadoOpt = certificadoService.getByEmisor(emisorId);

        if (certificadoOpt.isPresent()) {
            model.addAttribute("certificado", certificadoOpt.get());
            model.addAttribute("isNew", false); // Caso: Existe
        } else {
            Certificado nuevo = new Certificado();
            Emisor emisor = new Emisor();
            emisor.setId(emisorId);
            nuevo.setEmisor(emisor);
            model.addAttribute("certificado", nuevo);
            model.addAttribute("isNew", true); // Caso: Nuevo
        }

        return "ui/certificado/form :: form-fields";
    }


    @PostMapping("/save")
    public String saveAction(@ModelAttribute Certificado certificado,
                             @RequestParam("archivoP12") MultipartFile archivo,
                             Model model) throws IOException {

        // 1. Ejecutamos la lógica de guardado
        certificadoService.save(certificado, archivo);

        // 2. Preparamos el modelo para refrescar la vista
        model.addAttribute("mensaje", "Certificado vinculado exitosamente.");
        model.addAttribute("emisores", emisorService.findAll());

        // 3. Importante: Limpiamos el objeto para que el formulario
        // vuelva a su estado inicial tras el guardado exitoso
        model.addAttribute("certificado", new Certificado());
        model.addAttribute("isNew", true);

        // Retornamos la plantilla completa para que HTMX actualice el #main-content
        return "ui/certificado/form";
    }






    @DeleteMapping("/delete/{emisorId}")
    public String deleteAction(@PathVariable Long emisorId, Model model) {
        certificadoService.eliminarPorEmisor(emisorId);

        // Devolvemos el fragmento vacío para "limpiar" el formulario
        model.addAttribute("certificado", new Certificado());
        model.addAttribute("isNew", true);
        return "ui/certificado/form";
    }


}