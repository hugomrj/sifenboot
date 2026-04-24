package org.sifenboot.core.cancelar.service;

import org.sifenboot.core.evento.dto.request.CancelarRequest;
import org.sifenboot.core.evento.dto.response.CancelarResponse;
import org.sifenboot.core.cancelar.repository.EventoCancelarRepository;
import org.sifenboot.core.cancelar.service.parser.EventoCancelarSoapParser;
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
