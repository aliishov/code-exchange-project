package com.raul.problem_service.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemNotFoundException extends RuntimeException {
    private final String msg;
}
