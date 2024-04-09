package com.example.postservice.service;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.PostFindOneResponseDto;
import com.example.postservice.dto.response.PostSearchResponseDto;
import com.example.postservice.model.Post;
import com.example.postservice.model.PostTag;
import com.example.postservice.model.Tag;
import com.example.postservice.repository.CustomPostRepository;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.repository.PostTagRepository;
import com.example.postservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final CustomPostRepository customPostRepository;

    @Transactional
    public boolean create(PostCreateRequestDto postCreateRequestDto) {
        Post newPost = Post.of(postCreateRequestDto);
        postRepository.save(newPost);
        // TODO: content, url 추가
        // tag 설정
        if (postCreateRequestDto.tags() != null && !postCreateRequestDto.tags().isEmpty()) {
            for (String tagName : postCreateRequestDto.tags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.from(tagName)));
                PostTag postTag = PostTag.of(newPost, tag);
                postTagRepository.save(postTag);
                newPost.addPostTag(postTag);
            }
        }
        return true;
    }

    public PostFindOneResponseDto findOne(Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    //게시물 제목으로 검색
    public Page<PostSearchResponseDto> findPostsByTitle(String keyword, Pageable pageable) {
        Page<Post> postList = postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return postList.map(PostSearchResponseDto::from);
    }

    //게시물 태그로 검색
    public Page<PostSearchResponseDto> findPostsByTag(String tagName, Pageable pageable) {
        Page<Post> postList = customPostRepository.findByTagName(tagName, pageable);
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
        // TODO: tag 수정 처리
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
