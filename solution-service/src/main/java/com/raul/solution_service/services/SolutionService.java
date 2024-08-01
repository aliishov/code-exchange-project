package com.raul.solution_service.services;

import com.raul.solution_service.dto.solutionDto.SolutionRequestDto;
import com.raul.solution_service.dto.solutionDto.SolutionResponseDto;
import com.raul.solution_service.models.Solution;
import com.raul.solution_service.repositories.SolutionRepository;
import com.raul.solution_service.utils.exceptions.SolutionNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolutionService {

    private final SolutionRepository repository;
    private final SolutionConverter converter;
    private final Map<Integer, SolutionResponseDto> solutionHash = new HashMap<>();

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
        repository.save(solution);

        var solutionResponse = converter.convertToSolutionResponse(solution);

        solutionHash.put(solution.getId(), solutionResponse);

        log.info("Solution created with ID: {}", solution.getId());

        return new ResponseEntity<>(solutionResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<SolutionResponseDto> update(Integer solutionId, SolutionRequestDto request) {
        log.info("Updating solution with ID: {}", solutionId);

        var solution = repository.findById(solutionId)
                .orElseThrow(() -> new SolutionNotFoundException("Solution with ID: " + solutionId + " not found"));

        updateSolutionFromRequest(solution, request);
        repository.save(solution);

        var solutionResponse = converter.convertToSolutionResponse(solution);
        solutionHash.put(solution.getId(), solutionResponse);

        log.info("Solution with ID: {} updated", solution.getId());


        return new ResponseEntity<>(solutionResponse, HttpStatus.ACCEPTED);
    }

    private void updateSolutionFromRequest(Solution solution, SolutionRequestDto request) {

        log.debug("Updating solution fields for ID: {}", solution.getId());

        solution.setSolutionCode(request.solutionCode());
        solution.setStatus(request.status());
        solution.setUpdatedAt(LocalDateTime.now());

        log.debug("Solution fields updated for ID: {}", solution.getId());
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
