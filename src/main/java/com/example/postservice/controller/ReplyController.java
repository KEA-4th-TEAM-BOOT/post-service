package com.example.postservice.controller;


import com.example.postservice.dto.request.ReplyCreateRequestDto;
import com.example.postservice.handler.SuccessResponse;
import com.example.postservice.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> saveReply(@RequestBody ReplyCreateRequestDto dto) {
        return new ResponseEntity<>(SuccessResponse.of(replyService.create(dto)), HttpStatus.OK);
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<SuccessResponse> getReply(@PathVariable("replyId") Long replyId) {
        return new ResponseEntity<>(SuccessResponse.of(replyService.findOne(replyId)), HttpStatus.OK);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<SuccessResponse> deleteReply(@PathVariable("replyId") Long replyId) {
        return new ResponseEntity<>(SuccessResponse.of(replyService.delete(replyId)), HttpStatus.OK);
    }
}
