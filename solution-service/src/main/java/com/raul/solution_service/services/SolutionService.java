package com.raul.solution_service.services;

import com.raul.solution_service.clients.EvaluationClient;
import com.raul.solution_service.dto.solutionDto.SolutionEvaluationDto;
import com.raul.solution_service.dto.solutionDto.SolutionRequestDto;
import com.raul.solution_service.dto.solutionDto.SolutionResponseDto;
import com.raul.solution_service.models.Status;
import com.raul.solution_service.repositories.SolutionRepository;
import com.raul.solution_service.utils.exceptions.SolutionNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolutionService {

    @Value("$rabbitmq.queue.name")
    private String queueName;
    private final SolutionRepository repository;
    private final SolutionConverter converter;
    private final Map<Integer, SolutionResponseDto> solutionHash = new HashMap<>();
    private final RabbitTemplate rabbitTemplate;
    private final EvaluationClient evaluationClient;

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing solution HashTable with existing solutions from database.");

        var solutions = repository.findAll();
        solutions.forEach(solution -> solutionHash.put(solution.getId(), converter.convertToSolutionResponse(solution)));

        log.info("Initialization complete. {} solutions added to the queue.", solutions.size());
    }

    public ResponseEntity<SolutionResponseDto> create(SolutionRequestDto request) {

        log.info("Creating a new solution for  problem ith ID: {}", request.problemId());

        var solution = converter.convertToSolution(request);
//        repository.save(solution);

        log.info("Checking evaluation for solution with ID: {}", solution.getId());

        sendSolutionForEvaluate(request.problemId(), request.solutionCode());
        var evaluation = Objects.requireNonNull(evaluationClient.getEvaluationBySolutionId(solution.getId()).getBody()).isSuccess();

        solution.setStatus(evaluation ? Status.ACCEPTED : Status.REJECTED);
        repository.save(solution);

        var solutionResponse = converter.convertToSolutionResponse(solution);
        solutionHash.put(solution.getId(), solutionResponse);

        log.info("Solution created with ID: {}", solution.getId());

        return new ResponseEntity<>(solutionResponse, HttpStatus.CREATED);
    }

    private void sendSolutionForEvaluate(Integer solutionId, String solutionCode) {
        rabbitTemplate.convertAndSend(queueName, new SolutionEvaluationDto(solutionId, solutionCode));
    }

    public ResponseEntity<Map<Integer, SolutionResponseDto>> getSolutionByProblemId(Integer problemId) {

        log.info("Fetching solutions for problem ID: {}", problemId);

        var solutions = solutionHash.values().stream()
                .filter(solution -> solution.problemId().equals(problemId))
                .collect(Collectors.toMap(SolutionResponseDto::id, solutionResponse -> solutionResponse));

        if (solutions.isEmpty()) {
            log.warn("No solutions found for problem with ID: {}", problemId);
            throw new SolutionNotFoundException("No solutions found for problem with ID: " + problemId);
        }

        log.info("Found {} solutions for problem with ID: {}", solutions.size(), problemId);
        return new ResponseEntity<>(solutions, HttpStatus.OK);
    }
}
