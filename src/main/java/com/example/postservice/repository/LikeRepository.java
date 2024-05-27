package com.example.postservice.repository;

import com.example.postservice.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Page<Like> findByuserLink(Long userLink, Pageable pageable);
    Optional<Like> findByPostIdAnduserLink(Long postId, Long userLink);
    Optional<Like> deleteByPostIdAnduserLink(Long postId, Long userLink);
}
