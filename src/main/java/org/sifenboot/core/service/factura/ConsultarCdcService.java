package org.sifenboot.core.service.factura;


import org.sifenboot.core.dto.factura.response.ConsultaCdcResponse;
import org.sifenboot.core.repository.factura.ConsultaCdcRepository;
import org.sifenboot.core.service.factura.parser.ConsultaCdcSoapParser;
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