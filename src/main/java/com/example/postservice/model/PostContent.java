package com.example.postservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(collection = "place_info")
public class PostContent {
    @Id
    private String id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false)
    private String content;
}
