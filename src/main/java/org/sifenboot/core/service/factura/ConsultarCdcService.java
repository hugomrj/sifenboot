package org.sifenboot.core.service.factura;


import org.sifenboot.core.dto.factura.response.ConsultaCdcResponse;
import org.sifenboot.core.repository.factura.ConsultaCdcRepository;
import org.sifenboot.core.service.factura.parser.ConsultaCdcSoapParser;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.stereotype.Service;


@Service
public class ConsultarCdcService {

    private final CertificadoService certificadoService;
    private final ConsultaCdcRepository repository;
    private final ConsultaCdcSoapParser parser = new ConsultaCdcSoapParser();

    public ConsultarCdcService(ConsultaCdcRepository repository, CertificadoService certificadoService) {
        this.repository = repository;
        this.certificadoService = certificadoService;
    }


    public ConsultaCdcResponse consultar(String emisorCod, String cdc) {

        if (cdc == null || cdc.isBlank()) {
            throw new IllegalArgumentException("El CDC no puede estar vacío");
        }

        // Obtener el certificado
        Certificado certificado = certificadoService.obtenerPorCodigoEmisor(emisorCod);

        String xml = repository.buscarPorCdc(cdc, certificado);

        if (xml == null || xml.isBlank()) {
            throw new IllegalStateException("No se recibió respuesta del SOAP");
        }

        return parser.parse(xml);
    }
}