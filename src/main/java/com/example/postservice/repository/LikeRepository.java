package com.example.postservice.repository;

import com.example.postservice.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Page<Like> findByUserId(Long userId, Pageable pageable);
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    Optional<Like> deleteByPostIdAndUserId(Long postId, Long userId);
}
