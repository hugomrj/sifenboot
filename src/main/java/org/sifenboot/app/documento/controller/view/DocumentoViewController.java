package org.sifenboot.app.documento.controller.view;

import org.sifenboot.app.documento.dto.DocumentoListDTO;
import org.sifenboot.app.documento.model.Documento;
import org.sifenboot.app.documento.service.DocumentoService;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.app.emisor.service.EmisorService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping("/emisor/{cod}/documentos")
public class DocumentoViewController {

    private final EmisorService emisorService;
    private final DocumentoService documentoService;

    public DocumentoViewController(DocumentoService documentoService,
                                   EmisorService emisorService) {
        this.documentoService = documentoService;
        this.emisorService = emisorService;
    }

    @GetMapping
    public String index(@PathVariable String cod, Model model) {

        List<Documento> facturas;

        if ("0".equals(cod)) {
            // vacío
            facturas = List.of();
        } else {
            documentoService.setSchemaContext(cod);
            facturas = documentoService.findAll();
        }


        DocumentoListDTO data = new DocumentoListDTO(cod, facturas);
        model.addAttribute("data", data);


        List<Emisor> emisores = emisorService.findAll();
        model.addAttribute("emisores", emisores);

        return "ui/documentos/lista";
    }


}