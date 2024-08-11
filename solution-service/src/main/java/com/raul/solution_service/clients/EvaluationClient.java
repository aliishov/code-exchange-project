package com.raul.solution_service.clients;

import com.raul.solution_service.dto.evaluationDto.EvaluationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "evaluation-service", url = "${evaluation.service.url}")
public interface EvaluationClient {

    @GetMapping("/{solutionId}")
    ResponseEntity<EvaluationResponseDto> getEvaluationBySolutionId(@PathVariable Integer solutionId);
}
