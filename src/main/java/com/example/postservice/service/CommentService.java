package com.example.postservice.service;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;
import com.example.postservice.repository.CommentJpaRepository;
import com.example.postservice.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean createComment(CommentCreateRequestDto dto) {
        Post existingPost = postJpaRepository.findById(dto.postId())
                .orElseThrow(IllegalArgumentException::new);
        Comment newComment = Comment.ofComment(dto, existingPost);
        commentJpaRepository.save(newComment);
        return true;
    }



}
