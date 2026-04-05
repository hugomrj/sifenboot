package org.sifenboot.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, Map.of(
                "exception", ex.getClass().getSimpleName()
        ));
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req, Map<String, Object> details) {

        ApiError body = new ApiError(
                Instant.now(),           // timestamp: Cuándo pasó
                status.value(),          // status: 401, 500, etc.
                status.getReasonPhrase(),// error: "Unauthorized", "Internal Server Error"
                message,                 // message: Tu mensaje personalizado
                details                  // details: Mapa de errores extra
        );

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }


    // 1. Añadimos este manejador específico
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(org.springframework.web.server.ResponseStatusException ex, HttpServletRequest req) {
        // Obtenemos el código (401, 404, etc.) que definiste en el Service
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Usamos el método 'build' que ya existe en tu código heredado
        return build(status, ex.getReason(), req, Map.of(
                "exception", ex.getClass().getSimpleName()
        ));
    }



}