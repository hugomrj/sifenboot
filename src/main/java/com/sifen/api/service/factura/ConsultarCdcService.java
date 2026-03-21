package com.sifen.api.service.factura;


import com.sifen.api.dto.factura.response.ConsultaCdcResponse;
import com.sifen.api.repository.factura.ConsultaCdcRepository;
import com.sifen.api.service.factura.parser.ConsultaCdcSoapParser;
import org.springframework.stereotype.Service;


@Service
public class ConsultarCdcService {

    private final ConsultaCdcRepository repository;
    private final ConsultaCdcSoapParser parser = new ConsultaCdcSoapParser();

    public ConsultarCdcService(ConsultaCdcRepository repository) {
        this.repository = repository;
    }

    public ConsultaCdcResponse consultar(String cdc) {

        if (cdc == null || cdc.isBlank()) {
            throw new IllegalArgumentException("El CDC no puede estar vacío");
        }

        String xml = repository.buscarPorCdc(cdc);

        if (xml == null || xml.isBlank()) {
            throw new IllegalStateException("No se recibió respuesta del SOAP");
        }

        return parser.parse(xml);
    }
}