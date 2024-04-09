package com.example.postservice.service;

import com.example.postservice.dto.request.ReplyCreateRequestDto;
import com.example.postservice.dto.response.ReplyFindOneResponseDto;
import com.example.postservice.model.Comment;
import com.example.postservice.model.Reply;
import com.example.postservice.repository.CommentRepository;
import com.example.postservice.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean create(ReplyCreateRequestDto dto) {
        Comment existingComment = commentRepository.findById(dto.commentId())
                .orElseThrow(IllegalArgumentException::new);
        Reply newReply = Reply.of(dto, existingComment);
        replyRepository.save(newReply);
        return true;
    }

    public ReplyFindOneResponseDto findOne(Long id) {
        Reply existingReply = replyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reply with id" + id + "not found"));
        return ReplyFindOneResponseDto.from(existingReply);
        // TODO: 응답에 post 넣을건지
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            replyRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }
}
