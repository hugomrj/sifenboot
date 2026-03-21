// Opcional si querés ser aún más explícito
package com.sifen.api.errors;
public class NonUniqueResultException extends RuntimeException {
  public NonUniqueResultException(String entity, String key, Object value, int count) {
    super("Resultado no único en " + entity + " para " + key + "=" + value + " (filas=" + count + ")");
  }
}
