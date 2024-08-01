package com.raul.solution_service.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SolutionNotFoundException extends RuntimeException {
    private final String msg;
}
