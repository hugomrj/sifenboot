package org.sifenboot.errors;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String message
) {}

