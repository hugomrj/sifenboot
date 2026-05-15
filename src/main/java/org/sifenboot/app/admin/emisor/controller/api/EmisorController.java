package org.sifenboot.app.admin.emisor.controller.api;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/emisores")
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