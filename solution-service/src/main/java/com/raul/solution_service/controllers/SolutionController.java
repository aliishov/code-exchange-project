package com.raul.solution_service.controllers;

import com.raul.solution_service.dto.solutionDto.SolutionRequestDto;
import com.raul.solution_service.dto.solutionDto.SolutionResponseDto;
import com.raul.solution_service.services.SolutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/solution")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @PostMapping("/create")
    public ResponseEntity<SolutionResponseDto> create(@RequestBody @Valid SolutionRequestDto request) {
        return solutionService.create(request);
    }

    @GetMapping("problem/{problemId}")
    public ResponseEntity<Map<Integer, SolutionResponseDto>> getSolutionByProblemId(@PathVariable Integer problemId) {
        return solutionService.getSolutionByProblemId(problemId);
    }
}
