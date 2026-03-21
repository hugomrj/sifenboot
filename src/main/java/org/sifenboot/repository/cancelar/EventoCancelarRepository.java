package org.sifenboot.repository.cancelar;


import org.sifenboot.integration.soap.client.EventoCancelarClient;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;
@Repository
public class EventoCancelarRepository {

    private final EventoCancelarClient cancelarClient;

    public EventoCancelarRepository(EventoCancelarClient cancelarClient) {
        this.cancelarClient = cancelarClient;
    }

    public String cancelarEvento(String cdc, String motivo) {

        try {
            HttpResponse<String> httpResponse =
                    cancelarClient.cancelarEvento(cdc, motivo);


            if (httpResponse.statusCode() >= 500) {
                throw new RuntimeException("Error HTTP " + httpResponse.statusCode());
            }

            return httpResponse.body(); // XML crudo

        } catch (Exception e) {
            throw new RuntimeException("Error llamando al servicio SOAP", e);
        }
    }
}
