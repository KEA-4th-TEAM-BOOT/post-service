package com.example.postservice.model;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import com.example.postservice.dto.request.ReplyCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "replys")
public class Reply extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public static Reply of(ReplyCreateRequestDto dto, Comment existingComment) {
        return Reply.builder()
                .comment(existingComment)
                .userId(dto.userId())
                .content(dto.content())
                .build();
    }

}
