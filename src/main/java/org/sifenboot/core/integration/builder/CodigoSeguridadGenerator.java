package org.sifenboot.core.integration.builder;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class CodigoSeguridadGenerator {

    public String generar() {

        int numero = ThreadLocalRandom.current()
                .nextInt(1, 1_000_000_000);

        return String.format("%09d", numero);
    }

}