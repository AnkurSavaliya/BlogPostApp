package com.ankur.BlogPostApp.service;

import com.ankur.BlogPostApp.dto.CommentRequestDto;
import com.ankur.BlogPostApp.dto.CommentResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;

public interface CommentsService {

    ResponseDataDto<CommentResponseDto> getCommentById(Long commentId);

    ResponseDataDto<CommentResponseDto> createCommentForBlog(CommentRequestDto commentRequestDto);

    ResponseDataDto<String> deleteCommentForBlog(Long commentId);
}
