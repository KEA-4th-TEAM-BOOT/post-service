package com.example.postservice.dto.request;

public record ReplyCreateRequestDto(Long commentId, Long userId, String content) {
}
