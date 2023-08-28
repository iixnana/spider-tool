package com.spider.routes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidFormatException extends Exception {
    public InvalidFormatException(String message) {
        super(message);
    }
}
