package com.example.postservice.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException{

    private final PostErrorCode errorCode;

    private final String message;

    public PostException(PostErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "[%s] %s".formatted(errorCode, message);
    }
}
