package com.example.postservice.model;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Getter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Integer hit;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer like;

    public static Post ofPost(PostCreateRequestDto dto) {
        return Post.builder()
                .subject(dto.subject())
                .title(dto.title())
                .thumbnail(dto.thumbnail())
                .accessibility(dto.accessibility())
                .userId(dto.userId())
                .categoryId(dto.categoryId())
                .subCategoryId(dto.subCategoryId())
                .build();
    }

    public void updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        this.subject = postUpdateRequestDto.subject();
        this.title = postUpdateRequestDto.title();
        this.thumbnail = postUpdateRequestDto.thumbnail();
        this.accessibility = postUpdateRequestDto.accessibility();
        this.categoryId = postUpdateRequestDto.categoryId();
        this.subCategoryId = postUpdateRequestDto.subCategoryId();
    }
}
