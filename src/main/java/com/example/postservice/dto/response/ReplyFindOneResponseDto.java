package com.example.postservice.dto.response;

import com.example.postservice.model.Reply;

public record ReplyFindOneResponseDto(Long id, Long userId, String content) {
    public static ReplyFindOneResponseDto from(Reply reply) {
        return new ReplyFindOneResponseDto(
                reply.getId(),
                reply.getUserId(),
                reply.getContent()
        );
    }
}
