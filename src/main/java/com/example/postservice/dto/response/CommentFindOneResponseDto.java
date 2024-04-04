package com.example.postservice.dto.response;

import com.example.postservice.model.Comment;

public record CommentFindOneResponseDto(Long id, Long userId, String content, Boolean replyInclude) {

    public static CommentFindOneResponseDto from(Comment comment) {
        return new CommentFindOneResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getReplyInclude()
        );
    }
}
