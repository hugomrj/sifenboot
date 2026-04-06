    package org.sifenboot.shell.controller.view.emisor;

    import org.sifenboot.shell.model.Emisor;
    import org.sifenboot.shell.service.EmisorService; // Importamos tu servicio
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model; // Importante para pasar datos a Thymeleaf
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    import java.util.List;

    @Controller
    @RequestMapping("/app/emisores/view")
    public class EmisorViewController {

        // 1. Inyectamos el servicio de Emisores
        private final EmisorService emisorService;

        public EmisorViewController(EmisorService emisorService) {
            this.emisorService = emisorService;
        }

        @GetMapping("/list")
        public String listPage(Model model) {
            // 2. Buscamos los datos en la DB y los pasamos al "modelo"
            model.addAttribute("emisores", emisorService.findAll());

            List<Emisor> lista = emisorService.findAll();
            System.out.println("LOG: Cantidad de emisores encontrados: " + lista.size());

            // 3. Retornamos la vista (Spring buscará templates/ui/emisor/list.html)
            return "ui/emisor/list";
        }

        @GetMapping("/new")
        public String formPage() {
            return "ui/emisor/form";
        }
    }


