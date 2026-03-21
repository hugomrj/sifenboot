package org.sifenboot.errors;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        Map<String, Object> details
) {}