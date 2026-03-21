
package com.sifen.api.errors;
public class DataMissingException extends RuntimeException {
  public DataMissingException(String entity, String msg) {
      super("Falta de datos en " + entity + ": " + msg);
  }
}
