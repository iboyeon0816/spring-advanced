package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends GeneralException {
    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
