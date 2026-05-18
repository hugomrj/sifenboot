package org.sifenboot.core.factura.service.helper;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public final class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    // Constructor privado para evitar instanciación de clase utilitaria
    private JsonUtils() {
        throw new UnsupportedOperationException("Clase utilitaria");
    }

    public static BigDecimal getBigDecimalOrZero(JsonNode item, String fieldName) {
        if (item != null && item.has(fieldName) && !item.get(fieldName).isNull()) {
            String value = item.get(fieldName).asText().trim();
            if (!value.isEmpty()) {
                try {
                    return new BigDecimal(value);
                } catch (NumberFormatException e) {
                    log.warn("Formato numérico inválido en campo '{}' con valor '{}', se asume 0.", fieldName, value);
                }
            }
        }
        return BigDecimal.ZERO;
    }
}