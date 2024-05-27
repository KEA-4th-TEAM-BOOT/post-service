package com.example.postservice.dto.response;

import com.example.postservice.model.Post;

public record PostFindOneResponseDto(Long id, String userLink, Long categoryId, Long subCategoryId, String subject, String title, String thumbnail, Boolean accessibility, Integer hitCnt, Integer likeCnt) {

    public static PostFindOneResponseDto from(Post post) {
        return new PostFindOneResponseDto(
                post.getId(),
                post.getUserLink(),
                post.getCategoryId(),
                post.getSubCategoryId(),
                post.getSubject(),
                post.getTitle(),
                post.getThumbnail(),
                post.getAccessibility(),
                post.getHitCnt(),
                post.getLikeCnt()
        );
    }
}
