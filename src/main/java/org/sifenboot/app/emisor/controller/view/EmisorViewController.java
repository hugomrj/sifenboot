    package org.sifenboot.app.emisor.controller.view;

    import org.sifenboot.app.departamento.model.Departamento;
    import org.sifenboot.app.emisor.model.Emisor;
    import org.sifenboot.app.emisor.model.EmisorConfiguracion;
    import org.sifenboot.app.departamento.service.DepartamentoService;
    import org.sifenboot.app.emisor.service.EmisorService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    @Controller
    @RequestMapping("/app/emisores/view")
    public class EmisorViewController {

        private final EmisorService emisorService;
        private final DepartamentoService departamentoService;

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

            // Inicialización de relaciones para evitar NullPointerException en la vista
            emisor.setConfiguracion(new EmisorConfiguracion());
            emisor.setDepartamento(new Departamento());

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

            // Forzar la carga si es Lazy para que Thymeleaf lo vea
            if (emisor.getDepartamento() != null) {
                emisor.getDepartamento().getDescripcion();
            }

            model.addAttribute("emisor", emisor);
            // IMPORTANTE: Debes pasar la lista de departamentos también para que el select
            // tenga contra qué comparar el ID 6
            model.addAttribute("departamentos", departamentoService.findAll());

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