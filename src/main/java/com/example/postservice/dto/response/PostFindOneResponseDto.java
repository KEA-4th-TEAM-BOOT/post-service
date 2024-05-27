package com.example.postservice.dto.response;

import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;
import com.example.postservice.model.Reply;

import java.util.List;
import java.util.stream.Collectors;

public record PostFindOneResponseDto(Long id, String userLink, Long categoryId, Long subCategoryId, String subject, String title, String thumbnail, Boolean accessibility, Integer hitCnt, Integer likeCnt, List<CommentResponseDto> comments) {

    public static PostFindOneResponseDto from(Post post) {
        List<CommentResponseDto> commentDtos = post.getCommentList().stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());

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
                post.getLikeCnt(),
                commentDtos
        );
    }

    public record CommentResponseDto(Long id, Long userId, String content, Boolean replyInclude, List<ReplyResponseDto> replies) {
        public static CommentResponseDto from(Comment comment) {
            List<ReplyResponseDto> replyDtos = comment.getReplyList().stream()
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
