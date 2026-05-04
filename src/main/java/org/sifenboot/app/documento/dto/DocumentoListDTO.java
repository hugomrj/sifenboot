package org.sifenboot.app.documento.dto;

import org.sifenboot.app.documento.model.Documento;
import java.util.List;

public class DocumentoListDTO {
    private String codEmisor;
    private List<Documento> facturas;

    public DocumentoListDTO(String codEmisor, List<Documento> facturas) {
        this.codEmisor = codEmisor;
        this.facturas = facturas;
    }

    // Getters necesarios para Thymeleaf
    public String getCodEmisor() { return codEmisor; }
    public List<Documento> getFacturas() { return facturas; }
}