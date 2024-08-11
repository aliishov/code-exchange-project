package com.raul.evaluation_service.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationNotFoundException extends RuntimeException {
    private final String msg;
}
