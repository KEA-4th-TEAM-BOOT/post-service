package com.example.postservice.exception;

public enum PostErrorCode {

    FAIL_FIND_POST_COMMENT("게시물에 존재하지 않는 코멘트입니다."),
    FAIL_FIND_POST("존재하지 않는 게시물입니다.");

    public final String message;

    PostErrorCode(String message) {
        this.message = message;
    }
}
