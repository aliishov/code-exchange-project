package com.raul.solution_service.utils.exceptionsHandler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) { }
