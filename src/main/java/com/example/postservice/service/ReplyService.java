package com.example.postservice.service;

import com.example.postservice.dto.request.ReplyCreateRequestDto;
import com.example.postservice.dto.response.ReplyFindOneResponseDto;
import com.example.postservice.model.Comment;
import com.example.postservice.model.Reply;
import com.example.postservice.repository.CommentJpaRepository;
import com.example.postservice.repository.ReplyJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyService {
    private final ReplyJpaRepository replyJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean create(ReplyCreateRequestDto dto) {
        Comment existingComment = commentJpaRepository.findById(dto.commentId())
                .orElseThrow(IllegalArgumentException::new);
        Reply newReply = Reply.ofReply(dto, existingComment);
        replyJpaRepository.save(newReply);
        return true;
    }

    public ReplyFindOneResponseDto findOne(Long id) {
        Reply existingReply = replyJpaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reply with id" + id + "not found"));
        return ReplyFindOneResponseDto.from(existingReply);
        // TODO: 응답에 post 넣을건지
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            replyJpaRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }
}
