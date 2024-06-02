package com.example.postservice.dto.response;

import java.util.List;

public record PostMainWithLoginResponseDto(
        String nickname,
        String profileUrl,
        List<PostSearchResponseDto> topLikedPosts,
        List<PostSearchResponseDto> followedAuthorPosts) {

    public static PostMainWithLoginResponseDto of(String nickname, String profileUrl, List<PostSearchResponseDto> topLikedPosts, List<PostSearchResponseDto> followedAuthorPosts) {
        return new PostMainWithLoginResponseDto(nickname, profileUrl, topLikedPosts, followedAuthorPosts);
    }
}
