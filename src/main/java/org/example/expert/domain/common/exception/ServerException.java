package org.example.expert.domain.common.exception;

import org.springframework.http.HttpStatus;

public class ServerException extends GeneralException {

    public ServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
