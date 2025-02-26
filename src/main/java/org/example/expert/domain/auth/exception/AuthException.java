package org.example.expert.domain.auth.exception;

import org.example.expert.domain.common.exception.GeneralException;
import org.springframework.http.HttpStatus;

public class AuthException extends GeneralException {

    public AuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
