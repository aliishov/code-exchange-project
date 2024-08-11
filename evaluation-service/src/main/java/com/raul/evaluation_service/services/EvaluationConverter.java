package com.raul.evaluation_service.services;

import com.raul.evaluation_service.dto.EvaluationResponseDto;
import com.raul.evaluation_service.models.Evaluation;
import org.springframework.stereotype.Service;

@Service
public class EvaluationConverter {
    public EvaluationResponseDto convertToEvaluationResponse(Evaluation evaluation) {
        return new EvaluationResponseDto(
                evaluation.getId(),
                evaluation.getSolutionId(),
                evaluation.getIsSuccess(),
                evaluation.getEvaluatedAt()
        );
    }
}
