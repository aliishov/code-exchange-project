package com.raul.solution_service.dto.problemDto;

import java.time.LocalDateTime;
import java.util.List;

public record ProblemResponseDto(

        String title,
        String description,
        Difficulty difficulty,
        List<String> categoryName,
        LocalDateTime createdAt
) { }
