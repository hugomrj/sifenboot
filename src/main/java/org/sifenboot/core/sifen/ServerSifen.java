package org.sifenboot.core.sifen;

import org.springframework.stereotype.Service;

@Service
public class ServerSifen {

    private static final String PRODUCCION = "https://sifen.set.gov.py";
    private static final String TEST = "https://sifen-test.set.gov.py";

    public String getServer(String ambiente) {

        if ("prod".equalsIgnoreCase(ambiente)) {
            return PRODUCCION;
        }
        if ("test".equalsIgnoreCase(ambiente)) {
            return TEST;
        }

        throw new IllegalArgumentException("Ambiente no válido: " + ambiente);
    }

    public String getProduccionServer() {
        return PRODUCCION;
    }

    public String getTestServer() {
        return TEST;
    }
}
