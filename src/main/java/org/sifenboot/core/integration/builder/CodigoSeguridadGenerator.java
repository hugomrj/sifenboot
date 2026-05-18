package org.sifenboot.core.integration.builder;

import java.util.concurrent.ThreadLocalRandom;

public final class CodigoSeguridadGenerator {

    private CodigoSeguridadGenerator() {
        // Evita instanciación
    }

    public static String generar() {

        int numero = ThreadLocalRandom.current()
                .nextInt(1, 1_000_000_000);

        return String.format("%09d", numero);
    }
}