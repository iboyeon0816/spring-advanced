package org.example.expert.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class GeneralException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GeneralException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
