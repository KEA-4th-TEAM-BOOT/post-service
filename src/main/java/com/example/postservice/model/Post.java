package com.example.postservice.model;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Comment> commentList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Like> likeList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<PostTag> postTagList;

    @Column(nullable = false)
    private Long userId;

    private Long categoryId;

    private Long subCategoryId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String title;

    private String thumbnail;

    @Column(nullable = false)
    private Boolean accessibility;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hitCnt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer likeCnt;

    public static Post of(PostCreateRequestDto dto) {
        return Post.builder()
                .userId(dto.userId())
                .categoryId(dto.categoryId())
                .subCategoryId(dto.subCategoryId())
                .subject(dto.subject())
                .title(dto.title())
                .thumbnail(dto.thumbnail())
                .accessibility(dto.accessibility())
                .build();
    }

    public void updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        this.categoryId = postUpdateRequestDto.categoryId();
        this.subCategoryId = postUpdateRequestDto.subCategoryId();
        this.subject = postUpdateRequestDto.subject();
        this.title = postUpdateRequestDto.title();
        this.thumbnail = postUpdateRequestDto.thumbnail();
        this.accessibility = postUpdateRequestDto.accessibility();
    }

    public void addPostTag(PostTag postTag) {
        this.postTagList.add(postTag);
    }
}
