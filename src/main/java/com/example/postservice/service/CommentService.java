package com.example.postservice.service;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import com.example.postservice.dto.request.CommentUpdateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.CommentFindOneResponseDto;
import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;
import com.example.postservice.repository.CommentJpaRepository;
import com.example.postservice.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean create(CommentCreateRequestDto dto) {
        Post existingPost = postJpaRepository.findById(dto.postId())
                .orElseThrow(IllegalArgumentException::new);
        Comment newComment = Comment.ofComment(dto, existingPost);
        commentJpaRepository.save(newComment);
        return true;
    }

    public CommentFindOneResponseDto findOne(Long id) {
        Comment existingComment = commentJpaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment with id" + id + "not found"));
        return CommentFindOneResponseDto.from(existingComment);
        // TODO: 응답에 post 넣을건지
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            commentJpaRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }

//    @Transactional(rollbackFor = Exception.class)
//    public boolean update(CommentUpdateRequestDto dto) {
//        Comment existingComment = commentJpaRepository.findById(dto.id())
//                .orElseThrow(() -> new NoSuchElementException("Comment with id " + dto.id() + " not found"));
//        existingComment.update(dto);
//        return true;
//    }

}
