    package org.sifenboot.shell.controller.view.emisor;

    import org.sifenboot.shell.model.Emisor;
    import org.sifenboot.shell.service.EmisorService; // Importamos tu servicio
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model; // Importante para pasar datos a Thymeleaf
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
            List<Emisor> lista = emisorService.findAll();
            model.addAttribute("emisores", lista);
            return "ui/emisor/list";
        }

        // Formulario para NUEVO emisor
        @GetMapping("/new")
        public String formPage(Model model) {
            // Pasamos un objeto vacío para que Thymeleaf haga el binding
            model.addAttribute("emisor", new Emisor());
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

        // Acción de GUARDAR (POST)
        @PostMapping("/save")
        public String saveAction(@ModelAttribute("emisor") Emisor emisor) {
            // El service normaliza y guarda (Insert o Update según el ID)
            emisorService.save(emisor);
            return "redirect:/app/emisores/view/list";
        }


        @GetMapping("/details/{id}")
        public String detailsPage(@PathVariable Long id, Model model) {
            Emisor emisor = emisorService.findById(id);
            model.addAttribute("emisor", emisor);
            model.addAttribute("titulo", "Consulta de Emisor");
            model.addAttribute("readOnly", true); // <--- Esta es la señal para el HTML
            return "ui/emisor/form";
        }


        // Acción de ELIMINAR
        @DeleteMapping("/delete/{id}") // Usamos DeleteMapping para ser más estándares
        public String deleteAction(@PathVariable Long id, Model model) {
            // 1. Borramos de la base de datos
            emisorService.deleteById(id);

            // 2. Buscamos la lista actualizada
            List<Emisor> lista = emisorService.findAll();
            model.addAttribute("emisores", lista);

            // 3. Retornamos SOLO el fragmento de la tabla (o la vista de lista)
            return "ui/emisor/list";
        }
    }