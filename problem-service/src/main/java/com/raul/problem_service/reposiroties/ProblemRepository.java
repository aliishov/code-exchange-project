package com.raul.problem_service.reposiroties;

import com.raul.problem_service.models.Problem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    @EntityGraph(attributePaths = {"categories"})
    List<Problem> findAll();
}
