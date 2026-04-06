    package org.sifenboot.shell.controller.view;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.ui.Model;

    @Controller
    public class AuthViewController {

        @GetMapping("/login")
        public String loginPage() {
            return "login";
        }


        @GetMapping("/dashboard")
        public String dashboardPage(Model model) {
            // Aquí llamarías a tu servicio de facturación
            // model.addAttribute("aprobados", dteService.countAprobados());
            return "dashboard";
        }
    }