package org.sifenboot.app.documento.controller.view;

import org.sifenboot.app.documento.dto.DocumentoListDTO;
import org.sifenboot.app.documento.model.Documento;
import org.sifenboot.app.documento.service.DocumentoService;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.app.emisor.service.EmisorService;
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

            // Si no hay datos en DB, usamos el bloque de mock
            if (facturas == null || facturas.isEmpty()) {
                facturas = getMockData();
            }


        }


        DocumentoListDTO data = new DocumentoListDTO(cod, facturas);
        model.addAttribute("data", data);


        List<Emisor> emisores = emisorService.findAll();
        model.addAttribute("emisores", emisores);

        return "ui/documentos/lista";
    }




    // -------------------------------------------------------------------------
    // TODO: ELIMINAR ESTE BLOQUE CUANDO LA DB ESTÉ LISTA
    // -------------------------------------------------------------------------
    private List<Documento> getMockData() {
        List<Documento> list = new java.util.ArrayList<>();
        var est = new org.sifenboot.app.documento.model.EstadoDocumento();
        est.setDescripcion("APROBADO");

        for (int i = 1; i <= 3; i++) {
            Documento d = new Documento();
            d.setId((long) i);
            d.setEstablecimiento("001");
            d.setPuntoExpedicion("001");
            d.setNumeroDocumento("000000" + i);
            d.setNombreReceptor("Cliente Ejemplo S.A. " + i); // Nuevo
            d.setRucReceptor("80012345-" + i);               // Nuevo
            d.setMontoTotal(new java.math.BigDecimal("100000").multiply(new java.math.BigDecimal(i)));
            d.setEstado(est);
            d.setFechaEmision(java.time.LocalDateTime.now());
            list.add(d);
        }
        return list;
    }

    // -------------------------------------------------------------------------
}