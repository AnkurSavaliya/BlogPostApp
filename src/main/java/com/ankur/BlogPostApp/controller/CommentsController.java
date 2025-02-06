package com.ankur.BlogPostApp.controller;

import com.ankur.BlogPostApp.dto.CommentRequestDto;
import com.ankur.BlogPostApp.dto.CommentResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private final CommentsService commentsService;

    @Autowired
    private CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }


    @GetMapping("/{commentId}")
    public ResponseDataDto<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        return commentsService.getCommentById(commentId);
    }

    @PostMapping("/create-comment")
    public ResponseDataDto<CommentResponseDto> createCommentForBlog(@RequestBody @Validated CommentRequestDto commentRequestDto) {
        return commentsService.createCommentForBlog(commentRequestDto);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseDataDto<String> deleteCommentForBlog(@PathVariable Long commentId) {
        return commentsService.deleteCommentForBlog(commentId);
    }
}
