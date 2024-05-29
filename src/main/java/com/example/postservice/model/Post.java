package com.example.postservice.model;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Slf4j
@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private Set<Comment> commentList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<PostTag> postTagList = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String userLink;

    private Long personalPostId;

    private String postVoiceFileUrl;

    private Long categoryId;

    private Long subCategoryId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String thumbnail;

    private String thumbnailImageUrl;

    @Column(nullable = false)
    private Boolean accessibility;

    @ColumnDefault("0")
    private Integer hitCnt;

    @ColumnDefault("0")
    private Integer likeCnt;

    public static Post of(PostCreateRequestDto dto, Long userId) {
        return Post.builder()
                .userId(userId)
                .userLink(dto.userLink())
                .personalPostId(dto.personalPostId())
                .postVoiceFileUrl(dto.postVoiceFileUrl())
                .categoryId(dto.categoryId())
                .subCategoryId(dto.subCategoryId())
                .subject(dto.subject())
                .title(dto.title())
                .content(dto.content())
                .thumbnail(dto.thumbnail())
                .thumbnailImageUrl(dto.thumbnailImageUrl())
                .accessibility(dto.accessibility())
                .build();
    }

    public void updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        this.categoryId = postUpdateRequestDto.categoryId();
        this.subCategoryId = postUpdateRequestDto.subCategoryId();
        this.subject = postUpdateRequestDto.subject();
        this.title = postUpdateRequestDto.title();
        this.content = postUpdateRequestDto.content();
        this.thumbnail = postUpdateRequestDto.thumbnail();
        this.accessibility = postUpdateRequestDto.accessibility();
    }

    public void addPostTag(PostTag postTag) {
        if (this.postTagList == null) {
            log.error("postTagList is null. Initializing...");
            this.postTagList = new ArrayList<>();
        }
        this.postTagList.add(postTag);
        log.debug("Added postTag, list size now: " + this.postTagList.size());
    }
}
