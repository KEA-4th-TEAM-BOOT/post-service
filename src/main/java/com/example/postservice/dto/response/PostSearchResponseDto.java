package com.example.postservice.dto.response;

import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record PostSearchResponseDto(Long postId, String subject, String title, String thumbnail, String thumbnailImageUrl,
                                    LocalDateTime createdTime, int likeCnt, int size, String userLink, String nickname, String postVoiceFileUrl, long personalPostId, List<String> tagList) {
    public static PostSearchResponseDto from(Post post) {
        List<String> tagList = post.getPostTagList().stream().map(postTag -> postTag.getTag().getName()).toList();

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
                post.getNickname(),
                post.getPostVoiceFileUrl(),
                post.getPersonalPostId(),
                tagList
        );
    }

}
