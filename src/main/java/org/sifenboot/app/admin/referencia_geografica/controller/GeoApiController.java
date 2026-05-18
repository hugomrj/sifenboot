package org.sifenboot.app.admin.referencia_geografica.controller;


import org.sifenboot.app.admin.referencia_geografica.model.Departamento;
import org.sifenboot.app.admin.referencia_geografica.service.DepartamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // IMPORTANTE: Esto devuelve JSON automáticamente
@RequestMapping("/api/geo")
public class GeoApiController {

    private final DepartamentoService departamentoService;
    // Inyecta también DistritoService y LocalidadService cuando los necesitemos

    public GeoApiController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    // Endpoint para obtener departamentos
    @GetMapping("/departamentos")
    public List<Departamento> getDepartamentos() {
        // Asegúrate que tu modelo Departamento tenga getters para id y descripcion
        return departamentoService.findAll();
    }

    // Más adelante agregaremos aquí los endpoints de Distritos y Localidades
}