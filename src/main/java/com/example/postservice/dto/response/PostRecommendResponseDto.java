package com.example.postservice.dto.response;

import com.example.postservice.model.Post;

public record PostRecommendResponseDto(Long postId, Long userId, String content) {
    public static PostRecommendResponseDto from(Post post) {
        return new PostRecommendResponseDto(
                post.getId(),
                post.getUserId(),
                post.getContent()
        );
    }
}
