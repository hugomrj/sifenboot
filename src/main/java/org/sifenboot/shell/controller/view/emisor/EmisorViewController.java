package org.sifenboot.shell.controller.view.emisor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/emisores/view")
public class EmisorViewController {

    @GetMapping("/list")
    public String listPage() {
        // Retorna el path relativo dentro de tu carpeta de recursos/estáticos
        // Spring servirá: web/ui/emisor/list.html
        return "ui/emisor/list";
    }

    @GetMapping("/nuevo")
    public String formPage() {
        return "ui/emisor/form";
    }
}