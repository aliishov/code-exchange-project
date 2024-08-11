package com.raul.solution_service.dto.evaluationDto;

import java.time.LocalDateTime;

public record EvaluationResponseDto(
        Integer id,
        Integer solutionId,
        Boolean isSuccess,
        LocalDateTime evaluatedAt
) { }
