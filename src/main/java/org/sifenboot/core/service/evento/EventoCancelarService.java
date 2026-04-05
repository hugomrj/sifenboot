package org.sifenboot.core.service.evento;

import org.sifenboot.core.dto.evento.request.CancelarRequest;
import org.sifenboot.core.dto.evento.response.CancelarResponse;
import org.sifenboot.core.repository.cancelar.EventoCancelarRepository;
import org.sifenboot.core.service.evento.parser.EventoCancelarSoapParser;
import org.springframework.stereotype.Service;

@Service
public class EventoCancelarService {

    private final EventoCancelarRepository cancelarRepository;
    private final EventoCancelarSoapParser parser;

    public EventoCancelarService(
            EventoCancelarRepository cancelarRepository,
            EventoCancelarSoapParser parser
    ) {
        this.cancelarRepository = cancelarRepository;
        this.parser = parser;
    }

    public CancelarResponse execute(String codEmisor,CancelarRequest request) {

        // 1. Buscas al emisor en tu base de datos
        // Emisor emisor = emisorRepo.findByCodEmisor(codEmisor);


        String xml = cancelarRepository.cancelarEvento(
                request.getCdc(),
                request.getMotivo()
        );

        return parser.parse(xml);
    }
}
