package com.raul.evaluation_service.repositories;

import com.raul.evaluation_service.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    Optional<Evaluation> findBySolutionId(Integer solutionId);
}
