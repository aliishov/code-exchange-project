package com.raul.solution_service.dto.solutionDto;

import com.raul.solution_service.models.Status;

public record SolutionRequestDto(

        String solutionCode,
        Integer problemId,
        Status status
) { }
