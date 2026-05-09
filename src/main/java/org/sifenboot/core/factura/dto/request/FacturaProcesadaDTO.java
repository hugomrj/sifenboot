package org.sifenboot.core.factura.dto.request;


public record FacturaProcesadaDTO(
        String emisorCod,
        String cdc,
        String xmlDE,
        String estado // Ej: "FIRMADO", "REGISTRADO"
) {}
