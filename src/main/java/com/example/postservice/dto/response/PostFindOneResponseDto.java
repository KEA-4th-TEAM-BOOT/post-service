package com.example.postservice.dto.response;

import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;
import com.example.postservice.model.Reply;
import com.example.postservice.model.enums.PostAccessibility;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record PostFindOneResponseDto(Long id, String userLink, String nickname, Long personalPostId, String postVoiceFileUrl, Long categoryId, Long subCategoryId, String subject, String title, String content, String thumbnail, String thumbnailImageUrl, PostAccessibility accessibility, Integer hitCnt, Integer likeCnt,
                                     LocalDateTime createdTime, List<CommentResponseDto> comments, List<String> tagList) {

    public static PostFindOneResponseDto from(Post post) {
        List<CommentResponseDto> commentDtos = post.getCommentList().stream()
                .sorted(Comparator.comparing(Comment::getCreatedTime))
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());

        List<String> tagList = post.getPostTagList().stream().map(postTag -> postTag.getTag().getName()).toList();

        return new PostFindOneResponseDto(
                post.getId(),
                post.getUserLink(),
                post.getNickname(),
                post.getPersonalPostId(),
                post.getPostVoiceFileUrl(),
                post.getCategoryId(),
                post.getSubCategoryId(),
                post.getSubject(),
                post.getTitle(),
                post.getContent(),
                post.getThumbnail(),
                post.getThumbnailImageUrl(),
                post.getAccessibility(),
                post.getHitCnt(),
                post.getLikeCnt(),
                post.getCreatedTime(),
                commentDtos,
                tagList
        );
    }

    public record CommentResponseDto(Long id, Long userId, String content, Boolean replyInclude, List<ReplyResponseDto> replies) {
        public static CommentResponseDto from(Comment comment) {
            List<ReplyResponseDto> replyDtos = comment.getReplyList().stream()
                    .sorted(Comparator.comparing(Reply::getCreatedTime))
                    .map(ReplyResponseDto::from)
                    .collect(Collectors.toList());

            return new CommentResponseDto(
                    comment.getId(),
                    comment.getUserId(),
                    comment.getContent(),
                    comment.getReplyInclude(),
                    replyDtos
            );
        }
    }

    public record ReplyResponseDto(Long id, Long userId, String content) {
        public static ReplyResponseDto from(Reply reply) {
            return new ReplyResponseDto(
                    reply.getId(),
                    reply.getUserId(),
                    reply.getContent()
            );
        }
    }
}
