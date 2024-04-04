package com.example.postservice.dto.request;

public record CommentCreateRequestDto(Long postId, Long userId, String content) {
}
