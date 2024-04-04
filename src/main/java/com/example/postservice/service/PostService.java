package com.example.postservice.service;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.PostFindOneResponseDto;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostJpaRepository postJpaRepository;

    @Transactional
    public boolean create(PostCreateRequestDto postCreateRequestDto) {
        Post newPost = Post.ofPost(postCreateRequestDto);
        postJpaRepository.save(newPost);
        return true;
    }

    public PostFindOneResponseDto findOne(Long id) {
        Post existingPost = postJpaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(PostUpdateRequestDto dto) {
        Post existingPost = postJpaRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Post with id " + dto.id() + " not found"));
        existingPost.updatePost(dto);
        return true;
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            postJpaRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }

}
