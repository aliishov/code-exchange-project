package com.raul.solution_service.services;

import com.raul.solution_service.clients.ProblemClient;
import com.raul.solution_service.dto.solutionDto.SolutionRequestDto;
import com.raul.solution_service.dto.solutionDto.SolutionResponseDto;
import com.raul.solution_service.models.Solution;
import com.raul.solution_service.models.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class SolutionConverter {

    private final ProblemClient problemClient;

    public SolutionResponseDto convertToSolutionResponse(Solution solution) {
        var problem = problemClient.getProblemById(solution.getProblemId()).getBody();

        return new SolutionResponseDto(
                solution.getId(),
                solution.getSolutionCode(),
                solution.getProblemId(),
                problem.title(),
                solution.getStatus(),
                solution.getUpdatedAt()
        );
    }

    public Solution convertToSolution(SolutionRequestDto request) {
        return Solution.builder()
                .solutionCode(request.solutionCode())
                .problemId(request.problemId())
                .status(Status.PENDING)
                .submittedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
