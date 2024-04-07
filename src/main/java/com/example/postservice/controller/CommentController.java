package com.example.postservice.controller;

import com.example.postservice.dto.request.CommentCreateRequestDto;
import com.example.postservice.handler.SuccessResponse;
import com.example.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> saveComment(@RequestBody CommentCreateRequestDto dto) {
        return new ResponseEntity<>(SuccessResponse.of(commentService.create(dto)), HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> getPost(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(SuccessResponse.of(commentService.findOne(commentId)), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse> deletePost(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(SuccessResponse.of(commentService.delete(commentId)), HttpStatus.OK);
    }
}
