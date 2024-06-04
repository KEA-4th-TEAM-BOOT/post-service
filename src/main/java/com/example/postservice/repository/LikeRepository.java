package com.example.postservice.repository;

import com.example.postservice.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Page<Like> findByUserId(Long userId, Pageable pageable);
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
    List<Like> findByUserId(Long userId);
    Page<Like> findByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);
}
