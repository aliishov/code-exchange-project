package com.raul.problem_service.dto;

import com.raul.problem_service.models.Difficulty;

import java.time.LocalDateTime;
import java.util.List;

public record ProblemResponseDto (

        String title,
        String description,
        Difficulty difficulty,
        List<String> categoryName,
        LocalDateTime createdAt
) { }
