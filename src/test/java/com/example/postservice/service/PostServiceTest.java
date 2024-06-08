//package com.example.postservice.service;
//
//import com.example.postservice.dto.request.PostCreateRequestDto;
//import com.example.postservice.dto.request.PostUpdateRequestDto;
//import com.example.postservice.dto.response.*;
//import com.example.postservice.model.Like;
//import com.example.postservice.model.Post;
//import com.example.postservice.model.PostTag;
//import com.example.postservice.model.Tag;
//import com.example.postservice.model.enums.PostAccessibility;
//import com.example.postservice.repository.*;
//import com.example.postservice.config.JwtTokenProvider;
//import com.example.postservice.config.UserServiceClient;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private LikeRepository likeRepository;
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @Mock
//    private PostTagRepository postTagRepository;
//
//    @Mock
//    private CustomPostRepository customPostRepository;
//
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    private UserServiceClient userServiceClient;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @InjectMocks
//    private PostService postService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void create() {
//        String token = "Bearer token";
//        PostCreateRequestDto dto = new PostCreateRequestDto("userLink", 1L, "url", 1L, 1L, "subject", "title", "content", "thumbnail", "thumbnailImageUrl", List.of("tag1", "tag2"), PostAccessibility.PUBLIC);
//        Post newPost = Post.of(dto, 1L);
//        Post savedPost = Post.of(dto, 1L);
//
//        when(jwtTokenProvider.getUserId(token.substring(7))).thenReturn("1");
//        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
//        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
//        when(tagRepository.save(any(Tag.class))).thenReturn(Tag.from("tag"));
//
//        Long postId = postService.create(token, dto);
//
//        assertNotNull(postId);
//        verify(postRepository, times(1)).save(any(Post.class));
//        verify(tagRepository, times(2)).findByName(anyString());
//        verify(tagRepository, times(2)).save(any(Tag.class));
//        verify(postTagRepository, times(2)).save(any(PostTag.class));
//    }
//
//    @Test
//    void findOne() {
//        Long postId = 1L;
//        Post post;
//
//        when(postRepository.findDetailedById(postId)).thenReturn(Optional.of(post));
//
//        PostFindOneResponseDto responseDto = postService.findOne(postId);
//
//        assertNotNull(responseDto);
//        verify(postRepository, times(1)).findDetailedById(postId);
//    }
//
//    @Test
//    void findOneByUserLinkAndPersonalPostId() {
//        String userLink = "userLink";
//        Long personalPostId = 1L;
//        Post post;
//
//        when(postRepository.findDetailedByUserLinkAndPersonalPostId(userLink, personalPostId)).thenReturn(Optional.of(post));
//
//        PostFindOneResponseDto responseDto = postService.findOneByUserLinkAndPersonalPostId(userLink, personalPostId);
//
//        assertNotNull(responseDto);
//        verify(postRepository, times(1)).findDetailedByUserLinkAndPersonalPostId(userLink, personalPostId);
//    }
//
//    @Test
//    void findAllPostByUserLink() {
//        String userLink = "userLink";
//        List<Post> posts = Collections.singletonList(new Post());
//
//        when(postRepository.findByUserLink(userLink)).thenReturn(posts);
//
//        List<PostFindOneResponseDto> responseDtoList = postService.findAllPostByUserLink(userLink);
//
//        assertNotNull(responseDtoList);
//        assertFalse(responseDtoList.isEmpty());
//        verify(postRepository, times(1)).findByUserLink(userLink);
//    }
//
//    @Test
//    void findRecommendedPosts() {
//        List<Post> posts = Collections.singletonList(new Post());
//
//        when(postRepository.findAll()).thenReturn(posts);
//
//        List<PostForRecommendResponseDto> responseDtoList = postService.findRecommendedPosts();
//
//        assertNotNull(responseDtoList);
//        assertFalse(responseDtoList.isEmpty());
//        verify(postRepository, times(1)).findAll();
//    }
//
//    @Test
//    void findRecommendedWithUserIdPosts() {
//        Long userId = 1L;
//        Integer page = 0;
//        Integer size = 5;
//        List<Like> likes = Collections.singletonList(new Like());
//        List<Post> posts = Collections.singletonList(new Post());
//
//        when(likeRepository.findByUserIdOrderByCreatedTimeDesc(userId, PageRequest.of(page, size))).thenReturn(new PageImpl<>(likes));
//        when(postRepository.findByIdIn(anyList())).thenReturn(posts);
//
//        List<PostForRecommendResponseDto> responseDtoList = postService.findRecommendedWithUserIdPosts(userId, page, size);
//
//        assertNotNull(responseDtoList);
//        assertFalse(responseDtoList.isEmpty());
//        verify(likeRepository, times(1)).findByUserIdOrderByCreatedTimeDesc(userId, PageRequest.of(page, size));
//        verify(postRepository, times(1)).findByIdIn(anyList());
//    }
//
//    @Test
//    void findMainWithLogin() throws IOException {
//        String token = "Bearer token";
//        String userJson = "{\"nickname\":\"test\",\"profileUrl\":\"url\",\"followingList\":[{\"userId\":1}]}";
//        JsonNode root = objectMapper.readTree(userJson);
//        when(userServiceClient.getUserJson(token)).thenReturn(userJson);
//        when(objectMapper.readTree(userJson)).thenReturn(root);
//
//        when(postRepository.findByOrderByLikeCntDesc(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(new Post())));
//        when(postRepository.findByUserIdInOrderByCreatedTimeDesc(anyList(), any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.singletonList(new Post())));
//
//        PostMainWithLoginResponseDto responseDto = postService.findMainWithLogin(token);
//
//        assertNotNull(responseDto);
//        verify(userServiceClient, times(1)).getUserJson(token);
//        verify(postRepository, times(1)).findByOrderByLikeCntDesc(any(PageRequest.class));
//        verify(postRepository, times(1)).findByUserIdInOrderByCreatedTimeDesc(anyList(), any(PageRequest.class));
//    }
//
//    @Test
//    void findPostsByTitle() {
//        String keyword = "title";
//        PageRequest pageable = PageRequest.of(0, 10);
//        Page<Post> posts = new PageImpl<>(Collections.singletonList(new Post()));
//
//        when(postRepository.findByTitleContainingIgnoreCase(keyword, pageable)).thenReturn(posts);
//
//        Page<PostSearchResponseDto> responseDtoPage = postService.findPostsByTitle(keyword, pageable);
//
//        assertNotNull(responseDtoPage);
//        verify(postRepository, times(1)).findByTitleContainingIgnoreCase(keyword, pageable);
//    }
//
//    @Test
//    void findPostsByTag() {
//        String tagName = "tag";
//        PageRequest pageable = PageRequest.of(0, 10);
//        Page<Post> posts = new PageImpl<>(Collections.singletonList(new Post()));
//
//        when(customPostRepository.findByTagName(tagName, pageable)).thenReturn(posts);
//
//        Page<PostSearchResponseDto> responseDtoPage = postService.findPostsByTag(tagName, pageable);
//
//        assertNotNull(responseDtoPage);
//        verify(customPostRepository, times(1)).findByTagName(tagName, pageable);
//    }
//
//    @Test
//    void findPostsBySubject() {
//        String subject = "subject";
//        PageRequest pageable = PageRequest.of(0, 10);
//        Page<Post> posts = new PageImpl<>(Collections.singletonList(new Post()));
//
//        when(postRepository.findBySubject(subject, pageable)).thenReturn(posts);
//
//        Page<PostSearchResponseDto> responseDtoPage = postService.findPostsBySubject(subject, pageable);
//
//        assertNotNull(responseDtoPage);
//        verify(postRepository, times(1)).findBySubject(subject, pageable);
//    }
//
//    @Test
//    void findPostsOrderByLikeCnt() {
//        PageRequest pageable = PageRequest.of(0, 10);
//        Page<Post> posts = new PageImpl<>(Collections.singletonList(new Post()));
//
//        when(postRepository.findByOrderByLikeCntDesc(pageable)).thenReturn(posts);
//
//        Page<PostSearchResponseDto> responseDtoPage = postService.findPostsOrderByLikeCnt(pageable);
//
//        assertNotNull(responseDtoPage);
//        verify(postRepository, times(1)).findByOrderByLikeCntDesc(pageable);
//    }
//
//    @Test
//    void findRecentPosts() {
//        PageRequest pageable = PageRequest.of(0, 10);
//        Page<Post> posts = new PageImpl<>(Collections.singletonList(new Post()));
//
//        when(postRepository.findByOrderByCreatedTimeDesc(pageable)).thenReturn(posts);
//
//        Page<PostSearchResponseDto> responseDtoPage = postService.findRecentPosts(pageable);
//
//        assertNotNull(responseDtoPage);
//        verify(postRepository, times(1)).findByOrderByCreatedTimeDesc(pageable);
//    }
//
//    @Test
//    void update() {
//        Long postId = 1L;
//        PostUpdateRequestDto dto = new PostUpdateRequestDto(postId, "subject", "title", "content", "thumbnail", PostAccessibility.PUBLIC, 1L, 1L);
//        Post post = new Post();
//
//        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
//
//        boolean result = postService.update(dto);
//
//        assertTrue(result);
//        verify(postRepository, times(1)).findById(postId);
//    }
//
//    @Test
//    void delete() {
//        Long postId = 1L;
//
//        doNothing().when(postRepository).deleteById(postId);
//
//        boolean result = postService.delete(postId);
//
//        assertTrue(result);
//        verify(postRepository, times(1)).deleteById(postId);
//    }
//
//    @Test
//    void delete_nonExistentId() {
//        Long postId = 1L;
//
//        doThrow(EmptyResultDataAccessException.class).when(postRepository).deleteById(postId);
//
//        boolean result = postService.delete(postId);
//
//        assertFalse(result);
//        verify(postRepository, times(1)).deleteById(postId);
//    }
//}
