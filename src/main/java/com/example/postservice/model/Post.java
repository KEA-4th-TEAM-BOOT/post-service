package com.example.postservice.model;

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

    @Column(nullable = false)
    private Long userId;

    private Long categoryId;

    private Long subCategoryId;
}
