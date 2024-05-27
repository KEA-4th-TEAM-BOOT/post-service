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
                                                   @RequestParam(value = "userLink") Long userLink) {
        return new ResponseEntity<>(SuccessResponse.of(likeService.addLike(postId, userLink)), HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<SuccessResponse> inquiryPostsByUserLike(@RequestParam(value = "userLink") Long userLink,
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "12") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(SuccessResponse.of(likeService.findUserLike(userLink, pageable)), HttpStatus.OK);
    }


    @DeleteMapping("")
    public ResponseEntity<SuccessResponse> deleteLike(@RequestParam(value = "postId") Long postId,
                                                   @RequestParam(value = "userLink") Long userLink) {
        return new ResponseEntity<>(SuccessResponse.of(likeService.deleteLike(postId, userLink)), HttpStatus.OK);
    }
}
