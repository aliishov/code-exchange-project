package com.raul.problem_service.services;

import com.raul.problem_service.dto.ProblemRequestDto;
import com.raul.problem_service.dto.ProblemResponseDto;
import com.raul.problem_service.models.Problem;
import com.raul.problem_service.reposiroties.ProblemRepository;
import com.raul.problem_service.utils.exceptions.ProblemNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemService {

    private final ProblemRepository repository;
    private final ProblemConverter converter;
    private final Queue<Problem> problemQueue = new PriorityQueue<>();
    private final Map<Integer, ProblemResponseDto> problemHash = new HashMap<>();

    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing problem queue with existing problems from database.");
        var problems = repository.findAll();

        problemQueue.addAll(problems);
        problems.forEach(problem -> problemHash.put(problem.getId(), converter.convertToProblemResponse(problem)));

        log.info("Initialization complete. {} problems added to the queue.", problems.size());
    }

    public ResponseEntity<Map<Integer, ProblemResponseDto>> create(ProblemRequestDto request) {

        log.info("Creating a new problem with title: {}", request.title());

        var problem = converter.convertToProblem(request);
        repository.save(problem);
        addTaskToQueue(problem);
        problemHash.put(problem.getId(), converter.convertToProblemResponse(problem));

        log.info("Problem created with ID: {}", problem.getId());

        return new ResponseEntity<>(problemHash, HttpStatus.CREATED);
    }

    public ResponseEntity<Map<Integer, ProblemResponseDto>> getAll() {

        log.info("Fetching all problems");

        if (problemHash.isEmpty()) {
            log.warn("No problems found");
            throw new ProblemNotFoundException("Sorry! I have no problem for you");
        }

        log.info("Found {} problems", problemHash.size());

        return new ResponseEntity<>(problemHash, HttpStatus.OK);
    }

    public ResponseEntity<ProblemResponseDto> update(Integer problemId, ProblemRequestDto request) {

        log.info("Updating problem with ID: {}", problemId);

        var problem = repository.findById(problemId)
                    .orElseThrow(() -> new ProblemNotFoundException("Problem with ID: " + problemId + " not found"));

        updateProblemFromRequest(problem, request);
        repository.save(problem);
        addTaskToQueue(problem);
        problemHash.put(problem.getId(), converter.convertToProblemResponse(problem));

        log.info("Problem with ID: {} updated", problem.getId());


        return new ResponseEntity<>(converter.convertToProblemResponse(problem), HttpStatus.ACCEPTED);
    }

    private void updateProblemFromRequest(Problem problem, ProblemRequestDto request) {

        log.debug("Updating problem fields for ID: {}", problem.getId());

        problem.setTitle(request.title());
        problem.setDescription(request.description());
        problem.setDifficulty(request.difficulty());
        problem.setCategories(request.categories());
        problem.setUpdatedAt(LocalDateTime.now());

        log.debug("Problem fields updated for ID: {}", problem.getId());
    }

    public ResponseEntity<Map<Integer, ProblemResponseDto>> delete(Integer problemId) {

        log.info("Deleting problem with ID: {}", problemId);

        var problem = repository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem with ID: " + problemId + " not found"));

        repository.delete(problem);
        problemHash.remove(problemId);
        problemQueue.removeIf(p -> p.getId().equals(problemId));


        log.info("Problem with ID: {} deleted", problemId);

        return new ResponseEntity<>(problemHash, HttpStatus.ACCEPTED);
    }

    private void addTaskToQueue(Problem problem) {
        problemQueue.add(problem);
        log.info("Added task to queue: {}", problem);
    }

    @Transactional
    public ResponseEntity<ProblemResponseDto> nextProblem() {
        var nextProblem = problemQueue.poll();

        log.info("Retrieved next task: {}", nextProblem);

        if (nextProblem == null) {
            throw new ProblemNotFoundException("No problems available in the queue");
        }

        return new ResponseEntity<>(converter.convertToProblemResponse(nextProblem), HttpStatus.OK);
    }
}
