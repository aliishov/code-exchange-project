package com.raul.solution_service.dto.solutionDto;

import com.raul.solution_service.models.Status;

import java.time.LocalDateTime;

public record SolutionResponseDto(

        Integer id,
        String solutionCode,
        Integer problemId,
        String problemTitle,
        Status status,
        LocalDateTime submittedAt
) { }
