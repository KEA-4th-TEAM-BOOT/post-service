package com.example.postservice.model;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Reply> replyList = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("false")
    private Boolean replyInclude;

    public static Comment of(CommentCreateRequestDto dto, Post existingPost) {
        return Comment.builder()
                .post(existingPost)
                .userId(dto.userId())
                .content(dto.content())
                .build();
    }
}
