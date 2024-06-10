package com.example.postservice.repository;

import com.example.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Post와 연관된 Comment 및 Reply 엔티티를 함께 로드하는 메서드
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.commentList c LEFT JOIN FETCH c.replyList WHERE p.id = :id")
    Optional<Post> findDetailedById(@Param("id") Long id);

    //subject로 게시물 조회
    Page<Post> findBySubject(String subject, Pageable pageable);

    //title로 게시물 검색
    Page<Post> findByTitleContainingIgnoreCaseOrderByCreatedTimeDesc(String keyword, Pageable pageable);

    // likeCnt가 높은 순서대로 게시물을 조회
    Page<Post> findByOrderByLikeCntDesc(Pageable pageable);

    // 팔로우한 작성자의 최신 포스트 10개를 가져와서 응답
    Page<Post> findByUserIdInOrderByCreatedTimeDesc(List<Long> userIds, Pageable pageable);

    // 가장 최근의 post부터 조회
    Page<Post> findByOrderByCreatedTimeDesc(Pageable pageable);

    // postid 리스트로 찾아 최근 생성된 포스트를 반환
    Page<Post> findByIdInOrderByCreatedTimeDesc(List<Long> postIds, Pageable pageable);

    // 여러 postId로 찾아 반환
    List<Post> findByIdIn(List<Long> postIds);

    // 2024-06-05 이승원 작성
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.commentList c LEFT JOIN FETCH c.replyList WHERE p.userLink = :userLink and p.personalPostId = :personalPostId")
    Optional<Post> findDetailedByUserLinkAndPersonalPostId(@Param("userLink")String userLink, @Param("personalPostId")Long personalPostId);

    List<Post> findByUserLink(String userLink);
}
