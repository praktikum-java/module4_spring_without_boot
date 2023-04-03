package ru.practicum.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LaterApplicationException extends RuntimeException {
    public LaterApplicationException(String message) {
        super(message);
    }

    public LaterApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}