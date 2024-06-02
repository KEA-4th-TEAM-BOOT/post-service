package com.example.postservice.dto.response;

import com.example.postservice.model.Like;

public record LikeForRecommendResponseDto(Long userId, Long postId) {
    public static LikeForRecommendResponseDto from(Like like) {
        return new LikeForRecommendResponseDto(
                like.getUserId(),
                like.getPost().getId()
        );
    }
}
