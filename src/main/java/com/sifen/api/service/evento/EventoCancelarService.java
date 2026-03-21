package com.sifen.api.service.evento;

import com.sifen.api.dto.evento.request.CancelarRequest;
import com.sifen.api.dto.evento.response.CancelarResponse;
import com.sifen.api.repository.cancelar.EventoCancelarRepository;
import com.sifen.api.service.evento.parser.EventoCancelarSoapParser;
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
