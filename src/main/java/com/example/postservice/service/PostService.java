package com.example.postservice.service;

import com.example.postservice.config.JwtTokenProvider;
import com.example.postservice.config.UserServiceClient;
import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.dto.response.*;
import com.example.postservice.model.Like;
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
import java.util.Map;
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
    public Long create(String token, PostCreateRequestDto postCreateRequestDto) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        Post newPost = Post.of(postCreateRequestDto, userId);

        Post savedPost = postRepository.save(newPost);
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
        return savedPost.getId();
    }


    //post를 불러올때 comment와 reply는 항상 가져와야 하기에 즉시 로딩으로 구현
    public PostFindOneResponseDto findOne(Long id) {
        Post existingPost = postRepository.findDetailedById(id)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + id + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    // 2024-06-05 이승원 작성
    public PostFindOneResponseDto findOneByUserLinkAndPersonalPostId(String userLink, Long personalPostId) {
        Post existingPost = postRepository.findDetailedByUserLinkAndPersonalPostId(userLink, personalPostId)
                .orElseThrow(() -> new NoSuchElementException("Post with id " + personalPostId + " not found"));
        return PostFindOneResponseDto.from(existingPost);
    }

    public List<PostFindOneResponseDto> findAllPostByUserLink(String userLink) {
        List<Post> postList = postRepository.findByUserLink(userLink);
        List<PostFindOneResponseDto> postFindOneResponseDtoList = postList.stream()
                .map(PostFindOneResponseDto::from)
                .collect(Collectors.toList());
        return postFindOneResponseDtoList;
    }


    public List<PostForRecommendResponseDto> findRecommendedPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostForRecommendResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<PostForRecommendResponseDto> findRecommendedWithUserIdPosts(Long userId, Integer page, Integer size) {
        // 1단계: userId로 좋아요를 찾고 createdTime 내림차순으로 정렬하여 가져옴
        List<Like> likes = likeRepository.findByUserIdOrderByCreatedTimeDesc(userId, PageRequest.of(page, size)).getContent();

        // 2단계: 가져온 좋아요의 순서를 유지하여 postIds를 추출
        List<Long> likedPostIds = likes.stream()
                .map(like -> like.getPost().getId())
                .collect(Collectors.toList());

        // 3단계: likedPostIds로 Posts를 찾고 순서를 유지
        List<Post> posts = postRepository.findByIdIn(likedPostIds);

        // 빠른 조회를 위한 맵 생성
        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, post -> post));

        // likedPostIds의 순서를 유지하며 결과 리스트 생성
        List<Post> orderedPosts = likedPostIds.stream()
                .map(postMap::get)
                .toList();

        // DTO로 변환
        return orderedPosts.stream()
                .map(PostForRecommendResponseDto::from)
                .collect(Collectors.toList());
    }


    public PostMainWithLoginResponseDto findMainWithLogin(String token) {
        String accessToken = token.substring(7);
        Long userId = Long.valueOf(jwtTokenProvider.getUserId(accessToken));
        // 유저 정보 가져오기
        String userJson = userServiceClient.getUserJson(userId);
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
