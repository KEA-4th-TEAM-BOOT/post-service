package com.example.postservice.repository;

import com.example.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Post와 연관된 Comment 및 Reply 엔티티를 함께 로드하는 메서드
    @EntityGraph(attributePaths = {"commentList.replyList"})
    Optional<Post> findDetailedById(Long id);

    //subject로 게시물 조회
    Page<Post> findBySubject(String subject, Pageable pageable);

    //title로 게시물 검색
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    // likeCnt가 높은 순서대로 게시물을 조회
    Page<Post> findByOrderByLikeCntDesc(Pageable pageable);

    // 가장 최근의 post부터 조회
    Page<Post> findByOrderByCreatedTimeDesc(Pageable pageable);
}
