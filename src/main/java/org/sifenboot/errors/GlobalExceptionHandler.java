package org.sifenboot.errors;

import jakarta.persistence.EntityNotFoundException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String code,
            String message
    ) {

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                code,
                message
        );

        return new ResponseEntity<>(response, status);
    }

    // =========================
    // VALIDACIONES / REQUEST
    // =========================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex
    ) {

        // Cambio aquí: solo logueamos el mensaje, no toda la excepción
        logger.warn("JSON inválido recibido: {}", ex.getMessage());

        String message = "El JSON enviado está mal formado";

        Throwable cause = ex.getCause();

        if (cause != null) {
            message += ": " + cause.getMessage();
        }

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "ERR_INVALID_JSON",
                message
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "ERR_VALIDATION",
                details
        );
    }

    // =========================
    // NEGOCIO
    // =========================

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(
            BusinessRuleException ex
    ) {

        return buildResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "ERR_BUSINESS_RULE",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            InvalidConfigurationException ex
    ) {

        return buildResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "ERR_CONFIG_INVALID",
                ex.getMessage()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            UnauthorizedException ex
    ) {

        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "ERR_UNAUTHORIZED",
                ex.getMessage()
        );
    }

    // =========================
    // RECURSOS
    // =========================

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            EntityNotFoundException ex
    ) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                "ERR_NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(
            NoResourceFoundException ex
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // =========================
    // BASE DE DATOS
    // =========================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex
    ) {

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof PSQLException psqlEx) {

            String constraint =
                    psqlEx.getServerErrorMessage() != null
                            ? psqlEx.getServerErrorMessage().getConstraint()
                            : null;

            if ("documentos_cdc_key".equals(constraint)) {

                return buildResponse(
                        HttpStatus.CONFLICT,
                        "ERR_DUPLICATE_CDC",
                        "El documento ya existe (CDC duplicado)"
                );
            }
        }

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "ERR_DATA_INTEGRITY",
                "Error de integridad de datos"
        );
    }

    // =========================
    // FALLBACK
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(
            Exception ex
    ) {

        logger.error("Error inesperado en el sistema", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERR_INTERNAL",
                "Ocurrió un error inesperado"
        );
    }
}