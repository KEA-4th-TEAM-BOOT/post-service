package com.example.postservice.dto.response;

import com.example.postservice.model.Post;

public record PostForRecommendResponseDto(Long postId, Long userId, String content) {
    public static PostForRecommendResponseDto from(Post post) {
        return new PostForRecommendResponseDto(
                post.getId(),
                post.getUserId(),
                post.getContent()
        );
    }
}
