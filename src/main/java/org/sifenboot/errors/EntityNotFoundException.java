package org.sifenboot.errors;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String entity, String key, Object value) {
    super(entity + " no encontrado: " + key + "=" + value);
  }
}

