package com.raul.evaluation_service.services;

import com.raul.evaluation_service.dto.EvaluationResponseDto;
import com.raul.evaluation_service.dto.SolutionEvaluationDto;
import com.raul.evaluation_service.models.Evaluation;
import com.raul.evaluation_service.repositories.EvaluationRepository;
import com.raul.evaluation_service.utils.exceptions.EvaluationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluationService {

    private final EvaluationRepository repository;
    private final EvaluationConverter converter;

    @RabbitListener(queues = {"evaluationQueue"})
    public void evaluate(String text, SolutionEvaluationDto request) {

        log.info("received from queue: {}", text);

        var evaluation = Evaluation.builder()
                .solutionId(request.solutionId())
                .isSuccess(runTest(request.solutionCode()))
                .evaluatedAt(LocalDateTime.now())
                .build();

        repository.save(evaluation);
    }

    private Boolean runTest(String solutionCode) {
        /* TODO написать логику для обработки решений задач */
        return true;
    }

    public ResponseEntity<EvaluationResponseDto> getEvaluationBySolutionId(Integer solutionId) {
        var evaluation = repository.findBySolutionId(solutionId)
                .orElseThrow(() -> new EvaluationNotFoundException("Evaluation with solution ID: " + solutionId + " not found"));

        return new ResponseEntity<>(converter.convertToEvaluationResponse(evaluation), HttpStatus.OK);
    }
}
