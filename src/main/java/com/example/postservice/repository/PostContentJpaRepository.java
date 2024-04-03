package com.example.postservice.repository;

import com.example.postservice.model.PostContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostContentJpaRepository extends MongoRepository<PostContent, String> {
}
