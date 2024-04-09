package com.example.postservice.repository;

import com.example.postservice.model.PostTag;
import com.example.postservice.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByTag(Tag tag);
}
