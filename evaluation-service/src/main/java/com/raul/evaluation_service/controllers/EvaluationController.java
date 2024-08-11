package com.raul.evaluation_service.controllers;

import com.raul.evaluation_service.dto.EvaluationResponseDto;
import com.raul.evaluation_service.services.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/{solutionId}")
    public ResponseEntity<EvaluationResponseDto> getEvaluationBySolutionId(@PathVariable Integer solutionId) {
        return evaluationService.getEvaluationBySolutionId(solutionId);
    }
}
