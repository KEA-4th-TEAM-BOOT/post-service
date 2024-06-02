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
        return new ResponseEntity<>(SuccessResponse.of(postService.create(token, dto)), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse> getPost(@PathVariable("postId") Long postId) {
        return new ResponseEntity<>(SuccessResponse.of(postService.findOne(postId)), HttpStatus.OK);
    }

//    @GetMapping("/main")
//    public ResponseEntity<SuccessResponse> getMainPost() {
//        return new ResponseEntity<>(SuccessResponse.of(postService.findMain()), HttpStatus.OK);
//    }

    @GetMapping("/mainWithLogin")
    public ResponseEntity<SuccessResponse> getMainPost(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(SuccessResponse.of(postService.findMainWithLogin(token)), HttpStatus.OK);
    }


    // TODO: 응답에 userId도 포함
    @GetMapping("/search/title")
    public ResponseEntity<SuccessResponse> searchPostsByTitle(@RequestParam(value = "keyword") String keyword,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsByTitle(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/search/tag")
    public ResponseEntity<SuccessResponse> searchPostsByTag(@RequestParam(value = "keyword") String keyword,
                                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsByTag(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/search/subject")
    public ResponseEntity<SuccessResponse> searchPostsBySubject(@RequestParam(value = "keyword") String keyword,
                                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsBySubject(keyword, pageable)), HttpStatus.OK);
    }

    @GetMapping("/inquiry/like")
    public ResponseEntity<SuccessResponse> inquiryPostsOrderByLikeCnt(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                      @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(postService.findPostsOrderByLikeCnt(pageable)), HttpStatus.OK);
    }

    @GetMapping("/inquiry/recent")
    public ResponseEntity<SuccessResponse> inquiryRecentPosts(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
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
