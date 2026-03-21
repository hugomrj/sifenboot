package org.sifenboot.service.evento;

import org.sifenboot.dto.evento.request.CancelarRequest;
import org.sifenboot.dto.evento.response.CancelarResponse;
import org.sifenboot.repository.cancelar.EventoCancelarRepository;
import org.sifenboot.service.evento.parser.EventoCancelarSoapParser;
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

    public CancelarResponse execute(CancelarRequest request) {

        String xml = cancelarRepository.cancelarEvento(
                request.getCdc(),
                request.getMotivo()
        );

        return parser.parse(xml);
    }
}
