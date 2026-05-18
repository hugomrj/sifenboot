    package org.sifenboot.app.admin.emisor.controller.view;

    import org.sifenboot.app.admin.referencia_geografica.model.Departamento;
    import org.sifenboot.app.admin.emisor.model.Emisor;
    import org.sifenboot.app.admin.emisor.model.EmisorConfiguracion;
    import org.sifenboot.app.admin.referencia_geografica.service.DepartamentoService;
    import org.sifenboot.app.admin.emisor.service.EmisorService;
    import org.sifenboot.app.admin.referencia_geografica.service.DistritoService;
    import org.sifenboot.app.admin.referencia_geografica.service.LocalidadService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/app/emisores/view")
    public class EmisorViewController {

        private final EmisorService emisorService;
        private final DepartamentoService departamentoService;
        private final DistritoService distritoService;
        private final LocalidadService localidadService;


        public EmisorViewController(EmisorService emisorService,
                                    DepartamentoService departamentoService,
                                    DistritoService distritoService,
                                    LocalidadService localidadService
                                    ) {
            this.emisorService = emisorService;
            this.departamentoService = departamentoService;
            this.distritoService = distritoService;
            this.localidadService = localidadService;
        }



        @GetMapping("/list")
        public String listPage(Model model) {
            // Obtenemos la lista de emisores para la tabla
            model.addAttribute("emisores", emisorService.findAll());
            // Retorna el fragmento de la lista
            return "ui/emisor/list";
        }


        @GetMapping("/create")
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
        public String editPage(@PathVariable Integer id, Model model) {

            Emisor emisor = emisorService.findById(id);

            prepareFormModel(model, emisor);

            model.addAttribute(
                    "titulo",
                    "Editar Emisor: " + emisor.getRazonSocial()
            );

            return "ui/emisor/form";
        }


        @GetMapping("/details/{id}")
        public String detailsPage(@PathVariable Integer id, Model model) {

            Emisor emisor = emisorService.findById(id);

            prepareFormModel(model, emisor);

            model.addAttribute("titulo", "Consulta de Emisor");
            model.addAttribute("readOnly", true);

            return "ui/emisor/form";
        }


        private void prepareFormModel(Model model, Emisor emisor) {

            model.addAttribute("emisor", emisor);

            model.addAttribute(
                    "departamentos",
                    departamentoService.findAll()
            );

            // DISTRITOS
            if (emisor.getDepartamento() != null) {

                model.addAttribute(
                        "distritos",
                        distritoService.findByDepartamentoId(
                                emisor.getDepartamento().getId()
                        )
                );
            }

            // LOCALIDADES
            if (emisor.getDistrito() != null) {

                model.addAttribute(
                        "localidades",
                        localidadService.findByDistritoId(
                                emisor.getDistrito().getId()
                        )
                );
            }
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




        // Acción de ELIMINAR - Ajustada para estándares HTMX
        @DeleteMapping("/delete/{id}")
        public String deleteAction(@PathVariable Integer id, Model model) {
            emisorService.deleteById(id);

            // Retornamos la lista para que HTMX actualice el #main-content
            model.addAttribute("emisores", emisorService.findAll());
            return "ui/emisor/list";
        }




    }