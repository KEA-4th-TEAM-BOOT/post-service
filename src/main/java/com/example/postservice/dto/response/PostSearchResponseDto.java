package com.example.postservice.dto.response;

import com.example.postservice.model.Post;

import java.time.LocalDateTime;

public record PostSearchResponseDto(Long postId, String subject, String title, String thumbnail, String thumbnailImageUrl,
                                    LocalDateTime createdTime, int likeCnt, int size, String userLink, long personalPostId) {
    public static PostSearchResponseDto from(Post post) {
        return new PostSearchResponseDto(
                post.getId(),
                post.getSubject(),
                post.getTitle(),
                post.getThumbnail(),
                post.getThumbnailImageUrl(),
                post.getCreatedTime(),
                post.getLikeCnt(),
                post.getCommentList().size(),
                post.getUserLink(),
                post.getPersonalPostId()
        );
    }

}
