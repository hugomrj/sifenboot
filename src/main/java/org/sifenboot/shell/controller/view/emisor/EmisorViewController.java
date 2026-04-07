    package org.sifenboot.shell.controller.view.emisor;

    import org.sifenboot.shell.model.Emisor;
    import org.sifenboot.shell.service.EmisorService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/app/emisores/view")
    public class EmisorViewController {

        private final EmisorService emisorService;

        public EmisorViewController(EmisorService emisorService) {
            this.emisorService = emisorService;
        }

        // Listado principal
        @GetMapping("/list")
        public String listPage(Model model) {
            model.addAttribute("emisores", emisorService.findAll());
            return "ui/emisor/list";
        }

        // Formulario para NUEVO emisor
        @GetMapping("/new")
        public String formPage(Model model) {
            Emisor emisor = new Emisor();
            // Seteamos el default de ambiente para el radio/select del form
            emisor.setAmbiente("test");

            model.addAttribute("emisor", emisor);
            model.addAttribute("titulo", "Nuevo Emisor");
            return "ui/emisor/form";
        }

        // Formulario para EDITAR emisor existente
        @GetMapping("/edit/{id}")
        public String editPage(@PathVariable Long id, Model model) {
            Emisor emisor = emisorService.findById(id);
            model.addAttribute("emisor", emisor);
            model.addAttribute("titulo", "Editar Emisor: " + emisor.getRazonSocial());
            return "ui/emisor/form";
        }

        // Acción de GUARDAR (POST) - Ajustada para HTMX
        @PostMapping("/save")
        public String saveAction(@ModelAttribute("emisor") Emisor emisor, Model model) {
            // El service ahora valida ambiente, idCsc y csc
            emisorService.save(emisor);

            // En lugar de redirect, devolvemos la lista actualizada para el hx-target
            model.addAttribute("emisores", emisorService.findAll());
            model.addAttribute("mensaje", "Emisor guardado con éxito");
            return "ui/emisor/list";
        }

        @GetMapping("/details/{id}")
        public String detailsPage(@PathVariable Long id, Model model) {
            Emisor emisor = emisorService.findById(id);
            model.addAttribute("emisor", emisor);
            model.addAttribute("titulo", "Consulta de Emisor");
            model.addAttribute("readOnly", true);
            return "ui/emisor/form";
        }

        // Acción de ELIMINAR - Ajustada para estándares HTMX
        @DeleteMapping("/delete/{id}")
        public String deleteAction(@PathVariable Long id, Model model) {
            emisorService.deleteById(id);

            // Retornamos la lista para que HTMX actualice el #main-content
            model.addAttribute("emisores", emisorService.findAll());
            return "ui/emisor/list";
        }
    }