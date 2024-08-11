package com.raul.evaluation_service.dto;

import java.time.LocalDateTime;

public record EvaluationResponseDto(
        Integer id,
        Integer solutionId,
        Boolean isSuccess,
        LocalDateTime evaluatedAt
) { }
