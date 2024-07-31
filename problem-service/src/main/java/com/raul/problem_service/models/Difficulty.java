package com.raul.problem_service.models;

public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final Integer priority;

    Difficulty(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }
}
