package com.example.postservice.repository;

import com.example.postservice.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

}
