package com.example.postservice.repository;

import com.example.postservice.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
}
