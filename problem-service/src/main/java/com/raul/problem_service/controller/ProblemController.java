package com.raul.problem_service.controller;

import com.raul.problem_service.dto.ProblemRequestDto;
import com.raul.problem_service.dto.ProblemResponseDto;
import com.raul.problem_service.services.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/create")
    public ResponseEntity<Map<Integer, ProblemResponseDto>> create(@RequestBody @Valid ProblemRequestDto request) {
        return problemService.create(request);
    }

    @GetMapping
    public ResponseEntity<Map<Integer, ProblemResponseDto>> getAll() {
        return problemService.getAll();
    }

    @PutMapping("/update/{problemId}")
    public ResponseEntity<ProblemResponseDto> update(@PathVariable Integer problemId,
                                                     @RequestBody @Valid ProblemRequestDto request) {
        return problemService.update(problemId, request);
    }

    @DeleteMapping("/delete/{problemId}")
    public ResponseEntity<Map<Integer, ProblemResponseDto>> delete(@PathVariable Integer problemId) {
        return problemService.delete(problemId);
    }


    @GetMapping("/next")
    public ResponseEntity<ProblemResponseDto> nextProblem() {
        return problemService.nextProblem();
    }
}
