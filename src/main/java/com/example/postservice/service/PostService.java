package com.example.postservice.service;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.PostFindOneResponseDto;
import com.example.postservice.dto.response.PostSearchResponseDto;
import com.example.postservice.model.Post;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public boolean create(PostCreateRequestDto postCreateRequestDto) {
        Post newPost = Post.ofPost(postCreateRequestDto);
        postRepository.save(newPost);
        // TODO: tag, content, url 추가
        return true;
    }

    public PostFindOneResponseDto findOne(Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    //게시물 제목 검색
    public Page<PostSearchResponseDto> findPostsByTitle(String keyword, Pageable pageable) {
        Page<Post> postList = postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return postList.map(PostSearchResponseDto::from);
    }

    //subject로 게시물 리스트
    public Page<PostSearchResponseDto> findPostsBySubject(String subject, Pageable pageable) {
        Page<Post> postList = postRepository.findBySubject(subject, pageable);
        return postList.map(PostSearchResponseDto::from);
    }

    //LikeCnt 기준 내림차순으로 게시물 리스트
    public Page<PostSearchResponseDto> findPostsOrderByLikeCnt(Pageable pageable) {
        Page<Post> postList = postRepository.findByOrderByLikeCntDesc(pageable);
        return postList.map(PostSearchResponseDto::from);
    }

    //가장 최근 게시물 모두 조회
    public Page<PostSearchResponseDto> findRecentPosts(Pageable pageable) {
        Page<Post> postList = postRepository.findByOrderByCreatedTimeDesc(pageable);
        return postList.map(PostSearchResponseDto::from);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(PostUpdateRequestDto dto) {
        Post existingPost = postRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Post with id " + dto.id() + " not found"));
        existingPost.updatePost(dto);
        return true;
    }

    @Transactional
    public boolean delete(Long id) {
        try {
            postRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            // TODO: 로깅 처리
            return false;
        }
    }

}
