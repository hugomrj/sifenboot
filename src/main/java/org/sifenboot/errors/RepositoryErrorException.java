package org.sifenboot.errors;

public class RepositoryErrorException extends RuntimeException {
    public RepositoryErrorException(String message) {
        super(message);
    }
    public RepositoryErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
