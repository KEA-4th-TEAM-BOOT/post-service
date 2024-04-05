package com.example.postservice.repository;

import com.example.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    //subject로 게시물 조회
    Page<Post> findBySubject(String subject, Pageable pageable);

    //title로 게시물 검색
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    // likeCnt가 높은 순서대로 게시물을 조회
    Page<Post> findByOrderByLikeCntDesc(Pageable pageable);

    // 가장 최근의 post부터 조회
    Page<Post> findByOrderByCreatedTimeDesc(Pageable pageable);
}
