package com.example.postservice.service;

import com.example.postservice.config.JwtTokenProvider;
import com.example.postservice.config.UserServiceClient;
import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.*;
import com.example.postservice.model.Post;
import com.example.postservice.model.PostTag;
import com.example.postservice.model.Tag;
import com.example.postservice.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final CustomPostRepository customPostRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public boolean create(String token, PostCreateRequestDto postCreateRequestDto) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        Post newPost = Post.of(postCreateRequestDto,userId);
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
        postRepository.save(newPost);
        return true;
    }

    //post를 불러올때 comment와 reply는 항상 가져와야 하기에 즉시 로딩으로 구현
    public PostFindOneResponseDto findOne(Long id) {
        Post existingPost = postRepository.findDetailedById(id)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    public List<PostForRecommendResponseDto> findRecommendedPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostForRecommendResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<PostForRecommendResponseDto> findRecommendedWithUserIdPosts(Long userId, Integer page, Integer size) {
        List<Long> likedPostIds = likeRepository.findByUserId(userId).stream()
                .map(like -> like.getPost().getId())
                .collect(Collectors.toList());

        List<Post> posts = postRepository.findByIdInOrderByCreatedTimeDesc(likedPostIds, PageRequest.of(page, size)).getContent();
        return posts.stream()
                .map(PostForRecommendResponseDto::from)
                .collect(Collectors.toList());
    }

    public PostMainWithLoginResponseDto findMainWithLogin(String token) {
        // 유저 정보 가져오기
        String userJson = userServiceClient.getUserJson(token);
        String nickname = "";
        String profileUrl = "";
        List<Long> followingIds = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(userJson);
            nickname = root.path("nickname").asText();
            profileUrl = root.path("profileUrl").asText();
            JsonNode followingList = root.path("followingList");
            for (JsonNode node : followingList) {
                followingIds.add(node.path("userId").asLong());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }

        // 전체 포스트에서 좋아요가 가장 높은 순서대로 포스트 10개 가져오기
        Pageable topTenPageable = PageRequest.of(0, 10);
        List<Post> topLikedPosts = postRepository.findByOrderByLikeCntDesc(topTenPageable).getContent();
        List<PostSearchResponseDto> topLikedPostDtos = topLikedPosts.stream()
                .map(PostSearchResponseDto::from)
                .collect(Collectors.toList());

        // 유저가 팔로우한 작성자의 최신 포스트 10개 가져오기
        List<PostSearchResponseDto> followedAuthorPosts = getFollowedAuthorPosts(followingIds, topTenPageable);

        return PostMainWithLoginResponseDto.of(
                nickname,
                profileUrl,
                topLikedPostDtos,
                followedAuthorPosts
        );
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
    // TODO: subject enum으로 변경
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

    private List<PostSearchResponseDto> getFollowedAuthorPosts(List<Long> followingIds, Pageable pageable) {
        List<Post> followedAuthorPosts = postRepository.findByUserIdInOrderByCreatedTimeDesc(followingIds, pageable).getContent();
        return followedAuthorPosts.stream()
                .map(PostSearchResponseDto::from)
                .collect(Collectors.toList());
    }

}
