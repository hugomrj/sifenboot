package org.sifenboot.errors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.dao.CannotAcquireLockException;
//import org.springframework.dao.CannotGetJdbcConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.reactive.function.client.WebClientRequestException;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClientResponseException;
//import org.springframework.ws.client.WebServiceIOException;
//import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========= VALIDACIONES / REQUEST =========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "rejectedValue", fe.getRejectedValue(),
                        "message", fe.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Validation failed", req, Map.of("fieldErrors", fieldErrors));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, HttpServletRequest req) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR, // o BAD_GATEWAY / BAD_REQUEST según tu caso
                ex.getMessage(),                  // <-- acá va tu mensaje
                req,
                Map.of("exception", ex.getClass().getSimpleName())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        var violations = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                        "property", String.valueOf(v.getPropertyPath()),
                        "invalidValue", v.getInvalidValue(),
                        "message", v.getMessage()
                ))
                .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Constraint violation", req, Map.of("violations", violations));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, safeMessage(ex), req, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoHandlerFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Endpoint not found", req, Map.of("method", ex.getHttpMethod(), "url", ex.getRequestURL()));
    }

    // ========= SEGURIDAD =========

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access denied", req, null);
    }

    // ========= HTTP CLIENT (RestTemplate / RestClient / WebClient) =========

    /**
     * Respuesta HTTP del servicio externo (404, 400, 500, etc.).
     * - Si es 4xx: normalmente propagamos el status (es un error "del request" hacia el externo).
     * - Si es 5xx: en una API gateway-like suele mapearse a 502 Bad Gateway (downstream falló).
     */
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> handleRestClient(RestClientResponseException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.resolve(ex.getRawStatusCode());
        if (status == null) status = HttpStatus.BAD_GATEWAY;

        HttpStatus mapped = (status.is4xxClientError()) ? status : HttpStatus.BAD_GATEWAY;

        Map<String, Object> details = Map.of(
                "downstreamStatus", ex.getRawStatusCode(),
                "downstreamBody", trimBody(ex.getResponseBodyAsString())
        );

        return build(mapped, "Downstream HTTP error", req, details);
    }

   /* @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiError> handleWebClientResponse(WebClientResponseException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.resolve(ex.getRawStatusCode());
        if (status == null) status = HttpStatus.BAD_GATEWAY;

        HttpStatus mapped = (status.is4xxClientError()) ? status : HttpStatus.BAD_GATEWAY;

        return build(mapped, "Downstream HTTP error", req, Map.of(
                "downstreamStatus", ex.getRawStatusCode(),
                "downstreamBody", trimBody(ex.getResponseBodyAsString())
        ));
    }

    /**
     * Problemas de red / DNS / connection refused / timeout (cuando no hay respuesta HTTP).
     */
   /* @ExceptionHandler({ResourceAccessException.class, WebClientRequestException.class, SocketTimeoutException.class})
    public ResponseEntity<ApiError> handleNetworkTimeout(Exception ex, HttpServletRequest req) {
        // Si querés diferenciar: timeout=504, conexión=503. Aquí lo dejamos como 504 por defecto.
        return build(HttpStatus.GATEWAY_TIMEOUT, "Network/timeout calling downstream service", req,
                Map.of("exception", ex.getClass().getSimpleName()));
    }

    // ========= SOAP CLIENT =========

    @ExceptionHandler(SoapFaultClientException.class)
    public ResponseEntity<ApiError> handleSoapFault(SoapFaultClientException ex, HttpServletRequest req) {
        // SOAP Fault recibido: el upstream respondió, pero con fault.
        return build(HttpStatus.BAD_GATEWAY, "SOAP Fault from downstream", req, Map.of(
                "faultCode", String.valueOf(ex.getFaultCode()),
                "faultString", ex.getFaultStringOrReason()
        ));
    }*/

    /*@ExceptionHandler(WebServiceIOException.class)
    public ResponseEntity<ApiError> handleSoapIO(WebServiceIOException ex, HttpServletRequest req) {
        // I/O (timeout / conexión) en SOAP
        return build(HttpStatus.GATEWAY_TIMEOUT, "SOAP I/O error calling downstream", req, Map.of(
                "message", safeMessage(ex)
        ));
    }*/

    // ========= DB / POSTGRES =========

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        // Unique violation / FK violation, etc.
        return build(HttpStatus.CONFLICT, "Data integrity violation", req, Map.of(
                "cause", rootCauseMessage(ex)
        ));
    }


    /*@ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ApiError> handleDbConnection(CannotGetJdbcConnectionException ex, HttpServletRequest req) {
        // DB no disponible / pool agotado / red
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Database unavailable", req, Map.of(
                "cause", rootCauseMessage(ex)
        ));
    }*/

    @ExceptionHandler({CannotAcquireLockException.class})
    public ResponseEntity<ApiError> handleDbLock(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Database lock/conflict", req, Map.of(
                "cause", rootCauseMessage(ex)
        ));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(DataAccessException ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", req, Map.of(
                "cause", rootCauseMessage(ex)
        ));
    }

    // ========= EXCEPCIONES “CON STATUS” (opcional, útil para custom exceptions) =========

    @ExceptionHandler({ResponseStatusException.class, ErrorResponseException.class})
    public ResponseEntity<ApiError> handleStatusExceptions(Exception ex, HttpServletRequest req) {
        HttpStatus status;
        String message;

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = (rse.getReason() != null) ? rse.getReason() : "Request failed";
        } else {
            ErrorResponseException ere = (ErrorResponseException) ex;
            status = HttpStatus.valueOf(ere.getStatusCode().value());
            message = safeMessage(ere);
        }
        return build(status, message, req, null);
    }

    // ========= FALLBACK =========

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        // Log real aquí (con stacktrace) en tu logger; al cliente devolvemos algo seguro.
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, Map.of(
                "exception", ex.getClass().getSimpleName()
        ));
    }

    // ========= helpers =========

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req, Map<String, Object> details) {
        String traceId = firstNonBlank(
                MDC.get("traceId"),
                MDC.get("X-B3-TraceId"),
                MDC.get("correlationId")
        );

        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                traceId,
                details
        );

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private String safeMessage(Exception ex) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? ex.getClass().getSimpleName() : msg;
    }

    private String rootCauseMessage(Throwable t) {
        Throwable root = t;
        while (root.getCause() != null && root.getCause() != root) root = root.getCause();
        String msg = root.getMessage();
        return (msg == null) ? root.getClass().getSimpleName() : msg;
    }

    private String trimBody(String body) {
        if (body == null) return null;
        int max = 2000; // evita devolver megas al cliente
        return body.length() <= max ? body : body.substring(0, max) + "...(truncated)";
    }

    private String firstNonBlank(String... vals) {
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }
}
