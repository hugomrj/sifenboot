package org.sifenboot.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de negocio (Ejemplo: Configuración faltante)
    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(InvalidConfigurationException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "ERR_CONFIG_INVALID", ex.getMessage());
    }

    // Manejo de recursos no encontrados
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "ERR_NOT_FOUND", ex.getMessage());
    }

    // Fallback: Errores inesperados (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        // Loguear el error real para el desarrollador, pero no enviarlo al cliente
        logger.error("Error inesperado en el sistema: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERR_INTERNAL", "Ocurrió un error inesperado.");
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String code, String message) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                code,
                message
        );
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Esto extrae los mensajes de error de los campos que fallaron
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, "ERR_VALIDATION", details);
    }


    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        // 422 es ideal para errores de lógica de negocio que impiden procesar la petición
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "ERR_BUSINESS_RULE", ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        // Solo devolvemos un 404 sin loguear nada como ERROR
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        // Usamos tu método buildResponse para mantener la coherencia
        return buildResponse(HttpStatus.UNAUTHORIZED, "ERR_UNAUTHORIZED", ex.getMessage());
    }

}