package com.example.postservice.service;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import com.example.postservice.dto.response.CommentFindOneResponseDto;
import com.example.postservice.model.Comment;
import com.example.postservice.model.Post;
import com.example.postservice.repository.CommentRepository;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // TODO: replyInclude 설정 추가
    @Transactional(rollbackFor = Exception.class)
    public boolean create(CommentCreateRequestDto dto) {
        Post existingPost = postRepository.findById(dto.postId())
                .orElseThrow(IllegalArgumentException::new);
        Comment newComment = Comment.of(dto, existingPost);
        commentRepository.save(newComment);
        return true;
    }

    public CommentFindOneResponseDto findOne(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment with id" + id + "not found"));
        return CommentFindOneResponseDto.from(existingComment);
        // TODO: 응답에 post 넣을건지
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }

//    @Transactional(rollbackFor = Exception.class)
//    public boolean update(CommentUpdateRequestDto dto) {
//        Comment existingComment = commentRepository.findById(dto.id())
//                .orElseThrow(() -> new NoSuchElementException("Comment with id " + dto.id() + " not found"));
//        existingComment.update(dto);
//        return true;
//    }

}
