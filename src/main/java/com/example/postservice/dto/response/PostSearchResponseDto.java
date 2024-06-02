package com.example.postservice.dto.response;

import com.example.postservice.model.Post;

public record PostSearchResponseDto(Long postId, String subject, String title, String thumbnail, String thumbnailImageUrl) {
    public static PostSearchResponseDto from(Post post) {
        return new PostSearchResponseDto(
                post.getId(),
                post.getSubject(),
                post.getTitle(),
                post.getThumbnail(),
                post.getThumbnailImageUrl()
        );
    }

}
