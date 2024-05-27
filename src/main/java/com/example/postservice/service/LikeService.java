package com.example.postservice.service;

import com.example.postservice.dto.response.UserLikePostResponseDto;
import com.example.postservice.model.Like;
import com.example.postservice.model.Post;
import com.example.postservice.repository.LikeRepository;
import com.example.postservice.repository.PostRepository;
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
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    // TODO: 중복 생성 문제 해결
    @Transactional(rollbackFor = Exception.class)
    public boolean addLike(Long postId, Long userId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + postId + " not found"));
        Like newLike = Like.of(existingPost, userId);
        likeRepository.save(newLike);
        return true;
    }

    //유저가 좋아요를 누른 게시물 리스트
    public Page<UserLikePostResponseDto> findUserLike(Long userId, Pageable pageable) {
        Page<Like> userLikeList = likeRepository.findByUserId(userId, pageable);
        // TODO: 반환값에 유저id or 유저 정보 추가
        return userLikeList.map(like -> new UserLikePostResponseDto(
                like.getPost().getId(),
                like.getPost().getSubject(),
                like.getPost().getTitle(),
                like.getPost().getThumbnail()
        ));
    }

    @Transactional
    public boolean deleteLike(Long postId, Long userId) {
        try {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }
}
