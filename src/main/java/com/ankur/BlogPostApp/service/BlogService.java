package com.ankur.BlogPostApp.service;

import com.ankur.BlogPostApp.dto.BlogRequestDto;
import com.ankur.BlogPostApp.dto.BlogResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.model.Blog;

public interface BlogService {

    Blog findBlogByIdOrThrowException(Long blogId);

    ResponseDataDto<BlogResponseDto> getBlogDetailsById(Long blogId);

    ResponseDataDto<BlogResponseDto> createBlog(BlogRequestDto blogRequestDto);

    ResponseDataDto<BlogResponseDto> updateBlog(Long blogId, BlogRequestDto blogRequestDto);

    ResponseDataDto<String> deleteBlog(Long blogId);
}
