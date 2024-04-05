package com.example.postservice.service;

import com.example.postservice.dto.response.LikeUserPostResponseDto;
import com.example.postservice.model.Like;
import com.example.postservice.model.Post;
import com.example.postservice.repository.LikeJpaRepository;
import com.example.postservice.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikeService {
    private final LikeJpaRepository likeJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean addLike(Long postId, Long userId) {
        Post existingPost = postJpaRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + postId + " not found"));
        Like newLike = Like.ofLike(existingPost, userId);
        likeJpaRepository.save(newLike);
        return true;
    }

    public Page<LikeUserPostResponseDto> callUserLike(Long userId, Pageable pageable) {
        Page<Like> userLikeList = likeJpaRepository.findByUserId(userId, pageable);

        return userLikeList.map(like -> new LikeUserPostResponseDto(
                like.getPost().getId(),
                like.getPost().getSubject(),
                like.getPost().getTitle(),
                like.getPost().getThumbnail()
        ));
    }

    @Transactional
    public boolean deleteLike(Long postId, Long userId) {
        try {
            likeJpaRepository.deleteByPostIdAndUserId(postId, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }
}
