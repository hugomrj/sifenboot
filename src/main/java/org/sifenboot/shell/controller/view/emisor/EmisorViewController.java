    package org.sifenboot.shell.controller.view.emisor;

    import org.sifenboot.shell.model.Emisor;
    import org.sifenboot.shell.service.DepartamentoService;
    import org.sifenboot.shell.service.EmisorService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/app/emisores/view")
    public class EmisorViewController {

        private final EmisorService emisorService;
        private final DepartamentoService departamentoService; // Inyectar esto

        public EmisorViewController(EmisorService emisorService, DepartamentoService departamentoService) {
            this.emisorService = emisorService;
            this.departamentoService = departamentoService;
        }

        @GetMapping("/list")
        public String listPage(Model model) {
            // Obtenemos la lista de emisores para la tabla
            model.addAttribute("emisores", emisorService.findAll());
            // Retorna el fragmento de la lista
            return "ui/emisor/list";
        }

        @GetMapping("/new")
        public String formPage(Model model) {
            Emisor emisor = new Emisor();
            emisor.setAmbiente("test");

            model.addAttribute("emisor", emisor);
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("titulo", "Nuevo Emisor");
            return "ui/emisor/form";
        }

        @GetMapping("/edit/{id}")
        public String editPage(@PathVariable Long id, Model model) {
            Emisor emisor = emisorService.findById(id);
            model.addAttribute("emisor", emisor);
            model.addAttribute("departamentos", departamentoService.findAll());
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