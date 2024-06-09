package com.example.postservice.controller;

import com.example.postservice.dto.request.PostCreateRequestDto;
import com.example.postservice.dto.request.PostUpdateRequestDto;
import com.example.postservice.handler.SuccessResponse;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> savePost(@RequestHeader("Authorization") String token, @RequestBody PostCreateRequestDto dto) {
        Long postId = postService.create(token, dto);
        return new ResponseEntity<>(SuccessResponse.of(postId), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse> getPost(@PathVariable("postId") Long postId) {
        return new ResponseEntity<>(SuccessResponse.of(postService.findOne(postId)), HttpStatus.OK);
    }

    // 2024-06-05 이승원 작성
    @GetMapping("/{userLink}/post/{personalPostId}")
    public ResponseEntity<SuccessResponse> getPostByUserLinkAndPersonalPostId(@PathVariable String userLink, @PathVariable Long personalPostId){
        return new ResponseEntity<>(SuccessResponse.of(postService.findOneByUserLinkAndPersonalPostId(userLink, personalPostId)), HttpStatus.OK);
    }

    @GetMapping("/findAll/{userLink}")
    public ResponseEntity<SuccessResponse> getAllPostByUserLink(@PathVariable String userLink){
        return new ResponseEntity<>(SuccessResponse.of(postService.findAllPostByUserLink(userLink)), HttpStatus.OK);
    }

//    @GetMapping("/main")
//    public ResponseEntity<SuccessResponse> getMainPost() {
//        return new ResponseEntity<>(SuccessResponse.of(postService.findMain()), HttpStatus.OK);
//    }

    // recommend-service에서 사용되는 post
    @GetMapping("/recommend-service")
    public ResponseEntity<SuccessResponse> getRecommendedPosts() {
        return new ResponseEntity<>(SuccessResponse.of(postService.findRecommendedPosts()), HttpStatus.OK);
    }

    // recommend-service에서 사용되는 특정 유저가 좋아요를 누른 post
    @GetMapping("/recommend-service-with-userId")
    public ResponseEntity<SuccessResponse> getRecommendedWithUserIdPosts(@RequestParam("userId") Long userId,
                                                                         @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {
        return new ResponseEntity<>(SuccessResponse.of(postService.findRecommendedWithUserIdPosts(userId, page, size)), HttpStatus.OK);
    }

    @GetMapping("/main")
    public ResponseEntity<SuccessResponse> getMainPost() {
        return new ResponseEntity<>(SuccessResponse.of(postService.findMain()), HttpStatus.OK);
    }

    @GetMapping("/mainWithLogin")
    public ResponseEntity<SuccessResponse> getMainPost(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(SuccessResponse.of(postService.findMainWithLogin(token)), HttpStatus.OK);
    }

    // TODO: 응답에 userId도 포함
    @GetMapping("/search/title")
    public ResponseEntity<SuccessResponse> searchPostsByTitle(@RequestParam(value = "keyword") String keyword,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsByTitle(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/search/tag")
    public ResponseEntity<SuccessResponse> searchPostsByTag(@RequestParam(value = "keyword") String keyword,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsByTag(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/search/subject")
    public ResponseEntity<SuccessResponse> searchPostsBySubject(@RequestParam(value = "keyword") String keyword,
                                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsBySubject(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/inquiry/like")
    public ResponseEntity<SuccessResponse> inquiryPostsOrderByLikeCnt(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsOrderByLikeCnt(pageable)), HttpStatus.OK);
    }

    @GetMapping("/inquiry/recent")
    public ResponseEntity<SuccessResponse> inquiryRecentPosts(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findRecentPosts(pageable)), HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<SuccessResponse> updatePost(@RequestBody PostUpdateRequestDto dto) {
        return new ResponseEntity<>(SuccessResponse.of(postService.update(dto)), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponse> deletePost(@PathVariable("postId") Long postId) {
        return new ResponseEntity<>(SuccessResponse.of(postService.delete(postId)), HttpStatus.OK);
    }
}
