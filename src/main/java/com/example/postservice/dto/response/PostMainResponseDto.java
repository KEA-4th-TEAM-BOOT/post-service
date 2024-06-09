package com.example.postservice.dto.response;

import java.util.List;

public record PostMainResponseDto(
        List<PostSearchResponseDto> topLikedPosts,
        List<PostSearchResponseDto> recentPosts) {

    public static PostMainResponseDto of(List<PostSearchResponseDto> topLikedPosts, List<PostSearchResponseDto> recentPosts) {
        return new PostMainResponseDto(topLikedPosts, recentPosts);
    }
}
