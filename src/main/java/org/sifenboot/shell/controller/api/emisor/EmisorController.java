package org.sifenboot.shell.controller.api.emisor;

import org.sifenboot.shell.model.Emisor;
import org.sifenboot.shell.service.EmisorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emisores")
public class EmisorController {

    private final EmisorService emisorService;

    public EmisorController(EmisorService emisorService) {
        this.emisorService = emisorService;
    }

    @GetMapping
    public List<Emisor> getAll() {
        return emisorService.findAll(); // Devuelve JSON puro
    }
}