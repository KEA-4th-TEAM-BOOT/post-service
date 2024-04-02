package com.example.postservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String title;

    private String thumbnail;

    @Column(nullable = false)
    private Boolean accessibility;

    @Column(nullable = false)
    private Integer hit = 0;

    @Column(nullable = false)
    private Integer like = 0;

    @Column(nullable = false)
    private Long userId;

    private Long categoryId;

    private Long subCategoryId;
}
