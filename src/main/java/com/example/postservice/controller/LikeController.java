package com.example.postservice.controller;

import com.example.postservice.handler.SuccessResponse;
import com.example.postservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> addLike(@RequestParam(value = "postId") Long postId,
                                                   @RequestParam(value = "userId") Long userId) {
        return new ResponseEntity<>(SuccessResponse.of(likeService.addLike(postId, userId)), HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<SuccessResponse> inquiryPostsByUserLike(@RequestParam(value = "userId") Long userId,
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(likeService.findUserLike(userId, pageable)), HttpStatus.OK);
    }

    // recommend service 용 like 추출
    @GetMapping("/recommend-service")
    public ResponseEntity<SuccessResponse> extractLikeForRecommend() {
        return new ResponseEntity<>(SuccessResponse.of(likeService.extractLikeForRecommend()), HttpStatus.OK);
    }


    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteLike(@RequestParam(value = "postId") Long postId,
                                                   @RequestParam(value = "userId") Long userId) {
        return new ResponseEntity<>(SuccessResponse.of(likeService.deleteLike(postId, userId)), HttpStatus.OK);
    }
}
