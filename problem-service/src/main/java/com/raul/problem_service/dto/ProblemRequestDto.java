package com.raul.problem_service.dto;

import com.raul.problem_service.models.Category;
import com.raul.problem_service.models.Difficulty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProblemRequestDto(

        @NotNull(message = "Title should to be empty")
        @NotEmpty(message = "Title should not be empty")
        @Size(max = 100, message = "Title should not exceed 100 characters")
        String title,

        @NotNull(message = "description should to be empty")
        @NotEmpty(message = "Description should not be empty")
        @Size(max = 1000, message = "Description should not exceed 1000 characters")
        String description,

        @NotNull(message = "Difficulty should to be empty")
        Difficulty difficulty,

        @NotNull(message = "Category should to be empty")
        List<Category> categories
) { }
