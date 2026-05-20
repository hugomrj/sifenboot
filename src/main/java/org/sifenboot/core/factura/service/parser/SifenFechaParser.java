package org.sifenboot.core.factura.service.parser;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SifenFechaParser {

    private static final DateTimeFormatter SIFEN_DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final DateTimeFormatter[] ALTERNATIVE_FORMATTERS = {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    /**
     * Parsea y normaliza la fecha del cliente al formato estricto de SIFEN.
     * Si falla o es nula, retorna la fecha actual del sistema.
     */
    public static String normalizar(String fechaInput) {
        if (fechaInput == null || fechaInput.isEmpty()) {
            return fechaActual();
        }

        // Intento 1: Ya viene en formato exacto SIFEN
        try {
            LocalDateTime.parse(fechaInput, SIFEN_DATE_FORMATTER);
            return fechaInput;
        } catch (DateTimeParseException e) {
            // Intento 2: Probar formatos alternativos habituales del cliente
            for (DateTimeFormatter formatter : ALTERNATIVE_FORMATTERS) {
                try {
                    if (formatter == DateTimeFormatter.ofPattern("yyyy-MM-dd")) {
                        return LocalDateTime.parse(fechaInput + "T00:00:00", SIFEN_DATE_FORMATTER).format(SIFEN_DATE_FORMATTER);
                    } else {
                        return LocalDateTime.parse(fechaInput, formatter).format(SIFEN_DATE_FORMATTER);
                    }
                } catch (DateTimeParseException ex) {
                    // Continúa al siguiente formato
                }
            }
        }

        return fechaActual();
    }

    private static String fechaActual() {
        return LocalDateTime.now().format(SIFEN_DATE_FORMATTER);
    }
}