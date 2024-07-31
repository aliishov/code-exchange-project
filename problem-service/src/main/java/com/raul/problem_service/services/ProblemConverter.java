package com.raul.problem_service.services;

import com.raul.problem_service.dto.ProblemRequestDto;
import com.raul.problem_service.dto.ProblemResponseDto;
import com.raul.problem_service.models.Category;
import com.raul.problem_service.models.Problem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemConverter {
    public Problem convertToProblem(ProblemRequestDto request) {
        return Problem.builder()
                .title(request.title())
                .description(request.description())
                .difficulty(request.difficulty())
                .categories(request.categories())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ProblemResponseDto convertToProblemResponse(Problem problem) {
        List<String> categoryNames = problem.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());

        return new ProblemResponseDto(
                problem.getTitle(),
                problem.getDescription(),
                problem.getDifficulty(),
                categoryNames,
                problem.getCreatedAt()
        );
    }
}
