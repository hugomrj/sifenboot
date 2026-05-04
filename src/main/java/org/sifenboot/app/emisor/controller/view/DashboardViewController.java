package org.sifenboot.app.emisor.controller.view;


import org.sifenboot.app.emisor.service.EmisorDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/emisor/resumen")
public class DashboardViewController {

    private final EmisorDashboardService dashboardService;

    public DashboardViewController(EmisorDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("stats", dashboardService.getEstadisticas());
        return "ui/emisor/resumen";
    }
}


