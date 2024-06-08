//package com.example.postservice.controller;
//
//import com.example.postservice.dto.request.PostCreateRequestDto;
//import com.example.postservice.dto.request.PostUpdateRequestDto;
//import com.example.postservice.handler.SuccessResponse;
//import com.example.postservice.service.PostService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class PostControllerTest {
//
//    @Mock
//    private PostService postService;
//
//    @InjectMocks
//    private PostController postController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void savePost() {
//        String token = "Bearer token";
//        PostCreateRequestDto dto = new PostCreateRequestDto(/* parameters */);
//        Long postId = 1L;
//
//        when(postService.create(token, dto)).thenReturn(postId);
//
//        ResponseEntity<SuccessResponse> response = postController.savePost(token, dto);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postId, response.getBody().getData());
//
//        verify(postService, times(1)).create(token, dto);
//    }
//
//    @Test
//    void getPost() {
//        Long postId = 1L;
//        Object postResponse = new Object(); // Replace with actual response type
//
//        when(postService.findOne(postId)).thenReturn(postResponse);
//
//        ResponseEntity<SuccessResponse> response = postController.getPost(postId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponse, response.getBody().getData());
//
//        verify(postService, times(1)).findOne(postId);
//    }
//
//    @Test
//    void getPostByUserLinkAndPersonalPostId() {
//        String userLink = "userLink";
//        Long personalPostId = 1L;
//        Object postResponse = new Object(); // Replace with actual response type
//
//        when(postService.findOneByUserLinkAndPersonalPostId(userLink, personalPostId)).thenReturn(postResponse);
//
//        ResponseEntity<SuccessResponse> response = postController.getPostByUserLinkAndPersonalPostId(userLink, personalPostId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponse, response.getBody().getData());
//
//        verify(postService, times(1)).findOneByUserLinkAndPersonalPostId(userLink, personalPostId);
//    }
//
//    @Test
//    void getAllPostByUserLink() {
//        String userLink = "userLink";
//        Object postResponseList = new Object(); // Replace with actual response type
//
//        when(postService.findAllPostByUserLink(userLink)).thenReturn(postResponseList);
//
//        ResponseEntity<SuccessResponse> response = postController.getAllPostByUserLink(userLink);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponseList, response.getBody().getData());
//
//        verify(postService, times(1)).findAllPostByUserLink(userLink);
//    }
//
//    @Test
//    void getRecommendedPosts() {
//        Object postResponseList = new Object(); // Replace with actual response type
//
//        when(postService.findRecommendedPosts()).thenReturn(postResponseList);
//
//        ResponseEntity<SuccessResponse> response = postController.getRecommendedPosts();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponseList, response.getBody().getData());
//
//        verify(postService, times(1)).findRecommendedPosts();
//    }
//
//    @Test
//    void getRecommendedWithUserIdPosts() {
//        Long userId = 1L;
//        Integer page = 0;
//        Integer size = 5;
//        Object postResponseList = new Object(); // Replace with actual response type
//
//        when(postService.findRecommendedWithUserIdPosts(userId, page, size)).thenReturn(postResponseList);
//
//        ResponseEntity<SuccessResponse> response = postController.getRecommendedWithUserIdPosts(userId, page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponseList, response.getBody().getData());
//
//        verify(postService, times(1)).findRecommendedWithUserIdPosts(userId, page, size);
//    }
//
//    @Test
//    void getMainPost() {
//        String token = "Bearer token";
//        Object postResponse = new Object(); // Replace with actual response type
//
//        when(postService.findMainWithLogin(token)).thenReturn(postResponse);
//
//        ResponseEntity<SuccessResponse> response = postController.getMainPost(token);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponse, response.getBody().getData());
//
//        verify(postService, times(1)).findMainWithLogin(token);
//    }
//
//    @Test
//    void searchPostsByTitle() {
//        String keyword = "title";
//        Integer page = 0;
//        Integer size = 10;
//        PageRequest pageable = PageRequest.of(page, size);
//        Object postResponsePage = new Object(); // Replace with actual response type
//
//        when(postService.findPostsByTitle(keyword, pageable)).thenReturn(postResponsePage);
//
//        ResponseEntity<SuccessResponse> response = postController.searchPostsByTitle(keyword, page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponsePage, response.getBody().getData());
//
//        verify(postService, times(1)).findPostsByTitle(keyword, pageable);
//    }
//
//    @Test
//    void searchPostsByTag() {
//        String keyword = "tag";
//        Integer page = 0;
//        Integer size = 10;
//        PageRequest pageable = PageRequest.of(page, size);
//        Object postResponsePage = new Object(); // Replace with actual response type
//
//        when(postService.findPostsByTag(keyword, pageable)).thenReturn(postResponsePage);
//
//        ResponseEntity<SuccessResponse> response = postController.searchPostsByTag(keyword, page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponsePage, response.getBody().getData());
//
//        verify(postService, times(1)).findPostsByTag(keyword, pageable);
//    }
//
//    @Test
//    void searchPostsBySubject() {
//        String keyword = "subject";
//        Integer page = 0;
//        Integer size = 10;
//        PageRequest pageable = PageRequest.of(page, size);
//        Object postResponsePage = new Object(); // Replace with actual response type
//
//        when(postService.findPostsBySubject(keyword, pageable)).thenReturn(postResponsePage);
//
//        ResponseEntity<SuccessResponse> response = postController.searchPostsBySubject(keyword, page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponsePage, response.getBody().getData());
//
//        verify(postService, times(1)).findPostsBySubject(keyword, pageable);
//    }
//
//    @Test
//    void inquiryPostsOrderByLikeCnt() {
//        Integer page = 0;
//        Integer size = 10;
//        PageRequest pageable = PageRequest.of(page, size);
//        Object postResponsePage = new Object(); // Replace with actual response type
//
//        when(postService.findPostsOrderByLikeCnt(pageable)).thenReturn(postResponsePage);
//
//        ResponseEntity<SuccessResponse> response = postController.inquiryPostsOrderByLikeCnt(page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponsePage, response.getBody().getData());
//
//        verify(postService, times(1)).findPostsOrderByLikeCnt(pageable);
//    }
//
//    @Test
//    void inquiryRecentPosts() {
//        Integer page = 0;
//        Integer size = 10;
//        PageRequest pageable = PageRequest.of(page, size);
//        Object postResponsePage = new Object(); // Replace with actual response type
//
//        when(postService.findRecentPosts(pageable)).thenReturn(postResponsePage);
//
//        ResponseEntity<SuccessResponse> response = postController.inquiryRecentPosts(page, size);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(postResponsePage, response.getBody().getData());
//
//        verify(postService, times(1)).findRecentPosts(pageable);
//    }
//
//    @Test
//    void updatePost() {
//        PostUpdateRequestDto dto = new PostUpdateRequestDto(/* parameters */);
//        boolean updateResult = true;
//
//        when(postService.update(dto)).thenReturn(updateResult);
//
//        ResponseEntity<SuccessResponse> response = postController.updatePost(dto);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updateResult, response.getBody().getData());
//
//        verify(postService, times(1)).update(dto);
//    }
//
//    @Test
//    void deletePost() {
//        Long postId = 1L;
//        boolean deleteResult = true;
//
//        when(postService.delete(postId)).thenReturn(deleteResult);
//
//        ResponseEntity<SuccessResponse> response = postController.deletePost(postId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(deleteResult, response.getBody().getData());
//
//        verify(postService, times(1)).delete(postId);
//    }
//}
